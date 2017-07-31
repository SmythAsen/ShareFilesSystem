package com.asen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.TreeMap;

public class ClientCore extends Thread{

	private String ip;
	private int port;
	private Socket socket;
	private boolean isconnect = false;
	private TreeMap<Integer, String> files;
	private static int download_status = 2; //��Clientҳ�淴�����������0����ʾ�������أ�1��ʾ������ɣ�-1��ʾ����ʧ��,2��Ĭ��ֵ����ʾ��û�п�ʼ����
	
	public ClientCore(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {

	}
	
	//���ӵ�������
	public void connect() throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
		isconnect = true;
	}

	//��ȡ�ӷ���������������ļ���
	public String getFilesName() {
		String filesName = "";
		if(isconnect){
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				//�ӷ�����ȡ���ļ��б�
				files = (TreeMap<Integer, String>) ois.readObject();
				Set<Integer> ids = files.keySet();
				for (Object id : ids) {
					String s = id + ":" +files.get(id)+"\n";
					filesName += s;
				}
//				System.out.println(filesName);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			return filesName;
		}
		return "δ���յ�����";
	}
	
	/**
	 * ����ͻ��˽��淢������ָ���Ƿ���ȷ�������ȷ�����������������ָ���ͻ��˷���true���������ȷ����ͷ��Ľ��淵��false
	 * @param id
	 * @param dir
	 * @return
	 */
	public boolean sendComm(int id, String dir) {
		boolean isCommExist = false;
		for (int i  : files.keySet()) {
			if(i == id){
				isCommExist = true;
			}
		}
		if(isCommExist){
			PrintStream ps;
			try {
				ps = new PrintStream(socket.getOutputStream());
				ps.println(String.valueOf(id));
				
				//�����ļ�
				download(dir,files.get(id));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	//�����ļ�
	private void download(String dir,String filename) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir,filename)));
		byte[] b = new byte[1024];
		int len = 0;
		download_status = 0;
//		System.out.println("��ʼ���أ�"+filename);
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);;
		}
		download_status = 1;
//		System.out.println("������ɣ�");
	}
	
	//��ȡ�������
	public int getDownloadStatus(){
		return download_status;
	}
}
