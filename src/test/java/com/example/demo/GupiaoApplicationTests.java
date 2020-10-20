package com.example.demo;

import java.util.Collections;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GupiaoApplicationTests {

	@Test
	void contextLoads() {
	}

		  public static void main(String[] args) {  
		    LinkedList<String> lList = new LinkedList<String>();  
		    lList.add("1");  
		    lList.add("2");  
		    lList.add("3");  
		    lList.add("4");  
		    lList.add("5");  
		    System.out.println("链表的第一个元素是 : " + lList.getFirst());  
		    System.out.println("链表最后一个元素是 : " + lList.getLast());
		    lList.forEach(n ->{System.out.print(n+", ");});
		    
		    Collections.reverse(lList);
		    for(String value:lList) {
		    	System.out.println("value : " + value );
		    }
		  }  
}
