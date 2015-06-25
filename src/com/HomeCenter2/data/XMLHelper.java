package com.HomeCenter2.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.HomeCenter2.HomeCenterUIEngine;
import com.HomeCenter2.RegisterService;
import com.HomeCenter2.house.Area;
import com.HomeCenter2.house.Device;
import com.HomeCenter2.house.House;
import com.HomeCenter2.house.Room;

public class XMLHelper {
	//public static String TAG = "XMLHelper";

	public static boolean createDeviceFileXml(List<Object> objects) {
		try {
			boolean isCreated;
			File dir = new File(configManager.FOLDERNAME);
			if (!dir.isDirectory()) {
				isCreated = dir.mkdirs();
				//Log.d(TAG, "not is Directory::" + isCreated);
			}
			File file = new File(dir, configManager.DEVICE_FILENAME);
			if (!file.isFile()) {
				isCreated = file.createNewFile();
				//Log.d(TAG, "not is File:" + isCreated);
			}
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			Element rootElement = document.createElement(configManager.HOUSE);
			createNodes(document, rootElement, objects);

			document.appendChild(rootElement);

			// document = documentBuilder.newDocument();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);

			//Log.d(TAG, "created file devices.xml");
			return true;

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private static void createNodes(Document document, Element rootElement,
			List<Object> objects) {
		int size = objects.size();
		Object object;
		Element elementArea = null;
		Element elementRoom = null;
		Element elementDevice = null;

		for (int i = 0; i < size; i++) {
			object = objects.get(i);
			if (object != null && object instanceof Area) {
				if (elementArea != null) {
					if (elementRoom != null) {
						elementArea.appendChild(elementRoom);
						elementRoom = null;
					}
					rootElement.appendChild(elementArea);
					elementArea = null;
				}
				elementArea = ((Area) object).addAreaToXML(document);
				/*
				 * Log.d(TAG, "add child::area::" + (elementArea == null) +
				 * " , id::" + ((Area) object).getId() + " , mNameTV::" +
				 * ((Area) object).getName());
				 */

			} else if (object != null && object instanceof Room) {
				//Log.d(TAG, "createNodes::");
				if (elementRoom != null) {
					if (elementArea != null) {
						elementArea.appendChild(elementRoom);
					}
					elementRoom = null;
				}
				elementRoom = ((Room) object).addRoomToXML(document);
				/*Log.d(TAG,
						"createNodes::add child::room::"
								+ (elementRoom == null) + " , id::"
								+ ((Room) object).getId() + " , mNameTV::"
								+ ((Room) object).getName() + " , active: "
								+ ((Room) object).isActive());*/

			} else if (object != null && object instanceof Device) {
				if (elementRoom != null) {
					((Device) object).addDeviceToXML(document, elementRoom);
					/*
					 * Log.d(TAG, "add device from room::" + " , id::" +
					 * ((Device) object).getId() + " , mNameTV::" + ((Device)
					 * object).getName());
					 */
				} else if (elementArea != null) {
					((Device) object).addDeviceToXML(document, elementArea);
					/*
					 * Log.d(TAG, "add device from area::" + " , id::" +
					 * ((Device) object).getId() + " , mNameTV::" + ((Device)
					 * object).getName());
					 */
				}
			}

		}
		if (elementRoom != null) {
			if (elementArea != null) {
				elementArea.appendChild(elementRoom);
			}
		}
		if (elementArea != null) {
			rootElement.appendChild(elementArea);
		}
	}

	public static List<Object> readFileXml() {
		List<Object> objects = null;
		try {
			File dir = new File(configManager.FOLDERNAME);
			//Log.d("TMT" + TAG, configManager.FOLDERNAME);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			File file = new File(dir, configManager.DEVICE_FILENAME);
			if (!file.isFile()) {
				file.createNewFile();
				return objects;
			}
			/*
			 * if (file.isFile() == false){ createFileXml(); }
			 */
			objects = new ArrayList<Object>();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(configManager.AREA);
			if (nodeList != null) {
				int size = nodeList.getLength();
				Area area = null;
				for (int i = 0; i < size; i++) {
					Node fs1 = nodeList.item(i);
					NodeList lst = null;
					if (fs1 != null) {
						lst = fs1.getChildNodes();
					}
					if (lst != null && lst.getLength() > 0) {
						area = new Area();
						objects.add(area);
						area.readAreaFromXML(lst, objects);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	public static boolean createConfigFileXml(Bundle bundle) {
		try {
			boolean isCreated;
			File dir = new File(configManager.FOLDERNAME);
			if (!dir.isDirectory()) {
				isCreated = dir.mkdirs();
				//Log.d(TAG, "not is config directory::" + isCreated);
			}
			File file = new File(dir, configManager.CONFIG_FILENAME);
			if (!file.isFile()) {
				isCreated = file.createNewFile();
				//Log.d(TAG, "not is config file:" + isCreated);
			}
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			Element rootElement = document.createElement(configManager.CONFIG);

			createConfigNodes(document, rootElement, bundle);

			document.appendChild(rootElement);

			// document = documentBuilder.newDocument();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);

			//Log.d(TAG, "created file devices.xml");
			return true;

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private static void createConfigNodes(Document document,
			Element rootElement, Bundle bundle) {
		//Remote
		String serverRemote = bundle.getString(configManager.SERVER_REMOTE);
		int portRemote = bundle.getInt(configManager.PORT_REMOTE, -1);

		Element elementSever = null;
		Element elementPort = null;
		if (!TextUtils.isEmpty(serverRemote)) {
			elementSever = document.createElement(configManager.SERVER_REMOTE);
			elementSever.appendChild(document.createTextNode(serverRemote));
			rootElement.appendChild(elementSever);
		}
		if (portRemote>0) {
			elementPort = document.createElement(configManager.PORT_REMOTE);
			elementPort.appendChild(document.createTextNode(String.valueOf(portRemote)));
			rootElement.appendChild(elementPort);
		}
		
		//Local
		String serverLocal = bundle.getString(configManager.SERVER_LOCAL);
		int portLocal = bundle.getInt(configManager.PORT_LOCAL, -1);
		
		if (!TextUtils.isEmpty(serverLocal)) {
			elementSever = document.createElement(configManager.SERVER_LOCAL);
			elementSever.appendChild(document.createTextNode(serverLocal));
			rootElement.appendChild(elementSever);
		}
		if (portLocal>0) {
			elementPort = document.createElement(configManager.PORT_LOCAL);
			elementPort.appendChild(document.createTextNode(String.valueOf(portLocal)));
			rootElement.appendChild(elementPort);
		}
		
		//type
		
		int isRemote = bundle.getInt(configManager.IP_TYPE, -1);
		if (isRemote>=0) {
			elementPort = document.createElement(configManager.IP_TYPE);
			elementPort.appendChild(document.createTextNode(String.valueOf(isRemote)));
			rootElement.appendChild(elementPort);
		}
	}

	public static Bundle readConfigFileXml() {
		Bundle bundle = null;
		try {
			File dir = new File(configManager.FOLDERNAME);
			//Log.d("TMT" + TAG, configManager.FOLDERNAME);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			File file = new File(dir, configManager.CONFIG_FILENAME);
			if (!file.isFile()) {
				file.createNewFile();
				return bundle;
			}
			/*
			 * if (file.isFile() == false){ createFileXml(); }
			 */
			bundle = new Bundle();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName(configManager.CONFIG);
			if (nodeList != null) {
				int size = nodeList.getLength();				
				for (int i = 0; i < size; i++) {
					Node fs1 = nodeList.item(i);
					NodeList lst = null;
					if (fs1 != null) {
						lst = fs1.getChildNodes();
					}
					if (lst != null && lst.getLength() > 0) {
						int sizeChild = lst.getLength();
						for (int j = 0; j < sizeChild; j++) {
							Node fsNode = lst.item(j);
							if (fsNode != null) {
								String name = fsNode.getNodeName();
								String value;
								if (name.equals(configManager.SERVER_REMOTE)) {
									value = fsNode.getFirstChild()
											.getNodeValue();
									bundle.putString(configManager.SERVER_REMOTE,
											value);
								} else if (name.equals(configManager.PORT_REMOTE)) {
									value = fsNode.getFirstChild()
											.getNodeValue();
									int port = -1;
									if(!TextUtils.isEmpty(value)){
										port = Integer.parseInt(value);
									}
									bundle.putInt(configManager.PORT_REMOTE, port);
								}else if (name.equals(configManager.SERVER_LOCAL)) {
									value = fsNode.getFirstChild()
											.getNodeValue();
									bundle.putString(configManager.SERVER_LOCAL,
											value);
								} else if (name.equals(configManager.PORT_LOCAL)) {
									value = fsNode.getFirstChild()
											.getNodeValue();
									int port = -1;
									if(!TextUtils.isEmpty(value)){
										port = Integer.parseInt(value);
									}
									bundle.putInt(configManager.PORT_LOCAL, port);
								} else if (name.equals(configManager.IP_TYPE)) {
									value = fsNode.getFirstChild()
											.getNodeValue();
									int remote = 0;
									if (!TextUtils.isEmpty(value)) {
										remote = Integer.parseInt(value);
									}
									bundle.putInt(configManager.IP_TYPE, remote);
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bundle;
	}
}
