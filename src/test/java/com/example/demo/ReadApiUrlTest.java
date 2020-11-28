package com.example.demo;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.ai.MockDeal;
import com.example.service.GuPiaoService;
import com.example.service.task.UpdateRealTimeTask;
import com.example.uitls.ReadApiUrl;
import com.example.uitls.RedisUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GupiaoApplication.class })
public class ReadApiUrlTest {
	@Autowired
	private ReadApiUrl readApiUrl;
	@Autowired
	private GuPiaoService guPiaoService;
	@Resource
	private RedisUtil redisUtil;
	
	
	private String number="sh688366";
	
	
	public void readRealTimeUrl() {
		readApiUrl.readRealTimeUrl(number);
	}
	@Test
	public void updateRealTime() {
		new UpdateRealTimeTask(guPiaoService,number,readApiUrl,redisUtil).run();
	}
}
