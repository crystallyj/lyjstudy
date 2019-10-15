package com.njits.bigdata.jar;

public class TypeUtil {

	/**  
	  * 判断一个类是否为基本数据类型。  
	  * @param clazz 要判断的类。  
	  * @return true 表示为基本数据类型。  
	  */ 
	 public static boolean isBaseDataType(String typeName)
	 {   
	     return 
	     (   
	    		 typeName.equals("int")||
	    		 typeName.equals("java.lang.String")||
	    		 typeName.equals("long")||
	    		 typeName.equals("short")||
	    		 typeName.equals("double")||
	    		 typeName.equals("float")||
	    		 typeName.equals("char")||
	    		 typeName.equals("boolean")||
	    		 typeName.equals("java.util.Date")||
	    		 typeName.equals("java.lang.Integer")||
	    		 typeName.equals("java.lang.Long")||
	    		 typeName.equals("java.lang.Short")||
	    		 typeName.equals("java.lang.Double")||
	    		 typeName.equals("java.lang.Float")||
	    		 typeName.equals("java.lang.Boolean")
	     );   
	 }
}
