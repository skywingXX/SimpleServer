package com.xx;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;


public class TestServer {
	

	private ServerSocket ss;
	
	private InputStreamReader inputStreamReader; 
	
	private BufferedReader bufferedReader;
	
	public void server(){

		try {
			//让服务器端程序开始监听来自4242端口的客户端请求
			if (ss==null) {
				 ss = new ServerSocket(4242);
				 System.out.println("服务器启动...");
			}
			
			//服务器无穷的循环等待客户端的请求
			while(true){	
			/*
			 *accept()方法会在等待用户的socket连接时闲置着，当用户链接
			 *上来时，此方法会返回一个socket(在不同的端口上)以便与客户端
			 *通信。Socket与ServerSocket的端口不同，因此ServerSocket可以
			 *空闲出来等待其他客户端
			 */ 
			//这个方法会停下来等待要求到达之后再继续
			Socket s = ss.accept();
			String text = "";
			
			inputStreamReader = new InputStreamReader(s.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			//new InputStreamReader(inputStreamReader,"UTF-8")
			String request = bufferedReader.readLine();
			// summary转换
			//List<String> sentenceList = HanLP.extractSummary(request, 2);
			//拼音转换
			List<Pinyin> pinyinList = HanLP.convertToPinyinList(request);
			for (Pinyin pinyin : pinyinList)
	        {
	        	if(pinyin.getPinyinWithToneMark().equals("none")) {
	        		text = text + pinyin;
	        		continue;
	        	}
	        		
	        	text= text +pinyin.getPinyinWithToneMark()+" ";
	        }
			
			
			System.out.println("接收到了客户端的请求:"+request);
			PrintWriter printWriter = new PrintWriter(s.getOutputStream());
			
			String advice = text.toString();
			
			
			printWriter.println(advice);
			printWriter.close();
			text = "";
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		TestServer server = new TestServer();
		server.server();
	}
	
	
}


