package com.asen.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

/**
 * 客户端界面
 * 用户输入服务器ip地址和端口号并进行连接
 * 连接成功后选择下载的文件进行下载
 * @author Asen
 */
public class ClientFrame extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = -8586211375465435563L;

	private JTextField tf_ip; // 需要连接的服务器ip地址
	private JTextField tf_port; // 需要连接的服务器接口
	private JTextField tf_id; // 向服务器发送的下载指令id
	private JTextField tf_savedir; // 保存文件的路径
	private JButton btn_download; // 下载按钮
	private JButton btn_connect; // 连接按钮
	private JLabel label_isconnect; // 连接状态
	private String filesName; // 服务器传输过来的文件
	private JTextPane filelist; // 展示服务器传输过来的文件
	private ClientCore cc; // 客服端逻辑处理类
	private static JLabel isdownload; // 下载状态
	private JProgressBar pr; // 进度条
	private static String ip; // 已经连接的服务器IP地址
	private static int port; // 已经连接的服务器端口号
	private DownloadTask task; // 下载任务更新UI进度条
	private boolean isconnected = false;// 打开连接开关
	// 设置文本框距离左边的基本距离
	private Insets baseMargin = new Insets(0, 5, 0, 0);

	// 当进度条值改变时执行
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (int) evt.getNewValue();
			pr.setValue(progress);
		}
	}

	// 当用户点击下载按钮时执行
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (isconnected) {
			// 检验选择的指令是否存在，路径是否正确
			int comm = 0;
			String dir = null;
			// 首先检验时候有输入id和路径
			if (!"".equals(tf_id.getText().trim()) && !"".equals(tf_savedir.getText().trim())) {
				// 将验证工作交给类ClientCore处理
				if (tf_id.getText().trim().matches("^\\d*$")) { // 判断文件id是否为数字
					comm = Integer.parseInt(tf_id.getText().trim());
					dir = tf_savedir.getText().trim();
					// 如果通過CilentCore的验证，则下载该文件,下载文件逻辑交给ClientCore
					if (cc.sendComm(comm, dir)) { // 判断指令是否正确
						try {
							cc.startDownload();
						} catch (IOException e) {
							// TODO 处理下载错误逻辑...
							e.printStackTrace();
						}
						// 监控下载进度，此处应该判断，当文件下载遇到错误是如何处理//TODO
						btn_download.setEnabled(false);
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						task = new DownloadTask(ClientFrame.this, cc, btn_download, pr, isdownload);
						task.addPropertyChangeListener(this);
						task.execute();
					}
				} else {
					downloadErr("请输入正确指令");
				}
			} else {
				downloadErr("请输入下载指令或下载路径");
			}
		} else {
			downloadErr("请先连接服务器");
		}
	}

	/**
	 * 初始化窗体
	 */
	public ClientFrame() {
		// 设置主窗体属性
		setTitle("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));

		// 设置程序title
		setAppTitle();
		// 初始化 IP、端口栏
		initIpandPort();
		// 初始化 连接栏
		initConnect();
		// 初始化 文件展示栏
		initShowfiles();
		// 初始化 文件选择 、存储
		initSelectandDown();
		// 初始化 文件下载栏
		initSlectDownloadFile();
		// 为连接按钮添加监听事件
		connectListener();
	}

	// 连接服务器
	public void connectListener() {
		btn_connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// 检验输入信息
				if (!tf_ip.getText().trim().equals("") && !tf_port.getText().trim().equals("")) { // 保证输入框有内容
					if (tf_port.getText().trim().matches("^\\d*$")) {// 判断端口输入框内容是否为数字
						ip = tf_ip.getText().trim(); // 将输入的ip地址传输到处理程序
						port = Integer.parseInt(tf_port.getText().trim());// 将输入的接口传入到处理程序
						try {
							// 连接服务器
							cc = new ClientCore(ip, port);
							cc.connect();
							isconnected = true;
						} catch (IOException e) {
							isconnected = false;
						}
					}
					if (isconnected) {
						label_isconnect.setText("连接成功！");
						label_isconnect.setForeground(Color.GREEN);
						// 连接成功后将服务器传输过来的内容展示在客户端上面
						filesName = cc.getFilesName();// 获取文件列表
						filelist.setText("服务器文件:\n" + filesName);// 展示文件列表
					} else {
						connectErr(label_isconnect, "IP地址或端口号错误！");
					}
				} else {
					connectErr(label_isconnect, "请输入IP地址或端口号！");
				}
			}
		});

	}

	// 设置下横须标题
	public void setAppTitle() {
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		JLabel Title_label = new JLabel("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
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

	// 连接栏
	public void initConnect() {
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

	public void initSelectandDown() {
		JLabel lblid = new JLabel("\u6587\u4EF6ID\uFF1A");
		lblid.setHorizontalAlignment(SwingConstants.CENTER);
		lblid.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		lblid.setBounds(39, 492, 87, 28);
		getContentPane().add(lblid);

		tf_id = new JTextField();
		tf_id.setFont(new Font("宋体", Font.PLAIN, 16));
		tf_id.setHorizontalAlignment(SwingConstants.LEFT);
		tf_id.setColumns(10);
		tf_id.setBounds(126, 492, 108, 28);
		tf_id.setMargin(baseMargin);
		getContentPane().add(tf_id);

		JLabel label_1 = new JLabel("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_1.setBounds(267, 492, 94, 28);
		getContentPane().add(label_1);

		tf_savedir = new JTextField();
		tf_savedir.setFont(new Font("宋体", Font.PLAIN, 16));
		tf_savedir.setHorizontalAlignment(SwingConstants.LEFT);
		tf_savedir.setColumns(10);
		tf_savedir.setBounds(368, 492, 200, 28);
		tf_savedir.setMargin(baseMargin);
		tf_savedir.setText("d:\\test");
		getContentPane().add(tf_savedir);
	}

	public void initSlectDownloadFile() {

		btn_download = new JButton("\u4E0B \u8F7D");
		btn_download.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btn_download.setBounds(14, 555, 99, 31);
		btn_download.addActionListener(this);
		getContentPane().add(btn_download);
		// 进度条
		pr = new JProgressBar();
		pr.setBounds(126, 556, 291, 30);
		pr.setStringPainted(true);// 设置提示信息
		pr.setString("当前没有下载任务");
		pr.setForeground(new Color(0, 165, 0));
		getContentPane().add(pr);

		JLabel label_download_satus = new JLabel("\u4E0B\u8F7D\u72B6\u6001\uFF1A");
		label_download_satus.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_download_satus.setBounds(421, 557, 81, 27);
		getContentPane().add(label_download_satus);

		isdownload = new JLabel("\u672A\u4E0B\u8F7D");
		isdownload.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		isdownload.setBounds(501, 558, 81, 24);
		getContentPane().add(isdownload);
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
		ClientFrame frame = new ClientFrame();
		frame.setResizable(false);
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
