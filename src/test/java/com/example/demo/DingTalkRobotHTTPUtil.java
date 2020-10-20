package com.example.demo;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
public class DingTalkRobotHTTPUtil {
		final static String APP_SECRET="477b77570a86de89c4c3a43a662e498d4262e7382ea0b0332563d88c93adc3fc";
		static boolean isTest=false;
		    
		    
		    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 30, 60, TimeUnit.SECONDS , new ArrayBlockingQueue<Runnable>(10), new DingTalkThreadFactory());

		    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=";

		    public static void main(String[] args) throws Exception {
		    	Date now=new Date();
		    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String content = MessageFormat.format("GS【买入提示】"+dateformat.format(now)+"\n------------------------------------ \n股票代码：{0}\n预警价位：{1}\n建议仓位：{2}\n当前金额：{3}\n策略规则", 
		        		                                                                  new Object[] {"SH600305", "21.39", "10%","22.51", "突破策略趋势"});
		        
		        String robotbuy = MessageFormat.format("GS【自动买入】"+dateformat.format(now)+"\n------------------------------------ \n股票代码：{0}\n买入价位：{1}\n数量：{2}\n当前余额：{3}", 
                        new Object[] {"SH600305", "22.51", "1000",(100000-(22.51*1000))});
		        
		        DingTalkRobotHTTPUtil.sendMsg(DingTalkRobotHTTPUtil.APP_SECRET, robotbuy, null, false);
		       
		    }
		    
		    /**
		     * 发送钉钉机器人通知
		     * 
		     * @param accessToken 机器人的accessToken
		     * @param content 通知内容
		     * @param notifyList 通知手机号集合
		     * @param isAtAll 是否@所有人
		     * @return
		     */
		    public static void sendMsg(String accessToken, String content, List<String> notifyList, Boolean isAtAll) throws Exception {
		        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(content)) {
		            String errorText = MessageFormat.format("parameter accessToken:{0} or content:{1} is null", accessToken, content);
		            throw new RuntimeException(errorText);
		        }
		        isTest=true;
		        run(accessToken, content, notifyList, isAtAll);
		    };

		    public static void run(final String accessToken, final String content, final List<String> notifyList, final Boolean isAtAll) throws Exception {

		        threadPoolExecutor.execute(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                    HttpClient httpclient = HttpClients.createDefault();
		    
		                    HttpPost httpPost = new HttpPost(WEBHOOK_TOKEN.concat(accessToken));
		                    httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		                    
		                    JSONObject textObj = new JSONObject();
		                    textObj.put("content", content);
		                    
		                    JSONObject notifyObj = new JSONObject();
		                    notifyObj.put("isAtAll", isAtAll);
		                    notifyObj.put("atMobiles", notifyList);
		    
		                    JSONObject messagegObj = new JSONObject();
		                    messagegObj.put("msgtype", "text");
		                    messagegObj.put("text", textObj);
		                    messagegObj.put("at", notifyObj);
		    
		                    StringEntity se = new StringEntity(messagegObj.toJSONString(), "utf-8");
		                    httpPost.setEntity(se);
		                    HttpResponse response = httpclient.execute(httpPost);
		    
		                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		                        EntityUtils.toString(response.getEntity());
		                    }
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }finally {
							if(isTest) {
								 System.exit(0);
							}
						}
		            }
		        });

		    }

		    public static class DingTalkThreadFactory implements ThreadFactory {

		        private AtomicInteger count = new AtomicInteger(0);
		        private String threadName = "ding-talk-thread-";

		        @Override
		        public Thread newThread(Runnable r) {
		            Thread t = new Thread(r);
		            String threadNameWithNum = threadName + count.addAndGet(1);
		            t.setName(threadNameWithNum);
		            return t;
		        }
		    }
		    
		   

}
