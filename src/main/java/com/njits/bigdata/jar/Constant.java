package com.njits.bigdata.jar;

import java.util.HashMap;
import java.util.Map;

public class Constant {

	public final static Map<Integer, String> jarResultMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -1656692776298251378L;
		{
			put(1, "jar文件加载成功");
			put(0, "jar文件加载异常");
			put(-1, "jar文件不存在");
		}
	};
	
	public final static Map<Integer, String> paramResultMap = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -1656692776298251378L;
		{
			put(0, "成功获取method的参数信息");
			put(-1, "jar包中未发现对应的class");
			put(-2, "class中未发现对应的method");
			put(-3, "class中未发现任何有效的method");
			put(-4, "该method无有效的入参");
			put(-5, "该method无有效的出参");
		}
	};
}
