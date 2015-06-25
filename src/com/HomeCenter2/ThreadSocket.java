package com.HomeCenter2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.HomeCenter2.data.ClientSocket;
import com.HomeCenter2.data.XMLHelper;
import com.HomeCenter2.data.configManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class ThreadSocket implements Runnable {
	public static final String TAG = "ThreadSocket";

	ClientSocket clientSocket;

	public ThreadSocket(ClientSocket _clientSocket) {
		super();
		clientSocket = _clientSocket;

	}

	public void run() {
		Log.d(TAG, "run");
		boolean isConnected = false;
		String ip;
		int port;
		try {
			if (RegisterService.getService() == null
					|| RegisterService.getService().getUIEngine() == null)
				return;

			ip = RegisterService.getService().getUIEngine().getHouse()
					.getIpAddress();
			InetAddress serverAddr = null;

			Bundle bundle = XMLHelper.readConfigFileXml();

			boolean isRemote = false;

			if (bundle != null) {
				int remote = bundle.getInt(configManager.IP_TYPE, -1);
				isRemote = remote > 0 ? true : false;
			}

			// remote
			if (isRemote) {
				Log.d(TAG, "login by remote");
				if (TextUtils.isEmpty(ip)) {
					if (bundle != null) {
						ip = bundle.getString(configManager.SERVER_REMOTE);
						if (TextUtils.isEmpty(ip)) {
							ip = configManager.SERVER_IP;
						}
					}
				}
				if (!TextUtils.isEmpty(ip)) {
					serverAddr = InetAddress.getByName(ip);
				}

				port = RegisterService.getService().getUIEngine().getHouse()
						.getPort();

				if (port < 0) {
					if (bundle != null) {
						port = bundle.getInt(configManager.PORT_REMOTE, -1);
						if (port < 0) {
							port = configManager.SERVER_PORT;
						}
					}
				}
			} else {
				Log.d(TAG, "login by local");
				// local
				if (TextUtils.isEmpty(ip)) {
					if (bundle != null) {
						ip = bundle.getString(configManager.SERVER_LOCAL);
						if (TextUtils.isEmpty(ip)) {
							ip = configManager.SERVER_IP;
						}
					}
				}
				if (!TextUtils.isEmpty(ip)) {
					serverAddr = InetAddress.getByName(ip);
					Log.e(TAG, "AAAAA serverAddr 2: "+serverAddr);
				}

				port = RegisterService.getService().getUIEngine().getHouse()
						.getPort();

				if (port < 0) {
					if (bundle != null) {
						port = bundle.getInt(configManager.PORT_LOCAL, -1);
						if (port < 0) {
							port = configManager.SERVER_PORT;
						}
					}
				}
			}
			// Log.d(TAG, "socket: ip: " + ip + " port : " + port);
			clientSocket = RegisterService.getService().getUIEngine()
					.getSocket();
			if (clientSocket == null)
				return;
			Socket socket = null;
			try {
				socket = new Socket();
				clientSocket.setSocket(socket);
				if (socket != null) {//nganguyen
					socket.connect(new InetSocketAddress(serverAddr, port));
					if (socket.isConnected()) {
						clientSocket.setOutPrint(new PrintWriter(socket.getOutputStream(), true));
						clientSocket.setInBuffer(new BufferedReader(new InputStreamReader(socket.getInputStream())));
						clientSocket.setTcpconnect(true);
						isConnected = true;
					}
				}
			} catch (IOException ex) {
				Log.e(TAG, "ex: " + ex.toString());
			}
		} catch (UnknownHostException e1) {
			Log.e(TAG, "ex: " + e1.toString());
			// e1.printStackTrace();
		} catch (IOException e1) {
			// Log.d(TAG, "run:: Read failed::" + e1.toString());
			// e1.printStackTrace();
			Log.e(TAG, "ex: " + e1.toString());
		} finally {
			RegisterService service = RegisterService.getService();
			if (service != null) {
				HomeCenterUIEngine uiEngine = service.getUIEngine();
				if (uiEngine != null) {
					Log.e(TAG, "create socket "
							+ (isConnected ? "done" : "fail"));
					uiEngine.notifyConnectSocketObserver(isConnected);
				}
			}
		}
	}
}
