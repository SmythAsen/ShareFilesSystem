package com.asen.client;

import java.awt.Color;
import javax.swing.JFrame;

//检查当前下载状态
//不断从ClientCore中获取到下载状态并设置到界面
public class DownloadStatus extends Thread {

	private String ip;
	private int port;
	private ClientCore cc; // 客户端核心类对象
	private ClientFrame cf; // 客户端界面类对象
	private boolean stop = false;//是否停止检查下载状态

	public DownloadStatus(String ip, int port, JFrame frame) {
		this.ip = ip;
		this.port = port;
		this.cf =(ClientFrame) frame;
	}

	@Override
	public void run() {
		cc = new ClientCore(ip, port);
		while (!stop) {
			System.out.println("文件总大小--->"+cc.getFileSize()+"MB");
			System.out.println("当前下载文件大小--->"+cc.getCurrentFileSize()+"MB");
			//取得下载状态
			System.out.println("下载状态---->"+cc.getDownloadStatus());
			switch (cc.getDownloadStatus()) {
			case 0:
				cf.getIsdownload().setText("正在下载..");
				cf.getIsdownload().setForeground(Color.ORANGE);
				break;
			case 1:
				cf.getIsdownload().setText("下载完成！");
				cf.getIsdownload().setForeground(Color.GREEN);
				stop = true;
				break;
			case -1:
				cf.getIsdownload().setText("下载失败！");
				cf.getIsdownload().setForeground(Color.RED);
				stop = true;
				break;
			case 2:
				cf.getIsdownload().setText("开始下载");
				cf.getIsdownload().setForeground(Color.GREEN);
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
