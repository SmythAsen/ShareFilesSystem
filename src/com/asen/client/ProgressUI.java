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
	//���ϻ�ȡ��ǰ����ֵ�����ظ�����
	
	}
	
}
