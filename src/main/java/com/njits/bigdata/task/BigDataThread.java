package com.njits.bigdata.task;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.njits.bigdata.jar.Constant;
import com.njits.bigdata.jar.ManageClassLoader;
import com.njits.bigdata.jar.ParamInfo;
import com.njits.bigdata.jar.TypeUtil;

/**
 * 模拟大数据任务执行线程
 * @author zhaojj
 *
 */
public class BigDataThread extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(BigDataThread.class);
	
	ManageClassLoader classLoader = new ManageClassLoader();
	String jarDir = "/Users/liyanjie/tempdata/testjar/";  //jar包的存放路径
	String jarName = "advert-2.0.jar";  //第三方jar名称
	String className = "com.njits.iot.advert.test.ModelCalculateBean"; //类包路径名称
	String methodName = "generateBean";//需要运行的方法名（如果该类中有多个同名方法则取第一个）

	public void run() {
		try {
			int ret = classLoader.loadJar(jarDir, jarName);
			logger.info("加载jar结果：{}", Constant.jarResultMap.get(ret));
			//jar加载成功向下执行
			if(ret==1) {
				ret = this.readMethodParam();
				if(ret == 1) {
					this.executeJarMethod(ManageClassLoader.clsMap.get(className), methodName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int readMethodParam() {
		int ret = -1;
		try {
			ParamInfo paramInfo = classLoader.readParams(className, methodName);
			ret = paramInfo.getRet();
			if (ret != 1) {
				logger.warn(Constant.paramResultMap.get(paramInfo.getRet()));
			} else {
				logger.info("### {} 的出参类型：{}", methodName, paramInfo.getOutParamTypeNames().toString());
				logger.info("### {} 的出参名称：{}", methodName, paramInfo.getOutParamFieldNames().toString());
				logger.info("### {} 的入参类型：{}", methodName, paramInfo.getInParamTypeNames().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 模拟输入数据源的参数执行方法
	 * @param cls
	 * @param methodName
	 * @throws Exception
	 */
	private void executeJarMethod(Class<?> cls, String methodName) throws Exception {
		if(cls==null) {
			return;
		}
		//模拟输入数据源的4个参数
		int int1 = 1;
		int int2 = 2;
		String str1 = "aa";
		String str2 = "bb";
		
		//模拟输出数据源的3个参数
		List<Object> rets = new ArrayList<Object>();
		
		Method[] methods = cls.getMethods();
		for(Method method : methods) {
			if(methodName.equals(method.getName())) {
				Object ret = method.invoke(cls.newInstance(), int1, str1, int2, str2);
				if(TypeUtil.isBaseDataType(ret.getClass().getTypeName())) {
					rets.add(ret);
				} else {
					//得到所有属性
					Field[] fields = ret.getClass().getDeclaredFields();
					for (Field field : fields){//遍历
						//打开私有访问
						field.setAccessible(true);
						//获取属性值
						rets.add(field.get(ret));
					}
				}
			}
		}
		for(Object obj : rets) {
			logger.info("返回值：{}", obj);
		}
	}
}
