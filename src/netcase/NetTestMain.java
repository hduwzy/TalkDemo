package netcase;

import static mylib.Print.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NetTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int min, max, stepr, step;
		min = 1;
		max = 255;
		step = 30;
		stepr = min;
		
		final List<Integer> l = new ArrayList<Integer>();
		l.add(min);
		
		while (stepr <= max) {
			if (stepr % step == 0 || stepr == max) {
				l.add(stepr);
			}
			stepr ++;
		}
		
		
		int size = l.size();
		for (int idx = 1; idx < size; idx++) {
			new Thread(new MyRunnable(l.get(idx - 1), l.get(idx)){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					IpGenerator ipg = new IpGenerator("h", this.start, this.end);
					while (ipg.hasNext()) {
						InetAddress ip = ipg.next();
						try {
							if (ip.isReachable(1000)) {
								print(ip.getHostAddress(), true);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
	
	abstract static class MyRunnable implements Runnable {
		final int start, end;
		
		MyRunnable(int min, int max) {
			start = min;
			end = max;
		}
	}
}




class NetTest {
	public static void test() {
		String local = "";
		try {
			InetAddress ip = InetAddress.getByName("xiaohuo1.local");
			print(ip.isReachable(5000));
			print(ip.getHostAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class IpGenerator implements Iterator<InetAddress>, Iterable{
	private String hostName;
	
	private int min, max, cur;
	
	public IpGenerator(String h, int min, int max) {
		hostName = h;
		this.min = min;
		this.max = max;
		cur = min;
	}
	
	
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return cur <= max;
	}

	@Override
	public InetAddress next() {
		// TODO Auto-generated method stub
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(this.hostName + cur);
			cur ++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return this;
	}
}