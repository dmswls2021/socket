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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Client extends JFrame{	
	JTextArea textArea;
	JTextField Msg;
	JButton send;
	JLabel nick;
	JTextField nickname;
	JButton change;
	JLabel cliip;
	JTextField cliiptx;
	JLabel joinip;
	JTextField joiniptx;
	JLabel joinport;
	JTextField joinporttx;
	JButton join;
	
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;	
	
	public static String clinick;
	public static int clientport;
	public static String clientip;

	public Client() {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			clientip = ip.getHostAddress();
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
				clinick = nickname.getText();
				System.out.print(clinick);
			}
		});
		cliip = new JLabel("   내 IP |   ");
		cliiptx = new JTextField(clientip);
		cliiptx.setEditable(false);
		JPanel IPPanel = new JPanel();
		IPPanel.setLayout(new BorderLayout());
		IPPanel.add(cliip, BorderLayout.WEST);
		IPPanel.add(cliiptx, BorderLayout.CENTER);
		nickPanel.add(nick, BorderLayout.WEST);
		nickPanel.add(nickname, BorderLayout.CENTER);
		nickPanel.add(change, BorderLayout.EAST);
		nickPanel.add(IPPanel, BorderLayout.SOUTH);
		add(nickPanel, BorderLayout.NORTH);
		
		textArea = new JTextArea();		
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);

		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());
		Msg = new JTextField();
		send = new JButton("   보내기   ");
		msgPanel.add(Msg, BorderLayout.CENTER);
		msgPanel.add(send, BorderLayout.EAST);
		
		send.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		JPanel inforjoinPanel = new JPanel();
		inforjoinPanel.setLayout(new BorderLayout());
		JPanel inforip = new JPanel();
		inforip.setLayout(new BorderLayout());
		JPanel inforport = new JPanel();
		inforport.setLayout(new BorderLayout());
		JPanel joinPanel = new JPanel();
		joinPanel.setLayout(new BorderLayout());
		joinip = new JLabel("   참가 채팅방 IP |   ");
		joiniptx = new JTextField();
		joinport = new JLabel("   참가 채팅방 PORT |   ");
		joinporttx = new JTextField();
		join = new JButton("   참가   ");
		join.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clientip = joiniptx.getText();
				clientport = Integer.parseInt(joinporttx.getText());
				
				CliThread cliThread = new CliThread();
				cliThread.setDaemon(true);
				cliThread.start();
			}
		});
		inforip.add(joinip,BorderLayout.WEST);
		inforip.add(joiniptx, BorderLayout.CENTER);
		inforport.add(joinport,BorderLayout.WEST);
		inforport.add(joinporttx, BorderLayout.CENTER);
		inforjoinPanel.add(inforip, BorderLayout.NORTH);
		inforjoinPanel.add(inforport, BorderLayout.SOUTH);
		joinPanel.add(inforjoinPanel, BorderLayout.CENTER);
		joinPanel.add(join, BorderLayout.EAST);
		joinPanel.add(msgPanel, BorderLayout.NORTH);
		add(joinPanel, BorderLayout.SOUTH);
		
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
				socket = new Socket(Server.serverip, clientport);
				textArea.append("서버에 접속됐습니다.\n");

				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);	
				
				while(true) {
					String msg = dis.readUTF();
					textArea.append(" [" + Server.sernick + "] : " + msg + "\n");
					textArea.setCaretPosition(textArea.getText().length());
				}
			} catch (UnknownHostException e) {
				textArea.append("주소가 이상합니다.\n");
			} catch (IOException e) {
				textArea.append("연결이 끊겼습니다.\n");
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