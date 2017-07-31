package com.asen.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.TreeMap;

/**
 * �ͻ��� 1.���շ���˴��͹������ļ��б� 2.�����˷���ָ��(��Ҫ���ص��ļ�) 3.�������Է���˵��ļ� 4.���ļ����浽����
 * 
 * @author lx
 */
public class Client extends Thread {

	private Socket socket;// ��Ҫ���ӵķ�����
	private File saveDir;//��������ļ���·��

	public Client(Socket socket,File saveDir) {
		super();
		this.socket = socket;
		this.saveDir = saveDir;
	}

	@Override
	public void run() {
		// ���շ���˴���������ļ��б��������˷�����Ҫ���ص��ļ�ָ��
		receiveFilesList();
	}

	/**
	 * ���շ������˷��͹������ļ��б�
	 */
	private void receiveFilesList() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			TreeMap<Integer, String> files = (TreeMap<Integer, String>) ois.readObject();
			Set<Integer> ids = files.keySet();
			for (Object id : ids) {
				System.out.println(id + "--->" + files.get(id));
			}
			//����ָ�����
			checkCommond(files);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����������ָ��(��Ҫ���ص��ļ�)
	 * @param comm
	 * @throws IOException 
	 */
	private void sendCommond(int comm) throws IOException{
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println(String.valueOf(comm));
	}

	/**
	 * ���������ָ���Ƿ���ȷ�������ȷ����ָ��
	 * @param ids 
	 * @throws IOException 
	 */
	private void checkCommond(TreeMap<Integer, String> files) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String comm = br.readLine();
		//������������Ƿ�Ϊ����
		if(comm.matches("^\\d*$")){
			//������ı��ת��Ϊint����
			int inputNo = Integer.parseInt(comm);
			boolean isCommExist = false;
			for (int i : files.keySet()) {
				if(i == inputNo){
					isCommExist = true;
					break;
				}
			}
			if(isCommExist){
				//����ָ��
				sendCommond(inputNo);
				//�����ļ�
				download(files.get(inputNo));
			}else{
				System.out.println("ָ�����,���������룡");
				checkCommond(files);
			}
		}else{
			System.out.println("��������Ҫ�����ļ���Ӧ������ָ�");
			checkCommond(files);
		}
	}

	/**
	 * ���ط������˴��͹������ļ�������
	 * @throws IOException 
	 */
	private void download(String filename) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(saveDir,filename)));
		byte[] b = new byte[1024];
		int len = 0;
		System.out.println("��ʼ���أ�"+filename);
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);;
		}
		System.out.println("������ɣ�");
	}

	public static void main(String[] args) {
		File saveDir = new File("D:\\test");
		try {
			Socket socket = new Socket("192.168.3.3", 2345);
			new Client(socket,saveDir).start();// �����ͻ���
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
