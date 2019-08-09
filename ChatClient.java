package chatting.project;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7747814883930610676L;
	private TextArea ta = new TextArea();
	private TextField tf = new TextField();
	private Button b1 = new Button("Send");
	private Button b2 = new Button("Exit");
	private Panel p = new Panel();
	private Panel p2 = new Panel();
	private Socket s = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private String name;
	
	public ChatClient() {
//		createGUI();
		createUserName();
		this.setVisible(true);
		this.setSize(500, 300);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void createUserName() {
		TextField tf2 = new TextField();
		Button b3 = new Button("Login");
		p2.add(tf2);
		p2.add(b3);
		add(p2, "Center");
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = tf2.getText();
				createGUI();
			}			
		});
	}
	
	
	public void createGUI() {
		removeAll();

		add(ta, "West");
		add(p, "Center");
		add(tf, "South");
		p.add(b1);
		p.add(b2);
		tf.addActionListener(this);
		
		
		invalidate();
		validate();
	}

	public void go(String ip, int port) {
		try {
			s = new Socket(ip, port);
			System.out.println("success : Connection!");
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			ta.append("스트림 생성 성공\n");
			
			new ChatClientThread().start();
		} catch (Exception e) {
		
		} 
	}

	public void actionPerformed(ActionEvent ae) {
		String msg = tf.getText();
		try {
			oos.writeObject("[" + name + "]" + msg);
		} catch(IOException e) {
			System.out.println("메세지 전송중 오류발생 : " + e.getMessage());
			System.exit(0);
		}
		tf.setText("");
	}

	class ChatClientThread extends Thread {
		public void run() {
			try {
				while(true) {
					String msg = (String)ois.readObject();
					ta.append(msg + "\n");
				}
			} catch(Exception e) {
				ta.append("readObject()시 오류 발생 : " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		new ChatClient().go("223.194.145.68", 5500);
	}
}
