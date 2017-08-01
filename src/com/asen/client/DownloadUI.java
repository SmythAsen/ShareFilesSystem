package com.asen.client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class DownloadUI implements Runnable {

	private ClientCore cc; // ���ڻ�ȡ�ļ���С������
	private ClientFrame cf; // �ͻ��˽��������
	private boolean stop = false;//�Ƿ�ֹͣ�߳�
	private JProgressBar pr; //������

	public DownloadUI(ClientCore cc, JFrame frame,JProgressBar pr) {
		this.cc = cc;
		this.cf =(ClientFrame) frame;
		this.pr = pr;
	}
	
	@Override
	public void run() {
		while (!stop) {
			pr.setMaximum((int) cc.getFileSize());//�������������ֵ����Ϊ�ļ����С
			
			System.out.println("�ļ��ܴ�С--->"+cc.getFileSize()+"MB");
			System.out.println("��ǰ�����ļ���С--->"+cc.getCurrentFileSize()+"MB");
			//ȡ������״̬
			System.out.println("����״̬---->"+cc.getDownloadStatus());
			switch (cc.getDownloadStatus()) {
			case 0:
				cf.getIsdownload().setText("��������..");
				cf.getIsdownload().setForeground(Color.ORANGE);
				pr.setValue(cc.getCurrentFileSize());
				break;
			case 1:
				cf.getIsdownload().setText("������ɣ�");
				cf.getIsdownload().setForeground(Color.GREEN);
				pr.setValue(cc.getCurrentFileSize());
				stop = true;
				break;
			case -1:
				cf.getIsdownload().setText("����ʧ�ܣ�");
				cf.getIsdownload().setForeground(Color.RED);
				stop = true;
				break;
			case 2:
				cf.getIsdownload().setText("��ʼ����");
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
