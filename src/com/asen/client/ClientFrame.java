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
 * �ͻ��˽���
 * �û����������ip��ַ�Ͷ˿ںŲ���������
 * ���ӳɹ���ѡ�����ص��ļ���������
 * @author Asen
 */
public class ClientFrame extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = -8586211375465435563L;

	private JTextField tf_ip; // ��Ҫ���ӵķ�����ip��ַ
	private JTextField tf_port; // ��Ҫ���ӵķ������ӿ�
	private JTextField tf_id; // ����������͵�����ָ��id
	private JTextField tf_savedir; // �����ļ���·��
	private JButton btn_download; // ���ذ�ť
	private JButton btn_connect; // ���Ӱ�ť
	private JLabel label_isconnect; // ����״̬
	private String filesName; // ����������������ļ�
	private JTextPane filelist; // չʾ����������������ļ�
	private ClientCore cc; // �ͷ����߼�������
	private static JLabel isdownload; // ����״̬
	private JProgressBar pr; // ������
	private static String ip; // �Ѿ����ӵķ�����IP��ַ
	private static int port; // �Ѿ����ӵķ������˿ں�
	private DownloadTask task; // �����������UI������
	private boolean isconnected = false;// �����ӿ���
	// �����ı��������ߵĻ�������
	private Insets baseMargin = new Insets(0, 5, 0, 0);

	// ��������ֵ�ı�ʱִ��
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (int) evt.getNewValue();
			pr.setValue(progress);
		}
	}

	// ���û�������ذ�ťʱִ��
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (isconnected) {
			// ����ѡ���ָ���Ƿ���ڣ�·���Ƿ���ȷ
			int comm = 0;
			String dir = null;
			// ���ȼ���ʱ��������id��·��
			if (!"".equals(tf_id.getText().trim()) && !"".equals(tf_savedir.getText().trim())) {
				// ����֤����������ClientCore����
				if (tf_id.getText().trim().matches("^\\d*$")) { // �ж��ļ�id�Ƿ�Ϊ����
					comm = Integer.parseInt(tf_id.getText().trim());
					dir = tf_savedir.getText().trim();
					// ���ͨ�^CilentCore����֤�������ظ��ļ�,�����ļ��߼�����ClientCore
					if (cc.sendComm(comm, dir)) { // �ж�ָ���Ƿ���ȷ
						try {
							cc.startDownload();
						} catch (IOException e) {
							// TODO �������ش����߼�...
							e.printStackTrace();
						}
						// ������ؽ��ȣ��˴�Ӧ���жϣ����ļ�����������������δ���//TODO
						btn_download.setEnabled(false);
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						task = new DownloadTask(ClientFrame.this, cc, btn_download, pr, isdownload);
						task.addPropertyChangeListener(this);
						task.execute();
					}
				} else {
					downloadErr("��������ȷָ��");
				}
			} else {
				downloadErr("����������ָ�������·��");
			}
		} else {
			downloadErr("�������ӷ�����");
		}
	}

	/**
	 * ��ʼ������
	 */
	public ClientFrame() {
		// ��������������
		setTitle("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 680);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(new Color(91, 155, 213));

		// ���ó���title
		setAppTitle();
		// ��ʼ�� IP���˿���
		initIpandPort();
		// ��ʼ�� ������
		initConnect();
		// ��ʼ�� �ļ�չʾ��
		initShowfiles();
		// ��ʼ�� �ļ�ѡ�� ���洢
		initSelectandDown();
		// ��ʼ�� �ļ�������
		initSlectDownloadFile();
		// Ϊ���Ӱ�ť��Ӽ����¼�
		connectListener();
	}

	// ���ӷ�����
	public void connectListener() {
		btn_connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// ����������Ϣ
				if (!tf_ip.getText().trim().equals("") && !tf_port.getText().trim().equals("")) { // ��֤�����������
					if (tf_port.getText().trim().matches("^\\d*$")) {// �ж϶˿�����������Ƿ�Ϊ����
						ip = tf_ip.getText().trim(); // �������ip��ַ���䵽�������
						port = Integer.parseInt(tf_port.getText().trim());// ������Ľӿڴ��뵽�������
						try {
							// ���ӷ�����
							cc = new ClientCore(ip, port);
							cc.connect();
							isconnected = true;
						} catch (IOException e) {
							isconnected = false;
						}
					}
					if (isconnected) {
						label_isconnect.setText("���ӳɹ���");
						label_isconnect.setForeground(Color.GREEN);
						// ���ӳɹ��󽫷������������������չʾ�ڿͻ�������
						filesName = cc.getFilesName();// ��ȡ�ļ��б�
						filelist.setText("�������ļ�:\n" + filesName);// չʾ�ļ��б�
					} else {
						connectErr(label_isconnect, "IP��ַ��˿ںŴ���");
					}
				} else {
					connectErr(label_isconnect, "������IP��ַ��˿ںţ�");
				}
			}
		});

	}

	// �����º������
	public void setAppTitle() {
		JPanel Title_panel = new JPanel();
		Title_panel.setBounds(0, 13, 582, 50);
		Title_panel.setBackground(new Color(91, 155, 213));
		JLabel Title_label = new JLabel("\u6587\u4EF6\u5171\u4EAB\u5BA2\u6237\u7AEF");
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

	// ������
	public void initConnect() {
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

	public void initSelectandDown() {
		JLabel lblid = new JLabel("\u6587\u4EF6ID\uFF1A");
		lblid.setHorizontalAlignment(SwingConstants.CENTER);
		lblid.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		lblid.setBounds(39, 492, 87, 28);
		getContentPane().add(lblid);

		tf_id = new JTextField();
		tf_id.setFont(new Font("����", Font.PLAIN, 16));
		tf_id.setHorizontalAlignment(SwingConstants.LEFT);
		tf_id.setColumns(10);
		tf_id.setBounds(126, 492, 108, 28);
		tf_id.setMargin(baseMargin);
		getContentPane().add(tf_id);

		JLabel label_1 = new JLabel("\u4FDD\u5B58\u8DEF\u5F84\uFF1A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("΢���ź�", Font.PLAIN, 18));
		label_1.setBounds(267, 492, 94, 28);
		getContentPane().add(label_1);

		tf_savedir = new JTextField();
		tf_savedir.setFont(new Font("����", Font.PLAIN, 16));
		tf_savedir.setHorizontalAlignment(SwingConstants.LEFT);
		tf_savedir.setColumns(10);
		tf_savedir.setBounds(368, 492, 200, 28);
		tf_savedir.setMargin(baseMargin);
		tf_savedir.setText("d:\\test");
		getContentPane().add(tf_savedir);
	}

	public void initSlectDownloadFile() {

		btn_download = new JButton("\u4E0B \u8F7D");
		btn_download.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		btn_download.setBounds(14, 555, 99, 31);
		btn_download.addActionListener(this);
		getContentPane().add(btn_download);
		// ������
		pr = new JProgressBar();
		pr.setBounds(126, 556, 291, 30);
		pr.setStringPainted(true);// ������ʾ��Ϣ
		pr.setString("��ǰû����������");
		pr.setForeground(new Color(0, 165, 0));
		getContentPane().add(pr);

		JLabel label_download_satus = new JLabel("\u4E0B\u8F7D\u72B6\u6001\uFF1A");
		label_download_satus.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		label_download_satus.setBounds(421, 557, 81, 27);
		getContentPane().add(label_download_satus);

		isdownload = new JLabel("\u672A\u4E0B\u8F7D");
		isdownload.setFont(new Font("΢���ź�", Font.PLAIN, 16));
		isdownload.setBounds(501, 558, 81, 24);
		getContentPane().add(isdownload);
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
		ClientFrame frame = new ClientFrame();
		frame.setResizable(false);
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
