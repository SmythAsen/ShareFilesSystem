package com.asen.client;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class DownloadTask extends SwingWorker<Void, Void> {

	private JFrame frame;
	private JProgressBar pr;
	private JLabel isdownload;
	private JButton btn_download;
	private ClientCore cc; // �ͷ����߼�������

	public DownloadTask(JFrame frame, ClientCore cc, JButton btn_download, JProgressBar pr, JLabel isdownload) {
		this.frame = frame;
		this.btn_download = btn_download;
		this.pr = pr;
		this.isdownload = isdownload;
		this.cc = cc;
	}

	/*
	 * Main task. Executed in background thread.
	 */
	@Override
	public Void doInBackground() {

		int progress = 0; 
//		// ��ʼ��������
		setProgress(0);
		//�ó����ؽ��ȵĳ�������0.5ms����֤�õ��ļ����ܴ�С
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long total = cc.getFileSize();
		pr.setMinimum(0);
		pr.setMaximum((int) cc.getFileSize());
		String tip = "";
		while (progress < total) {
			progress = cc.getCurrentFileSize();
			pr.setValue(progress);
			tip = "��������:"+String.valueOf(progress/(1024*1024))+"MB/"+String.valueOf(total/(1024*1024))+"MB";
			pr.setString(tip);
			isdownload.setText("��������..");
			isdownload.setForeground(Color.ORANGE);
			System.out.println("dt:��ǰ���ش�С------>" + progress);
		}
		return null;
	}

	@Override
	public void done() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:dd");
		System.out.println(sdf.format(System.currentTimeMillis())+":������ɣ�");
		isdownload.setText("�������");
		pr.setString("�������,�ļ���С:"+cc.getFileSize()/(1024*1024)+"MB");
		isdownload.setForeground(Color.GREEN);
		Toolkit.getDefaultToolkit().beep();
		btn_download.setEnabled(true);
		frame.setCursor(null); // turn off the wait cursor
	}
}
