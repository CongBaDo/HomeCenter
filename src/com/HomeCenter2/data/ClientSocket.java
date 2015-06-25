package com.HomeCenter2.data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
	public static final String TAG = "ClientSocket";
	private Socket socket;
	private PrintWriter outPrint;
	private BufferedReader inBuffer;
	private boolean tcpconnect;

	public ClientSocket() {
		socket = null;
		outPrint = null;
		inBuffer = null;
		tcpconnect = false;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public PrintWriter getOutPrint() {
		return outPrint;
	}

	public void setOutPrint(PrintWriter outPrint) {
		this.outPrint = outPrint;
	}

	public BufferedReader getInBuffer() {
		return inBuffer;
	}

	public void setInBuffer(BufferedReader inBuffer) {
		this.inBuffer = inBuffer;
	}

	public boolean isTcpconnect() {
		return tcpconnect;
	}

	public void setTcpconnect(boolean tcpconnect) {
		this.tcpconnect = tcpconnect;
	}

}
