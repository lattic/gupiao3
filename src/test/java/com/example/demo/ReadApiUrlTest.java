package com.example.demo;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.service.GuPiaoService;
import com.example.service.task.DataTask;
import com.example.service.task.RealTimeTask;
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
	@Autowired
	private DataTask  dataTask;
	@Autowired
	private RealTimeTask realTimeTask;
	
	
	private String number="sh688366";
	
	
	public void readRealTimeUrl() {
		readApiUrl.readRealTimeUrl(number);
	}

	public void updateRealTime() {
		new UpdateRealTimeTask(guPiaoService,number,readApiUrl,redisUtil).run();
	}
	
	@Test
	public void task() {
		try {
			realTimeTask.task1();
			realTimeTask.task2();
			realTimeTask.task3();
			realTimeTask.task4();
			realTimeTask.task5();
			dataTask.updateAllDayGuPiao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
