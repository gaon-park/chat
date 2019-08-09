package chatting.project;

import java.io.*;
import java.net.*;

class User {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public User(Socket s, ObjectInputStream ois, ObjectOutputStream oos) {
		this.s = s;
		this.ois = ois;
		this.oos = oos;
	}
	
	public Socket getSocket() {	return s; }
	public ObjectOutputStream getObjectOutputStream() {	return oos; }
	public ObjectInputStream getObjectInputStream() {	return ois;	}
}
