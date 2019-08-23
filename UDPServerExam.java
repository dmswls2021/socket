

import java.net.*;
import java.io.*;

public class UDPServerExam{
	public static void main(String[] args) throws Exception{
	  
	  DatagramSocket ds= new DatagramSocket(3000);
	  byte[] data=new byte[65508];
	  DatagramPacket dp= new DatagramPacket(data, data.length);
          System.out.println("UDP 서버 대기중");
	  ds.receive(dp);
	  String msg = new String(dp.getData()).trim();
	  System.out.println(" 보낸 주소  :"+dp.getAddress());
	  System.out.println(" 보낸메시지 : " + msg);
       }
}