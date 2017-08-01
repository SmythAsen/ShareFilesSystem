package com.asen.client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class DownloadUI implements Runnable {

	private ClientCore cc; // 用于获取文件大小等数据
	private ClientFrame cf; // 客户端界面类对象
	private boolean stop = false;//是否停止线程
	private JProgressBar pr; //进度条

	public DownloadUI(ClientCore cc, JFrame frame,JProgressBar pr) {
		this.cc = cc;
		this.cf =(ClientFrame) frame;
		this.pr = pr;
	}
	
	@Override
	public void run() {
		while (!stop) {
			pr.setMaximum((int) cc.getFileSize());//将进度条的最大值设置为文件大大小
			
			System.out.println("文件总大小--->"+cc.getFileSize()+"MB");
			System.out.println("当前下载文件大小--->"+cc.getCurrentFileSize()+"MB");
			//取得下载状态
			System.out.println("下载状态---->"+cc.getDownloadStatus());
			switch (cc.getDownloadStatus()) {
			case 0:
				cf.getIsdownload().setText("正在下载..");
				cf.getIsdownload().setForeground(Color.ORANGE);
				pr.setValue(cc.getCurrentFileSize());
				break;
			case 1:
				cf.getIsdownload().setText("下载完成！");
				cf.getIsdownload().setForeground(Color.GREEN);
				pr.setValue(cc.getCurrentFileSize());
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
