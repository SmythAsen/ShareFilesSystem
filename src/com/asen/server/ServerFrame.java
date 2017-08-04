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
 * �ͻ��˽��� �û����������ip��ַ�Ͷ˿ںŲ��������� ���ӳɹ���ѡ�����ص��ļ���������
 * 
 * @author Asen
 */
public class ServerFrame extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = -8586211375465435563L;

	private JTextField tf_ip;
	private JTextField tf_port;
	private JTextPane filelist;
	private JLabel label_ip;
	private String floder;//Ҫ������ļ���
	private int port; //���õĶ˿ں�
	private ServerSocket ss;//������ͨ��
	private boolean isStop = false; //�Ƿ�ֹͣ����
	private int connectNum;//������
//	private Server s = new Server(socket, floder);
	
	// �����ı��������ߵĻ�������
	private Insets baseMargin = new Insets(0, 5, 0, 0);

	/**
	 * ��ʼ������
	 */
	public ServerFrame() {
		initFrame();// ��������������
		setAppTitle();// ���ó���title
		initShowfiles();// ��ʼ�� �ļ�չʾ��
		initIpandPort();// ��ʼ�� IP���˿���
		
	}

	public void startService(){
		Socket socket = null;
		try {
			this.ss = new ServerSocket();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ��������ֵ�ı�ʱִ��
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (int) evt.getNewValue();
		}
	}

	//��Ӱ�ť�¼�
	@Override
	public void actionPerformed(ActionEvent evt) {
		String s = evt.getActionCommand();
		/**
		 *  ���û�������Ӱ�ťʱִ��
		 */
		if ("connect".equals(s)) {
			// ����������Ϣ
		
			/**
			 * ���û�������ذ�ťʱִ��
			 */
		} else if ("download".equals(s)) { 
		
		}
	}

	// ��ʼ������
	public void initFrame() {
		setTitle("�ļ���������");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));
	}

	// �����º������
	public void setAppTitle() {
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		JLabel Title_label = new JLabel("�ļ���������");
		Title_label.setHorizontalAlignment(SwingConstants.CENTER);
		Title_label.setFont(new Font("΢���ź�", Font.PLAIN, 26));
		Title_panel.add(Title_label);
		getContentPane().add(Title_panel);
	}

	/********** IP���˿��� **********/
	public void initIpandPort() {
		// ip��ǩ
		JLabel label_ip = new JLabel("\u670D\u52A1\u5668IP\uFF1A");
		label_ip.setHorizontalAlignment(SwingConstants.CENTER);
		label_ip.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_ip.setBounds(28, 90, 94, 28);
		getContentPane().add(label_ip);
		// ip�����
		tf_ip = new JTextField();
		tf_ip.setFont(new Font("����", Font.PLAIN, 16));
		tf_ip.setHorizontalAlignment(SwingConstants.LEFT);
		tf_ip.setBounds(126, 90, 216, 28);
		tf_ip.setMargin(baseMargin);
		tf_ip.setText("192.168.46.51");
		label_ip = new JLabel();
		getContentPane().add(tf_ip);
		// �˿ڱ�ǩ
		JLabel label_port = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		label_port.setHorizontalAlignment(SwingConstants.CENTER);
		label_port.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_port.setBounds(356, 90, 87, 28);
		getContentPane().add(label_port);
		// �˿������
		tf_port = new JTextField();
		tf_port.setFont(new Font("����", Font.PLAIN, 16));
		tf_port.setHorizontalAlignment(SwingConstants.LEFT);
		tf_port.setColumns(10);
		tf_port.setBounds(433, 90, 108, 32);
		tf_port.setMargin(baseMargin);
		tf_port.setText("2345");
		getContentPane().add(tf_port);
	}

	

	public void initShowfiles() {
		// ���ñ���
		JLabel label = new JLabel("\u670D\u52A1\u7AEF\u5171\u4EAB\u7684\u6587\u4EF6");
		label.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label.setBounds(223, 216, 159, 18);
		getContentPane().add(label);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 247, 565, 220);
		getContentPane().add(scrollPane);
		filelist = new JTextPane();
		filelist.setFont(new Font("����", Font.PLAIN, 16));
		filelist.setEditable(false);
		filelist.setMargin(baseMargin);
		scrollPane.setViewportView(filelist);

	}

	// ���ش�����ʾ��
	public void downloadErr(String errMsg) {
		JOptionPane.showMessageDialog(null, errMsg, "���ش���", JOptionPane.ERROR_MESSAGE);
	}

	// ���Ӵ�����ʾ��
	public void connectErr(JLabel label_isconnect, String errMsg) {
		JOptionPane.showMessageDialog(null, errMsg, "���Ӵ���", JOptionPane.ERROR_MESSAGE);
		label_isconnect.setText("����ʧ�ܣ�");
		label_isconnect.setForeground(new Color(255, 0, 0));
	}

	public static void createAndShowGUI() {
		ServerFrame frame = new ServerFrame();
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
	}

	/* �������� */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
