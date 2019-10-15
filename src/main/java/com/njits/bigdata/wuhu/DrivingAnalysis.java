package com.njits.bigdata.wuhu;

public class DrivingAnalysis {

	public AckBean bigDataCalculate(int int1, String str1, int int2, String str2) {
		DriverService server = new DriverService();
		server.test();
		System.out.println("int1="+int1+", "+"str1="+str1+", "+"int2="+int2+", "+"str2="+str2);
		System.out.println("DrivingAnalysis bigDataCalculate 方法执行成功");
		AckBean ack = new AckBean();
		ack.setAck1(int1);
		ack.setAck2(str1);
		ack.setAck3(int2);
		ack.setAck4(str2);
		return ack;
	}
	
	public String bigDataCalculate2(int int1, String str1, int int2, String str2) {
		DriverService server = new DriverService();
		server.test();
		System.out.println("int1="+int1+", "+"str1="+str1+", "+"int2="+int2+", "+"str2="+str2);
		return "DrivingAnalysis bigDataCalculate2 方法执行成功";
	}
	
	public String bigDataCalculate3() {
		DriverService server = new DriverService();
		server.test();
		return "DrivingAnalysis bigDataCalculate3 方法执行成功";
	}
	
	public void bigDataCalculate4(int int1, String str1, int int2, String str2) {
		DriverService server = new DriverService();
		server.test();
		System.out.println("int1="+int1+", "+"str1="+str1+", "+"int2="+int2+", "+"str2="+str2);
	}
}
