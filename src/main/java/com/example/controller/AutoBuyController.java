package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AutoBuyController {
	
	private static  boolean isDemo=false;
	
	@RequestMapping("/getAction")
	@ResponseBody
	public String getAction(@RequestParam(value="name")String name,@RequestParam(value="ts")String ts) {
		if(!isDemo)
			return name+"_0.01_100";
		return "";
	}
	
	@RequestMapping("/done")
	@ResponseBody
	public String done(@RequestParam(value="name")String name,@RequestParam(value="ts")String ts) {
		isDemo = true;
		System.out.println(name);
		return "";
	}
}