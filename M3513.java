

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class M3513 extends JFrame{
	String serverNick;
	String clientNick;
	
	public M3513() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("m3513 소켓프로그램");
		
		Container c = getContentPane();
		c.setLayout(null);
		
		JLabel intro = new JLabel("소켓프로그램");
		JLabel server = new JLabel("SERVER");
		JLabel client = new JLabel("CLIENT");
		
		intro.setLocation(225, 70);
		intro.setSize(100, 30);
		c.add(intro);
		
		server.setLocation(240, 170);
		server.setSize(100,20);
		c.add(server);
		
		client.setLocation(245, 340);
		client.setSize(100,20);
		c.add(client);
		
		JButton create = new JButton("새로운 채팅방 생성");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SerFrame();
				dispose();
			}
		});
		create.setLocation(165, 220);
		create.setSize(200,50);
		c.add(create);
		
		
		JButton join = new JButton("채팅방 참가");
		join.setLocation(165, 390);
		join.setSize(200,50);
		c.add(join);
		
		
		this.setSize(550,700);
		this.setVisible(true);
	}
	
	public void SerFrame() {
		JFrame f = new JFrame("서버/채팅방 생성");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = f.getContentPane();
		c.setLayout(null); 
		
		JLabel nick = new JLabel("닉네임");
		nick.setBounds(100, 50, 100, 20);
		c.add(nick);
		
		JTextField nickname = new JTextField();
		nickname.setBounds(170, 50, 200, 20);
		c.add(nickname);
		
		JButton change = new JButton("변경");
		change.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				serverNick = nickname.getText();
				System.out.println(serverNick);
			}
		});
		change.setBounds(390, 50, 80, 20);
		c.add(change);
		
		
		f.setSize(550,700);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		M3513 f = new M3513();
	}
}