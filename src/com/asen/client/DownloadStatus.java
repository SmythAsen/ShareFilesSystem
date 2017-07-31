package com.asen.client;

import java.awt.Color;
import javax.swing.JFrame;

//��鵱ǰ����״̬
//���ϴ�ClientCore�л�ȡ������״̬�����õ�����
public class DownloadStatus extends Thread {

	private String ip;
	private int port;
	private ClientCore cc; // �ͻ��˺��������
	private ClientFrame cf; // �ͻ��˽��������
	private boolean stop = false;//�Ƿ�ֹͣ�������״̬

	public DownloadStatus(String ip, int port, JFrame frame) {
		this.ip = ip;
		this.port = port;
		this.cf =(ClientFrame) frame;
	}

	@Override
	public void run() {
		cc = new ClientCore(ip, port);
		while (!stop) {
			System.out.println("�ļ��ܴ�С--->"+cc.getFileSize()+"MB");
			System.out.println("��ǰ�����ļ���С--->"+cc.getCurrentFileSize()+"MB");
			//ȡ������״̬
			System.out.println("����״̬---->"+cc.getDownloadStatus());
			switch (cc.getDownloadStatus()) {
			case 0:
				cf.getIsdownload().setText("��������..");
				cf.getIsdownload().setForeground(Color.ORANGE);
				break;
			case 1:
				cf.getIsdownload().setText("������ɣ�");
				cf.getIsdownload().setForeground(Color.GREEN);
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
