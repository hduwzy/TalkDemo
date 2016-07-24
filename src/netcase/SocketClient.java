package netcase;

import java.net.InetAddress;
import java.net.Socket;

import java.util.Scanner;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class SocketClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket("45.79.128.250", 80001);
			
			final Thread iThread = new Thread(new ClientInteract(s));
			Thread readThread = new Thread(new ClientReader(s){
				@SuppressWarnings("deprecation")
				public void run() {
					super.run();
					iThread.destroy();
				}
			});;
			
			readThread.start();
			iThread.start();
			
		} catch(IOException e) {
			
		}
	}
}

class ClientReader implements Runnable {
	private Socket soHandle;
	ClientReader(Socket s) {
		soHandle = s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(soHandle.getInputStream()));
			PrintStream ps = new PrintStream(System.out);
			String temp;
			while ((temp = br.readLine()) != null) {
				ps.println(temp);
			}
			soHandle.close();
		} catch (IOException e) {
			
		}
	}
}

class ClientInteract implements Runnable{
	private Socket soHandle;
	ClientInteract(Socket s) {
		soHandle = s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			PrintStream ps = new PrintStream(soHandle.getOutputStream());
			String temp;
			while ((temp = br.readLine()) != null) {
				if (temp.isEmpty()) {
					continue;
				}
				ps.println(temp);
			}
		} catch (IOException e) {
			
		}
	}
	
}