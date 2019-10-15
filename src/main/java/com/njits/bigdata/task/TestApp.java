package com.njits.bigdata.task;

public class TestApp {
	public static void main(String[] args) throws Exception {
		//模拟定时job，每10秒执行一次
		for(int i=0; i<100; i++) {
			BigDataThread thread = new BigDataThread();
			thread.run();
			Thread.sleep(10000);
		}
	}
}
