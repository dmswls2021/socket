import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
	JTextArea textArea; //��� ��������
	JTextField Msg;
	JButton send;

	ServerSocket serverSocket;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	
	public Server() {		
		setTitle("����/ä�ù� ����");
		setSize(550,700);
		
		textArea = new JTextArea();		
		textArea.setEditable(false); //���� ����
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);

		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());

		Msg = new JTextField();
		send = new JButton("������");
		msgPanel.add(Msg, BorderLayout.CENTER);
		msgPanel.add(send, BorderLayout.EAST);
		add(msgPanel,BorderLayout.SOUTH);

		send.addActionListener(new ActionListener() {			

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});

		setVisible(true);
		Msg.requestFocus();

		ServerThread serverThread = new ServerThread();
		serverThread.setDaemon(true);
		serverThread.start();

		addWindowListener(new WindowAdapter() {			
			
			@Override
			public void windowClosing(WindowEvent e) {				
				super.windowClosing(e);

				try {
					if(dos != null) dos.close();
					if(dis != null) dis.close();
					if(socket != null) socket.close();
					if(serverSocket != null) serverSocket.close();
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
			}			
		});
	}

	public class ServerThread extends Thread {

		@Override
		public void run() {			
			try {
				serverSocket = new ServerSocket(10001);
				textArea.append("ä�ù��� ���Ƚ��ϴ�.\n");
				textArea.append("������ ������ ��ٸ��ϴ�.\n");				
				socket = serverSocket.accept();
				textArea.append(socket.getInetAddress().getHostAddress() + "���� �����ϼ̽��ϴ�.\n");

				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

				while(true) {
					String msg = dis.readUTF();
					textArea.append(" ["+ clinick +"] : " + msg + "\n");
					textArea.setCaretPosition(textArea.getText().length());
				}
			} catch (IOException e) {
				textArea.append("�����ڰ� �������ϴ�.\n");
			}
		}
	}
	
	public void sendMessage() {	
		String msg = Msg.getText();
		Msg.setText("");
		textArea.append(" [" + sernick + " : " + msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());

		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};		
		t.start();
	}	
}