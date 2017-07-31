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
	private JTextField tf_ip; // ��Ҫ���ӵķ�����ip��ַ
	private JTextField tf_port; // ��Ҫ���ӵķ������ӿ�
	private JTextField tf_id;
	private JTextField tf_savedir; // �����ļ���·��
	private JButton btn_download;
	private JButton btn_connect;
	private JLabel label_isconnect;
	private String filesName; // ����������������ļ�
	private JTextPane filelist; // չʾ����������������ļ�
	private ClientCore cc;
	private JLabel isdownload;
	private JProgressBar pr;
	private boolean isTimeToCheckDownload = false;

	/**
	 * ��������
	 */
	public ClientFrame() {
		// ��������������
		setTitle("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));

		// ����title
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		getContentPane().add(Title_panel);
		JLabel Title_label = new JLabel("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		Title_label.setHorizontalAlignment(SwingConstants.CENTER);
		Title_label.setFont(new Font("΢���ź�", Font.PLAIN, 26));
		Title_panel.add(Title_label);

		/********** IP���˿��� **********/
		JLabel label_ip = new JLabel("\u670D\u52A1\u5668IP\uFF1A");
		label_ip.setHorizontalAlignment(SwingConstants.CENTER);
		label_ip.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_ip.setBounds(28, 90, 94, 28);
		getContentPane().add(label_ip);

		tf_ip = new JTextField();
		tf_ip.setHorizontalAlignment(SwingConstants.LEFT);
		tf_ip.setBounds(126, 90, 216, 28);
		getContentPane().add(tf_ip);
		tf_ip.setColumns(10);

		JLabel label_port = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		label_port.setHorizontalAlignment(SwingConstants.CENTER);
		label_port.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_port.setBounds(356, 90, 87, 28);
		getContentPane().add(label_port);

		tf_port = new JTextField();
		tf_port.setHorizontalAlignment(SwingConstants.LEFT);
		tf_port.setColumns(10);
		tf_port.setBounds(433, 90, 108, 32);
		getContentPane().add(tf_port);

		/********** ������ **********/
		btn_connect = new JButton("\u8FDE \u63A5");
		btn_connect.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		btn_connect.setBounds(243, 152, 99, 27);
		getContentPane().add(btn_connect);

		JLabel label_connect_status = new JLabel("\u8FDE\u63A5\u72B6\u6001\uFF1A");
		label_connect_status.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		label_connect_status.setBounds(379, 157, 87, 22);
		getContentPane().add(label_connect_status);

		label_isconnect = new JLabel("\u672A\u8FDE\u63A5");
		label_isconnect.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		label_isconnect.setBounds(460, 157, 87, 22);
		getContentPane().add(label_isconnect);

		JLabel label_line = new JLabel("------------------------------------------------------");
		label_line.setBounds(14, 152, 233, 28);
		getContentPane().add(label_line);

		JLabel label = new JLabel("\u670D\u52A1\u7AEF\u5171\u4EAB\u7684\u6587\u4EF6");
		label.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label.setBounds(223, 216, 159, 18);
		getContentPane().add(label);

		/********** �ļ�չʾ�� **********/
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 247, 554, 220);
		getContentPane().add(scrollPane);
		filelist = new JTextPane();
		filelist.setEditable(false);
		scrollPane.setViewportView(filelist);

		/********** �ļ�ѡ�� ���洢 **********/
		JLabel lblid = new JLabel("\u6587\u4EF6ID\uFF1A");
		lblid.setHorizontalAlignment(SwingConstants.CENTER);
		lblid.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		lblid.setBounds(39, 492, 87, 28);
		getContentPane().add(lblid);

		tf_id = new JTextField();
		tf_id.setHorizontalAlignment(SwingConstants.LEFT);
		tf_id.setColumns(10);
		tf_id.setBounds(126, 492, 108, 28);
		getContentPane().add(tf_id);

		JLabel label_1 = new JLabel("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_1.setBounds(267, 492, 94, 28);
		getContentPane().add(label_1);

		tf_savedir = new JTextField();
		tf_savedir.setHorizontalAlignment(SwingConstants.LEFT);
		tf_savedir.setColumns(10);
		tf_savedir.setBounds(368, 492, 200, 28);
		getContentPane().add(tf_savedir);

		/********** �ļ������� **********/
		btn_download = new JButton("\u4E0B \u8F7D");
		btn_download.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		btn_download.setBounds(14, 555, 99, 31);
		getContentPane().add(btn_download);
		pr = new JProgressBar(0,100);
		pr.setBounds(126, 556, 291, 30);
		getContentPane().add(pr);
		
		JLabel label_download_satus = new JLabel("\u8FDE\u63A5\u72B6\u6001\uFF1A");
		label_download_satus.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		label_download_satus.setBounds(421, 557, 81, 27);
		getContentPane().add(label_download_satus);
		
		isdownload = new JLabel("\u672A\u4E0B\u8F7D");
		isdownload.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		isdownload.setBounds(501, 558, 81, 24);
		getContentPane().add(isdownload);

		// Ϊ���Ӱ�ť��Ӽ����¼�
		btn_connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("���Ӱ�ť�����");
				String ip = null;
				int port = 0;
				// ����������Ϣ
				if (!tf_ip.getText().trim().equals("") && !tf_port.getText().trim().equals("")) {
					ip = tf_ip.getText().trim();
					port = Integer.parseInt(tf_port.getText().trim());
					boolean isconnect = true;
					try {
						// ���ӷ�����
						cc = new ClientCore(ip, port);
						cc.connect();
						isTimeToCheckDownload = true;
					} catch (UnknownHostException e1) {
						isconnect = false;
					} catch (IOException e1) {
						isconnect = false;
					}
					if (isconnect) {
						label_isconnect.setText("���ӳɹ���");
						label_isconnect.setForeground(Color.GREEN);
						// ���ӳɹ��󽫷������������������չʾ�ڿͻ�������
						filesName = cc.getFilesName();
//						System.out.println("�ͻ��˴������������:" + filesName);
						filelist.setText("�ͻ��˴������������:\n" + filesName);

					} else {
						JOptionPane.showMessageDialog(null, "IP��ַ��˿ںŴ���", "���Ӵ���", JOptionPane.ERROR_MESSAGE);
						label_isconnect.setText("����ʧ�ܣ�");
						label_isconnect.setForeground(new Color(255, 0, 0));
					}
				} else {
					JOptionPane.showMessageDialog(null, "������IP��ַ��˿ں�", "���Ӵ���", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Ϊ���ذ�ť����¼�����
		btn_download.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("���ذ�ť�����");
				//����ѡ���ָ���Ƿ���ڣ�·���Ƿ���ȷ
				int comm = 0;
				String dir = null;
				//���ȼ���ʱ��������id��·��
				if(!"".equals(tf_id.getText().trim()) && !"".equals(tf_savedir.getText().trim())){
					comm = Integer.parseInt(tf_id.getText().trim());
					dir = tf_savedir.getText().trim();
					//����֤����������ClientCore����
					if(tf_id.getText().trim().matches("^\\d*$")){
						//���ͨ�^CilentCore����֤�������ظ��ļ�,�����ļ��߼�����ClientCore
						if(cc.sendComm(comm,dir)){
							checkDoloadStatus();
						}else{
							//���������ָ���
							JOptionPane.showMessageDialog(null, "��������ȷָ��", "���ش���", JOptionPane.ERROR_MESSAGE);
						}
					}else{
						JOptionPane.showMessageDialog(null, "��������ȷָ��", "���ش���", JOptionPane.ERROR_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null, "����������ָ�������·��", "���ش���", JOptionPane.ERROR_MESSAGE);
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
			isdownload.setText("����ʧ��");
			isdownload.setForeground(Color.RED);
			break;
		case 0:
			isdownload.setText("��������");
			isdownload.setForeground(Color.ORANGE);
			break;
		case 1:
			isdownload.setText("�������");
			isdownload.setForeground(Color.GREEN);
			break;
		case 2:
			isdownload.setText("δ����");
			break;
		}
	}
	
	public boolean isTimeToCheckDownload() {
		return isTimeToCheckDownload;
	}

	/**
	 * ��������
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
