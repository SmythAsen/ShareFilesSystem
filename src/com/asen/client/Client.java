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
 * 客户端 1.接收服务端传送过来的文件列表 2.向服务端发送指令(需要下载的文件) 3.接收来自服务端的文件 4.将文件保存到本地
 * 
 * @author lx
 */
public class Client extends Thread {

	private Socket socket;// 需要连接的服务器
	private File saveDir;//存放下载文件的路径

	public Client(Socket socket,File saveDir) {
		super();
		this.socket = socket;
		this.saveDir = saveDir;
	}

	@Override
	public void run() {
		// 接收服务端传输过来的文件列表，并向服务端发送需要下载的文件指令
		receiveFilesList();
	}

	/**
	 * 接收服务器端发送过来的文件列表
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
			//检验指令后发送
			checkCommond(files);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向服务器发送指令(需要下载的文件)
	 * @param comm
	 * @throws IOException 
	 */
	private void sendCommond(int comm) throws IOException{
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println(String.valueOf(comm));
	}

	/**
	 * 检验输入的指令是否正确，如果正确则发送指令
	 * @param ids 
	 * @throws IOException 
	 */
	private void checkCommond(TreeMap<Integer, String> files) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String comm = br.readLine();
		//检查输入内容是否为数字
		if(comm.matches("^\\d*$")){
			//将输入的编号转换为int类型
			int inputNo = Integer.parseInt(comm);
			boolean isCommExist = false;
			for (int i : files.keySet()) {
				if(i == inputNo){
					isCommExist = true;
					break;
				}
			}
			if(isCommExist){
				//发送指令
				sendCommond(inputNo);
				//下载文件
				download(files.get(inputNo));
			}else{
				System.out.println("指令不存在,请重新输入！");
				checkCommond(files);
			}
		}else{
			System.out.println("请输入需要下载文件对应的数字指令！");
			checkCommond(files);
		}
	}

	/**
	 * 下载服务器端传送过来的文件到本地
	 * @throws IOException 
	 */
	private void download(String filename) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(saveDir,filename)));
		byte[] b = new byte[1024];
		int len = 0;
		System.out.println("开始下载："+filename);
		while((len = bis.read(b)) != -1){
			bos.write(b, 0, len);;
		}
		System.out.println("下载完成！");
	}

	public static void main(String[] args) {
		File saveDir = new File("D:\\test");
		try {
			Socket socket = new Socket("192.168.3.3", 2345);
			new Client(socket,saveDir).start();// 启动客户端
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
