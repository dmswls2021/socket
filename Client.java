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
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame{
	JTextArea textArea;
	JTextField Msg;
	JButton send;
	
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;	

	public Client() {
		setTitle("����/ä�ù� ����");
		setSize(550,700);
		
		textArea = new JTextArea();		
		textArea.setEditable(false);
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

		CliThread cliThread = new CliThread();
		cliThread.setDaemon(true);
		cliThread.start();

		addWindowListener(new WindowAdapter() {			
			@Override 
			public void windowClosing(WindowEvent e) {				
				super.windowClosing(e);
				try {
					if(dos != null) dos.close();
					if(dis != null) dis.close();
					if(socket != null) socket.close();
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
			}			
		});
	}

	public class CliThread extends Thread {

		@Override
		public void run() {
			try {
				socket = new Socket("  ���� IP �ּ�  ", 10001);
				textArea.append("������ ���ӵƽ��ϴ�.\n");

				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);	
				
				while(true) {
					String msg = dis.readUTF();
					textArea.append(" [SERVER] : " + msg + "\n");
					textArea.setCaretPosition(textArea.getText().length());
				}
			} catch (UnknownHostException e) {
				textArea.append("�ּҰ� �̻��մϴ�.\n");
			} catch (IOException e) {
				textArea.append("������ ������ϴ�.\n");
			}
		}
	}

	public void sendMessage() {	
			String msg = Msg.getText();
			Msg.setText("");
			textArea.append(" [" + clinick + "] : " + msg + "\n");
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