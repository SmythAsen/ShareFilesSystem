package com.asen.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.TreeMap;

/**
 * 服务器端
 * 1.向客户端发送共享文件夹里面的文件列表
 * 2.接收客户端发送过来的需要下载的文件指令
 * 3.根据指令向客服端发送文件
 * @author lx
 */
public class Server extends Thread {

	private File floder; //需要被共享的文件夹
	private Socket socket; //当前连接上的客户端
	
	//取得共享文件夹中的文件名
	private String[] filesName;
	private TreeMap<Integer,String> files;
	private File sendFile;//需要发送的文件
	
	public Server(Socket socket,File floder) {
		super();
		this.floder = floder;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//向客户端列出共享文件夹中的文件
		sendFilesList();
		
		//接收来自客户端的下载指令(需要传输的文件),并根据指令向服务端发送相应的文件
		receiveCommond();
	}

	/**
	 * 向客户端列出需要共享的文件
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
	 * 接收来自客户端的指令
	 */
	private void receiveCommond() {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//获取客户端发送过来的下载指令
					int fno = Integer.parseInt(br.readLine());
					System.out.println("需要下载的文件是:"+files.get(fno));
					//向客户端传送文件
					sendFile(files.get(fno));
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	/**
	 * 向客户端传输文件
	 * @throws IOException 
	 */
	private void sendFile(String file) throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:dd");
		sendFile  = new File(floder,file);
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sendFile));
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		long fileSize = sendFile.length();//文件大小
		byte[] b = new byte[1024];
		int len = 0;
		//把文件大小传给客户端
		oos.writeLong(fileSize);
		oos.flush();
		System.out.println("文件开始传输,文件大小:"+fileSize);
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);
		}
		bis.close();
		bos.close();
		System.out.println(sdf.format(System.currentTimeMillis())+":文件传输完成！");
	}
	
	
	public static void main(String[] args) throws IOException {
		File floder = new File("H:\\电影");
		//创建一个网络通道
		ServerSocket ss = new ServerSocket(2345);
		System.out.println("服务器已启动...");
		Socket socket = null;
		while (true) {
			//监听客户端连接
			try {
				socket = ss.accept();
			} catch (IOException e) {
				System.out.println("客服端断开连接,IP地址为"+socket.getInetAddress().getHostAddress());
			}
			System.out.println("客户端已连接："+socket.getInetAddress().getHostAddress());
			new Server(socket,floder).start();//针对每一个连接的客户端单独开启一个线程
		}
	}
}
