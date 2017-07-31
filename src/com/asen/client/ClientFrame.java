package com.asen.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class ClientFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = -8586211375465435563L;
	private JTextField tf_ip; // 需要连接的服务器ip地址
	private JTextField tf_port; // 需要连接的服务器接口
	private JTextField tf_id;
	private JTextField tf_savedir; // 保存文件的路径
	private JButton btn_download;
	private JButton btn_connect;
	private JLabel label_isconnect;
	private String filesName; // 服务器传输过来的文件
	private JTextPane filelist; // 展示服务器传输过来的文件
	private ClientCore cc;
	private JLabel isdownload;
	private JProgressBar pr;
	private boolean isTimeToCheckDownload = false;

	/**
	 * 创建窗体
	 */
	public ClientFrame() {
		// 设置主窗体属性
		setTitle("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));

		// 窗体title
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		getContentPane().add(Title_panel);
		JLabel Title_label = new JLabel("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		Title_label.setHorizontalAlignment(SwingConstants.CENTER);
		Title_label.setFont(new Font("微软雅黑", Font.PLAIN, 26));
		Title_panel.add(Title_label);

		/********** IP、端口栏 **********/
		JLabel label_ip = new JLabel("\u670D\u52A1\u5668IP\uFF1A");
		label_ip.setHorizontalAlignment(SwingConstants.CENTER);
		label_ip.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_ip.setBounds(28, 90, 94, 28);
		getContentPane().add(label_ip);

		tf_ip = new JTextField();
		tf_ip.setHorizontalAlignment(SwingConstants.LEFT);
		tf_ip.setBounds(126, 90, 216, 28);
		getContentPane().add(tf_ip);
		tf_ip.setColumns(10);

		JLabel label_port = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		label_port.setHorizontalAlignment(SwingConstants.CENTER);
		label_port.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_port.setBounds(356, 90, 87, 28);
		getContentPane().add(label_port);

		tf_port = new JTextField();
		tf_port.setHorizontalAlignment(SwingConstants.LEFT);
		tf_port.setColumns(10);
		tf_port.setBounds(433, 90, 108, 32);
		getContentPane().add(tf_port);

		/********** 连接栏 **********/
		btn_connect = new JButton("\u8FDE \u63A5");
		btn_connect.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btn_connect.setBounds(243, 152, 99, 27);
		getContentPane().add(btn_connect);

		JLabel label_connect_status = new JLabel("\u8FDE\u63A5\u72B6\u6001\uFF1A");
		label_connect_status.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_connect_status.setBounds(379, 157, 87, 22);
		getContentPane().add(label_connect_status);

		label_isconnect = new JLabel("\u672A\u8FDE\u63A5");
		label_isconnect.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_isconnect.setBounds(460, 157, 87, 22);
		getContentPane().add(label_isconnect);

		JLabel label_line = new JLabel("------------------------------------------------------");
		label_line.setBounds(14, 152, 233, 28);
		getContentPane().add(label_line);

		JLabel label = new JLabel("\u670D\u52A1\u7AEF\u5171\u4EAB\u7684\u6587\u4EF6");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label.setBounds(223, 216, 159, 18);
		getContentPane().add(label);

		/********** 文件展示栏 **********/
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 247, 554, 220);
		getContentPane().add(scrollPane);
		filelist = new JTextPane();
		filelist.setEditable(false);
		scrollPane.setViewportView(filelist);

		/********** 文件选择 、存储 **********/
		JLabel lblid = new JLabel("\u6587\u4EF6ID\uFF1A");
		lblid.setHorizontalAlignment(SwingConstants.CENTER);
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		lblid.setBounds(39, 492, 87, 28);
		getContentPane().add(lblid);

		tf_id = new JTextField();
		tf_id.setHorizontalAlignment(SwingConstants.LEFT);
		tf_id.setColumns(10);
		tf_id.setBounds(126, 492, 108, 28);
		getContentPane().add(tf_id);

		JLabel label_1 = new JLabel("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_1.setBounds(267, 492, 94, 28);
		getContentPane().add(label_1);

		tf_savedir = new JTextField();
		tf_savedir.setHorizontalAlignment(SwingConstants.LEFT);
		tf_savedir.setColumns(10);
		tf_savedir.setBounds(368, 492, 200, 28);
		getContentPane().add(tf_savedir);

		/********** 文件下载栏 **********/
		btn_download = new JButton("\u4E0B \u8F7D");
		btn_download.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btn_download.setBounds(14, 555, 99, 31);
		getContentPane().add(btn_download);
		pr = new JProgressBar(0,100);
		pr.setBounds(126, 556, 291, 30);
		getContentPane().add(pr);
		
		JLabel label_download_satus = new JLabel("\u8FDE\u63A5\u72B6\u6001\uFF1A");
		label_download_satus.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_download_satus.setBounds(421, 557, 81, 27);
		getContentPane().add(label_download_satus);
		
		isdownload = new JLabel("\u672A\u4E0B\u8F7D");
		isdownload.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		isdownload.setBounds(501, 558, 81, 24);
		getContentPane().add(isdownload);

		// 为连接按钮添加监听事件
		btn_connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("连接按钮被点击");
				String ip = null;
				int port = 0;
				// 检验输入信息
				if (!tf_ip.getText().trim().equals("") && !tf_port.getText().trim().equals("")) {
					ip = tf_ip.getText().trim();
					port = Integer.parseInt(tf_port.getText().trim());
					boolean isconnect = true;
					try {
						// 连接服务器
						cc = new ClientCore(ip, port);
						cc.connect();
						isTimeToCheckDownload = true;
					} catch (UnknownHostException e1) {
						isconnect = false;
					} catch (IOException e1) {
						isconnect = false;
					}
					if (isconnect) {
						label_isconnect.setText("连接成功！");
						label_isconnect.setForeground(Color.GREEN);
						// 连接成功后将服务器传输过来的内容展示在客户端上面
						filesName = cc.getFilesName();
//						System.out.println("客户端传输过来的内容:" + filesName);
						filelist.setText("客户端传输过来的内容:\n" + filesName);

					} else {
						JOptionPane.showMessageDialog(null, "IP地址或端口号错误", "连接错误", JOptionPane.ERROR_MESSAGE);
						label_isconnect.setText("连接失败！");
						label_isconnect.setForeground(new Color(255, 0, 0));
					}
				} else {
					JOptionPane.showMessageDialog(null, "请输入IP地址或端口号", "连接错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// 为下载按钮添加事件监听
		btn_download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("下载按钮被点击");
				//检验选择的指令是否存在，路径是否正确
				int comm = 0;
				String dir = null;
				//首先检验时候有输入id和路径
				if(!"".equals(tf_id.getText().trim()) && !"".equals(tf_savedir.getText().trim())){
					comm = Integer.parseInt(tf_id.getText().trim());
					dir = tf_savedir.getText().trim();
					//将验证工作交给类ClientCore处理
					if(tf_id.getText().trim().matches("^\\d*$")){
						//如果通過CilentCore的验证，则下载该文件,下载文件逻辑交给ClientCore
						if(cc.sendComm(comm,dir)){
							checkDoloadStatus();
						}else{
							//否则输入的指令不对
							JOptionPane.showMessageDialog(null, "请输入正确指令", "下载错误", JOptionPane.ERROR_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(null, "请输入正确指令", "下载错误", JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null, "请输入下载指令或下载路径", "下载错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	@Override
	public void run() {
		checkDoloadStatus();
	}
	
	public void checkDoloadStatus(){
		
		switch (cc.getDownloadStatus()) {
		case -1:
			isdownload.setText("下载失败");
			isdownload.setForeground(Color.RED);
			break;
		case 0:
			isdownload.setText("正在下载");
			isdownload.setForeground(Color.ORANGE);
			break;
		case 1:
			isdownload.setText("下载完成");
			isdownload.setForeground(Color.GREEN);
			break;
		case 2:
			isdownload.setText("未下载");
			break;
		}
	}
	
	public boolean isTimeToCheckDownload() {
		return isTimeToCheckDownload;
	}

	/**
	 * 启动程序
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame = new ClientFrame();
					frame.setResizable(false);
					frame.setVisible(true);
					
//					if(new ClientFrame().isTimeToCheckDownload){
//						Thread check_download_status = new Thread(new ClientFrame());
//						check_download_status.start();
//						}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
