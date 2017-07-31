package com.asen.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;

/**
 * ��������
 * 1.��ͻ��˷��͹����ļ���������ļ��б�
 * 2.���տͻ��˷��͹�������Ҫ���ص��ļ�ָ��
 * 3.����ָ����ͷ��˷����ļ�
 * @author lx
 */
public class Server extends Thread {

	private File floder; //��Ҫ��������ļ���
	private Socket socket; //��ǰ�����ϵĿͻ���
	
	//ȡ�ù����ļ����е��ļ���
	private String[] filesName;
	private TreeMap<Integer,String> files;
	private File sendFile;//��Ҫ���͵��ļ�
	
	public Server(Socket socket,File floder) {
		super();
		this.floder = floder;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//��ͻ����г������ļ����е��ļ�
		sendFilesList();
		
		//�������Կͻ��˵�����ָ��(��Ҫ������ļ�),������ָ�������˷�����Ӧ���ļ�
		receiveCommond();
	}

	/**
	 * ��ͻ����г���Ҫ������ļ�
	 */
	private void sendFilesList() {
		ObjectOutputStream oos = null;
		filesName = floder.list();
		files = new TreeMap<Integer,String>();
		for (int i = 0; i < filesName.length; i++) {
			files.put(i+1, filesName[i]);
		}
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(files);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �������Կͻ��˵�ָ��
	 */
	private void receiveCommond() {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//��ȡ�ͻ��˷��͹���������ָ��
					int fno = Integer.parseInt(br.readLine());
					System.out.println("��Ҫ���ص��ļ���:"+files.get(fno));
					//��ͻ��˴����ļ�
					sendFile(files.get(fno));
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	/**
	 * ��ͻ��˴����ļ�
	 * @throws IOException 
	 */
	private void sendFile(String file) throws IOException{
		sendFile  = new File(floder,file);
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sendFile));
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		long fileSize = sendFile.length()/(1024*1024);//�ļ���С
		byte[] b = new byte[1024];
		int len = 0;
		//���ļ���С�����ͻ���
		oos.writeLong(fileSize);
		oos.flush();
		System.out.println("�ļ���ʼ����,�ļ���С:"+fileSize+"MB");
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);
		}
		bis.close();
		bos.close();
		System.out.println("�ļ�������ɣ�");
	}
	
	
	public static void main(String[] args) throws IOException {
		File floder = new File("H:\\��Ӱ");
		//����һ������ͨ��
		ServerSocket ss = new ServerSocket(2345);
		System.out.println("������������...");
		Socket socket = null;
		while (true) {
			//�����ͻ�������
			socket = ss.accept();
			System.out.println("�ͻ��������ӣ�"+socket.getInetAddress().getHostAddress());
			new Server(socket,floder).start();//���ÿһ�����ӵĿͻ��˵�������һ���߳�
		}
	}
}
