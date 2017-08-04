package com.asen.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/**
 * 客户端界面 用户输入服务器ip地址和端口号并进行连接 连接成功后选择下载的文件进行下载
 * 
 * @author Asen
 */
public class ServerFrame extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = -8586211375465435563L;

	private JTextField tf_ip;
	private JTextField tf_port;
	private JTextPane filelist;
	private JLabel label_ip;
	private String floder;//要共享的文件夹
	private int port; //设置的端口号
	private ServerSocket ss;//打开网络通道
	private boolean isStop = false; //是否停止服务
	private int connectNum;//连接数
//	private Server s = new Server(socket, floder);
	
	// 设置文本框距离左边的基本距离
	private Insets baseMargin = new Insets(0, 5, 0, 0);

	/**
	 * 初始化窗体
	 */
	public ServerFrame() {
		initFrame();// 设置主窗体属性
		setAppTitle();// 设置程序title
		initShowfiles();// 初始化 文件展示栏
		initIpandPort();// 初始化 IP、端口栏
		
	}

	public void startService(){
		Socket socket = null;
		try {
			this.ss = new ServerSocket();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 当进度条值改变时执行
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (int) evt.getNewValue();
		}
	}

	//添加按钮事件
	@Override
	public void actionPerformed(ActionEvent evt) {
		String s = evt.getActionCommand();
		/**
		 *  当用户点击连接按钮时执行
		 */
		if ("connect".equals(s)) {
			// 检验输入信息
		
			/**
			 * 当用户点击下载按钮时执行
			 */
		} else if ("download".equals(s)) { 
		
		}
	}

	// 初始化窗体
	public void initFrame() {
		setTitle("文件共享服务端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));
	}

	// 设置下横须标题
	public void setAppTitle() {
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		JLabel Title_label = new JLabel("文件共享服务端");
		Title_label.setHorizontalAlignment(SwingConstants.CENTER);
		Title_label.setFont(new Font("微软雅黑", Font.PLAIN, 26));
		Title_panel.add(Title_label);
		getContentPane().add(Title_panel);
	}

	/********** IP、端口栏 **********/
	public void initIpandPort() {
		// ip标签
		JLabel label_ip = new JLabel("\u670D\u52A1\u5668IP\uFF1A");
		label_ip.setHorizontalAlignment(SwingConstants.CENTER);
		label_ip.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_ip.setBounds(28, 90, 94, 28);
		getContentPane().add(label_ip);
		// ip输入框
		tf_ip = new JTextField();
		tf_ip.setFont(new Font("宋体", Font.PLAIN, 16));
		tf_ip.setHorizontalAlignment(SwingConstants.LEFT);
		tf_ip.setBounds(126, 90, 216, 28);
		tf_ip.setMargin(baseMargin);
		tf_ip.setText("192.168.46.51");
		label_ip = new JLabel();
		getContentPane().add(tf_ip);
		// 端口标签
		JLabel label_port = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		label_port.setHorizontalAlignment(SwingConstants.CENTER);
		label_port.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_port.setBounds(356, 90, 87, 28);
		getContentPane().add(label_port);
		// 端口输入框
		tf_port = new JTextField();
		tf_port.setFont(new Font("宋体", Font.PLAIN, 16));
		tf_port.setHorizontalAlignment(SwingConstants.LEFT);
		tf_port.setColumns(10);
		tf_port.setBounds(433, 90, 108, 32);
		tf_port.setMargin(baseMargin);
		tf_port.setText("2345");
		getContentPane().add(tf_port);
	}

	

	public void initShowfiles() {
		// 设置标题
		JLabel label = new JLabel("\u670D\u52A1\u7AEF\u5171\u4EAB\u7684\u6587\u4EF6");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label.setBounds(223, 216, 159, 18);
		getContentPane().add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 247, 565, 220);
		getContentPane().add(scrollPane);
		filelist = new JTextPane();
		filelist.setFont(new Font("宋体", Font.PLAIN, 16));
		filelist.setEditable(false);
		filelist.setMargin(baseMargin);
		scrollPane.setViewportView(filelist);

	}

	// 下载错误提示框
	public void downloadErr(String errMsg) {
		JOptionPane.showMessageDialog(null, errMsg, "下载错误", JOptionPane.ERROR_MESSAGE);
	}

	// 连接错误提示框
	public void connectErr(JLabel label_isconnect, String errMsg) {
		JOptionPane.showMessageDialog(null, errMsg, "连接错误", JOptionPane.ERROR_MESSAGE);
		label_isconnect.setText("连接失败！");
		label_isconnect.setForeground(new Color(255, 0, 0));
	}

	public static void createAndShowGUI() {
		ServerFrame frame = new ServerFrame();
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	/* 启动程序 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
