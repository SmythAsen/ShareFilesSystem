package com.asen.client;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * 为客户端下载提供的委托事件，用于更新进度条
 * @author Asen
 */
public class DownloadTask extends SwingWorker<Void, Void> {

	private JFrame frame;
	private JProgressBar pr;
	private JLabel isdownload;
	private JButton btn_download;
	private ClientCore cc; // 客服端逻辑处理类

	public DownloadTask(JFrame frame, ClientCore cc, JButton btn_download, JProgressBar pr, JLabel isdownload) {
		this.frame = frame;
		this.btn_download = btn_download;
		this.pr = pr;
		this.isdownload = isdownload;
		this.cc = cc;
	}

	/*
	 * 主要任务，当现场启动后，会在后台运行
	 */
	@Override
	public Void doInBackground() {

		int progress = 0; 
//		// 初始化进度条
		setProgress(0);
		//让程序监控进度的程序休眠0.5ms，保证拿到文件的总大小
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//获得进度条的最大值
		long total = cc.getFileSize();
		pr.setMinimum(0);
		pr.setMaximum((int) total);
		String tip = "";
		while (progress < total) {
			progress = cc.getCurrentFileSize();
			pr.setValue(progress);
			tip = "正在下载:"+String.valueOf(progress/(1024*1024))+"MB/"+String.valueOf(total/(1024*1024))+"MB";
			pr.setString(tip);
			isdownload.setText("正在下载..");
			isdownload.setForeground(Color.ORANGE);
			System.out.println("dt:当前下载大小------>" + progress);
		}
		return null;
	}

	@Override
	public void done() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:dd");
		System.out.println(sdf.format(System.currentTimeMillis())+":下载完成！");
		isdownload.setText("下载完成");
		pr.setString("下载完成,文件大小:"+cc.getFileSize()/(1024*1024)+"MB");
		isdownload.setForeground(Color.GREEN);
		Toolkit.getDefaultToolkit().beep(); //下载完成后发出提示音
		btn_download.setEnabled(true);
		frame.setCursor(null); // 关闭鼠标等待状态
	}
}
