package netcase;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import static mylib.Print.*;

public class MyServer {
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static List<Socket> soList;
	static {
		soList = new ArrayList<Socket>();
	}
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
			ServerSocket so = new ServerSocket(80001);
			while (true) {
				Socket s = so.accept();
				soList.add(s);
				print(s.getInetAddress().getHostAddress() + " login", true);
				new Thread(new SocketThread(s, soList)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}


class SocketThread implements Runnable{
	private Socket soHandle;
	private List<Socket> soList;
	private String remoteHost;
	private BufferedReader br;
	SocketThread(Socket s, List<Socket> soList) 
	throws IOException
	{
		soHandle = s;
		remoteHost = s.getInetAddress().getHostAddress();
		this.soList = soList;
		br = new BufferedReader(new InputStreamReader(soHandle.getInputStream()));
	}
	
	public void run() {
		try {
			
			
			String temp = "";
			while ((temp = this.readFromBuffer())!=null) {
				for (Socket s : soList) {
					if (s == soHandle) {
						continue;
					}
					PrintStream ps = new PrintStream(s.getOutputStream());
					ps.println(remoteHost + ": " + temp);
				}
			}
			soList.remove(soHandle);
//			soHandle.close();
			print(remoteHost + " logout", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String readFromBuffer() {
		try {
			return br.readLine();
		} catch(IOException e) {
			soList.remove(soHandle);
		}
		return null;
	}
}