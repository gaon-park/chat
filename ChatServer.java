package chatting.project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {
	private List<User> users = new ArrayList<User>();
	private int port = 5500;
	private ServerSocket ss;
	
	private ChatServer() {
		try{			
			ss = new ServerSocket(port);
			System.out.println("ServerSocket 생성 성공. port = " + port);
		} catch(IOException e) {
			
		}
	}
	
	public void go() {
		try {
			while(true) {
				Socket s = ss.accept();
				System.out.println("client connected");
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				User newUser = new User(s, ois, oos);
				users.add(newUser);
				new ChatServerThread(newUser).start();
			}
		} catch (IOException e) {
			
		}
		
	}
	public void broadcast(String msg) {
		for(int i = 0; i < users.size(); i++) {
			User u = users.get(i);
			ObjectOutputStream oos = u.getObjectOutputStream();
			try {
				oos.writeObject(msg);
			} catch(Exception e) {
				System.out.println("오류 발생 : " + e.getMessage());
			}
		}
	}
	public void removeClient(User user) {
		try {
			if(user.getObjectInputStream() != null) user.getObjectInputStream().close();
			if(user.getObjectOutputStream() != null) user.getObjectOutputStream().close();
			if(user.getSocket() != null) user.getSocket().close();
		} catch (IOException e) {
		}
	}
	
	class ChatServerThread extends Thread{
		private User user;
		
		public ChatServerThread(User user) {
			this.user = user;
		}
		
		public void run() {
			try {
				while(true) {
					ObjectInputStream ois = user.getObjectInputStream();
					String msg = (String)ois.readObject();
					broadcast(msg);
				}
			} catch(Exception e) {
				removeClient(user);
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatServer().go();
	}
}
