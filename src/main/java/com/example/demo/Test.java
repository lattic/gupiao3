package com.example.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

public class Test {
	
	public static void main(String args[]) {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(30, 50, 50, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(10000));

			
			
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						for(int i=1;i<=99999;i++) {
							System.out.println("readSh:"+i);
							readUrl(i,"sh6");
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			});
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						for(int i=1;i<=99999;i++) {
							System.out.println("readSh:"+i);
							readUrl(i,"sz0");
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			});
			pool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						for(int i=1;i<=99999;i++) {
							System.out.println("readSh:"+i);
							readUrl(i,"sz3");
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}
			});
	}

	public static void readUrl(final int i,String title) {
		WebContent url = new WebContent();
		String number = String.format("%05d", i);
		url.setPageUrl("http://hq.sinajs.cn/list="+title + number);
		String code = url.getPageSourceWithoutHtml();
		hanldeData(title+number, code);
	}
	

	private static void hanldeData(String number, String code) {
		if(code.length()>30) {
			String value = code.split("=")[1];
			System.out.println(value);
			String[] date = value.split(",");
			if(date.length<32) {
				return ;
			}
			GuPiao gp=new GuPiao(number,date[0],date[1],date[2],date[3],date[4],
								 date[5], date[6],date[7],date[8],date[9],
								 date[10],date[11],date[12],date[13],date[14],
								 date[15], date[16],date[17],date[18],date[19],
								 date[20],date[21],date[22],date[23],date[24],
								 date[25], date[26],date[27],date[28],date[29],
								 date[30],date[31]
					);
			System.out.println(gp.toString());
			System.out.println();
			
			try {
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
				File file = new File("C:\\Users\\king\\Desktop\\股票_"+ft.format(new Date())+".txt");
				if(!file.exists()){
					file.createNewFile();
				}
				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fileWriter);
				String context=JSON.toJSONString(gp);
				bw.write(context);
				bw.newLine();
				bw.flush();
				bw.close();
			}catch(Exception ex) {
				
			}
		}
	}
	
	
	
}
