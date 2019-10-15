package com.njits.bigdata.jar;

//自定义一个类加载器
public class DynamicClassLoader extends ClassLoader {
	
	public Class<?> defineClassByName(byte[] b) throws Exception {
		return defineClass(null, b, 0, b.length);
	}
}