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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
	JTextArea textArea; //멤버 참조변수
	JTextField Msg;
	JButton send;
	JLabel nick;
	JTextField nickname;
	JButton change;
	JLabel openip;
	JTextField openiptx;
	JLabel openport;
	JTextField openporttx;
	JButton open;

	ServerSocket serverSocket;
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	
	public static String sernick;
	public static int serverport = 10001;
	public static String serverip;
	
	public Server() {		
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			serverip = ip.getHostAddress();
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		
		

		setTitle("서버/채팅방 생성");
		setSize(550,700);
		
		JPanel nickPanel = new JPanel();
		nickPanel.setLayout(new BorderLayout());
		nick = new JLabel("   닉네임 |   ");
		nickname = new JTextField();
		change = new JButton("   변경   ");
		change.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sernick = nickname.getText();
				System.out.print(sernick);
			}
		});
		nickPanel.add(nick, BorderLayout.WEST);
		nickPanel.add(nickname, BorderLayout.CENTER);
		nickPanel.add(change, BorderLayout.EAST);
		add(nickPanel, BorderLayout.NORTH);
		
		textArea = new JTextArea();		
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);

		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());

		Msg = new JTextField();
		send = new JButton("보내기");
		msgPanel.add(Msg, BorderLayout.CENTER);
		msgPanel.add(send, BorderLayout.EAST);

		send.addActionListener(new ActionListener() {			

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		JPanel inforopenPanel = new JPanel();
		inforopenPanel.setLayout(new BorderLayout());
		JPanel inforip = new JPanel();
		inforip.setLayout(new BorderLayout());
		JPanel inforport = new JPanel();
		inforport.setLayout(new BorderLayout());
		JPanel openPanel = new JPanel();
		openPanel.setLayout(new BorderLayout());
		openip = new JLabel("   채팅방 IP |   ");
		openiptx = new JTextField(serverip);
		openiptx.setEditable(false);
		openport = new JLabel("   채팅방 PORT |   ");
		openporttx = new JTextField(Integer.toString(serverport));
		open = new JButton("   열기   ");
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				serverip = openiptx.getText();
				serverport = Integer.parseInt(openporttx.getText());
				
				ServerThread serverThread = new ServerThread();
				serverThread.setDaemon(true);
				serverThread.start();
			}
		});
		inforip.add(openip,BorderLayout.WEST);
		inforip.add(openiptx, BorderLayout.CENTER);
		inforport.add(openport,BorderLayout.WEST);
		inforport.add(openporttx, BorderLayout.CENTER);
		inforopenPanel.add(inforip, BorderLayout.NORTH);
		inforopenPanel.add(inforport, BorderLayout.SOUTH);
		openPanel.add(inforopenPanel, BorderLayout.CENTER);
		openPanel.add(open, BorderLayout.EAST);
		openPanel.add(msgPanel, BorderLayout.NORTH);
		add(openPanel, BorderLayout.SOUTH);

		setVisible(true);
		Msg.requestFocus();

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
				serverSocket = new ServerSocket(serverport);
				textArea.append("채팅방이 열렸습니다.\n");
				textArea.append("상대방의 접속을 기다립니다.\n");				
				socket = serverSocket.accept();
				textArea.append(Client.clinick + "님이 접속하셨습니다.\n");

				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

				while(true) {
					String msg = dis.readUTF();
					textArea.append(" ["+ Client.clinick +"] : " + msg + "\n");
					textArea.setCaretPosition(textArea.getText().length());
				}
			} catch (IOException e) {
				textArea.append("참가자가 나갔습니다.\n");
			}
		}
	}
	
	public void sendMessage() {	
		String msg = Msg.getText();
		Msg.setText("");
		textArea.append(" [" + sernick + "] : " + msg + "\n");
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