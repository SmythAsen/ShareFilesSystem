package com.asen.client;

public class Test {

	public static void main(String[] args) {

		String ip = "192.168.3.3";
		int port = 2345;
		ClientCore cc = new ClientCore(ip,port);
		System.out.println(cc.getDownloadStatus());;
	}

}
