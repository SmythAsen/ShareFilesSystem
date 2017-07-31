package com.asen.client;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressUI extends Thread {
	
	private JProgressBar pr;
	private JLabel isdownload;
	
	public ProgressUI(JProgressBar pr, JLabel isdownload) {
		this.pr = pr;
		this.isdownload = isdownload;
	}
	
	@Override
	public void run() {
	//不断获取当前进度值并返回给界面
	
	}
	
}
