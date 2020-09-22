package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebContent {
	private String pageUrl;// 定义需要操作的网页地址
	private String pageEncode = "gbk";// 定义需要操作的网页的编码

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPageEncode() {
		return pageEncode;
	}

	public void setPageEncode(String pageEncode) {
		this.pageEncode = pageEncode;
	}

	// 定义取源码的方法
	public String getPageSource() {
		StringBuffer sb = new StringBuffer();
		try {
			// 构建一URL对象
			URL url = new URL(pageUrl);
			// 使用openStream得到一输入流并由此构造一个BufferedReader对象
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), pageEncode));
			String line;
			// 读取www资源
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
		} catch (Exception ex) {
			System.err.println(ex+"==>"+pageUrl);
		}
		return sb.toString();
	}

	// 定义一个把HTML标签删除过的源码的方法
	public String getPageSourceWithoutHtml() {
		final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
		String htmlStr = getPageSource();// 获取未处理过的源码
//		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
//		Matcher m_script = p_script.matcher(htmlStr);
//		htmlStr = m_script.replaceAll(""); // 过滤script标签
//		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
//		Matcher m_style = p_style.matcher(htmlStr);
//		htmlStr = m_style.replaceAll(""); // 过滤style标签
//		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
//		Matcher m_html = p_html.matcher(htmlStr);
//		htmlStr = m_html.replaceAll(""); // 过滤html标签
//		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
//		Matcher m_space = p_space.matcher(htmlStr);
//		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
//		htmlStr = htmlStr.trim(); // 返回文本字符串
//		htmlStr = htmlStr.replaceAll(" ", "");
//		htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		return htmlStr;
	}

	public static String getOneHtml(String urlString) throws MalformedURLException, IOException {
		InputStreamReader in = new InputStreamReader(new URL(urlString).openStream(), "UTF-8");
		StringBuilder input = new StringBuilder();
		int ch;
		while ((ch = in.read()) != -1)
			input.append((char) ch);
		// System.out.println("----"+input);
		return input.toString();
	}

	public List<String> getLink(String s) {
		String regex;
		List<String> list = new ArrayList<String>();
		regex = "<a href=\"http://www.edu.cn/(.*)\">\\w+</a>";

		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}
}
