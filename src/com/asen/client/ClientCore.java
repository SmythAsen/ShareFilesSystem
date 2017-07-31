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
	private static int download_status = 2; //向Client页面反馈下载情况，0，表示正在下载，1表示下载完成，-1表示下载失败,2是默认值，表示还没有开始下载
	
	public ClientCore(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {

	}
	
	//连接到服务器
	public void connect() throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
		isconnect = true;
	}

	//获取从服务器传输过来的文件名
	public String getFilesName() {
		String filesName = "";
		if(isconnect){
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				//从服务器取得文件列表
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
		return "未接收到内容";
	}
	
	/**
	 * 检验客户端界面发过来的指令是否正确，如果正确则向服务器发送下载指令并向客户端返回true，如果不正确则向客服的界面返回false
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
				
				//下载文件
				download(dir,files.get(id));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	//下载文件
	private void download(String dir,String filename) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir,filename)));
		byte[] b = new byte[1024];
		int len = 0;
		download_status = 0;
//		System.out.println("开始下载："+filename);
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);;
		}
		download_status = 1;
//		System.out.println("下载完成！");
	}
	
	//获取下载情况
	public int getDownloadStatus(){
		return download_status;
	}
}
