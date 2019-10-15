package com.njits.bigdata.jar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author zhaojj
 */
public class ManageClassLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(ManageClassLoader.class);
	
	public static Map<String, Long> modifyMap = new HashMap<String, Long>();
	DynamicClassLoader dc = new DynamicClassLoader();
	public static Map<String, Class<?>> clsMap = new HashMap<String, Class<?>>();
	
	public ParamInfo readParams(String className, String methodName) {
		ParamInfo paramInfo = new ParamInfo();
		Class<?> cls = clsMap.get(className);
		if(cls==null) {
			paramInfo.setRet(-1);
			return paramInfo;
		}
		Method[] methods = cls.getMethods();
		if(methods==null || methods.length == 0) {
			paramInfo.setRet(-3);
			return paramInfo;
		}
		int num = 0;
		for(Method method : methods) {
			if(methodName.equals(method.getName())) {
				num++;
				Type type = method.getGenericReturnType();
				Parameter[] params = method.getParameters();
				if(params==null || params.length == 0) {
					paramInfo.setRet(-4);
					return paramInfo;
				}
				List<String> inParamTypeNames = new ArrayList<String>();
				for(Parameter param : params) {
					inParamTypeNames.add(param.getType().getName());
				}
				
				List<String> outParamTypeNames = new ArrayList<String>();
				List<String> outParamFieldNames = new ArrayList<String>();
				if(TypeUtil.isBaseDataType(type.getTypeName())) {
					outParamTypeNames.add(type.getTypeName());
					outParamFieldNames.add("");
				} else {
					Class<?> outParamCls = clsMap.get(type.getTypeName());
					if(outParamCls==null) {
						paramInfo.setRet(-5);
						return paramInfo;
					}
					//得到所有属性
					Field[] fields = outParamCls.getDeclaredFields();
					for(Field field : fields) {
						outParamTypeNames.add(field.getType().getName());
						outParamFieldNames.add(field.getName());
					}
				}
				
				paramInfo.setOutParamTypeNames(outParamTypeNames);
				paramInfo.setOutParamFieldNames(outParamFieldNames);
				paramInfo.setInParamTypeNames(inParamTypeNames);
				break;
			}
		}
		if(num==0) {
			paramInfo.setRet(-2);
		} else {
			paramInfo.setRet(1);
		}
		return paramInfo;
	} 
	
	/**
	 * 加载jar包并根据class名称返回class对象，如果jar有更新则刷新class内容，否则不刷新
	 * @param jarDir 存放jar的目录
	 * @param jarName jar包全路径名称
	 * @return jar中class对象列表
	 * @param className class名称
	 * @return -1文件不存在 0失败 1成功
	 */
	public int loadJar(String jarDir, String jarName) {
		String clsName;
        try {
        	File file = new File(jarDir+jarName);
			if (!file.exists()) {
				return -1;
			}
			long lastModified = 0;
			if(modifyMap.get(jarName)!=null) {
				lastModified = modifyMap.get(jarName);
			}
			if(file.lastModified() > lastModified) {
				this.unZip(file, jarDir);
				List<File> classList = new ArrayList<File>();
				this.readClassFile(new File(jarDir), classList);
				for (File classFile : classList) {
					clsName = classFile.getAbsolutePath().replace("\\", "/").replace(jarDir, "").replace("/", ".");
					clsName = clsName.substring(0, clsName.indexOf(".class"));
					System.out.println("classname="+clsName);
					if(null==this.loadClass(classFile))
					{
						continue;
					}
					
					clsMap.put(clsName, this.loadClass(classFile));
				}
				modifyMap.put(jarName, file.lastModified());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
        return 1;
	}
	
	/**
	 * 递归读取这个目录下的class文件
	 * @param dir 要读取的根目录
	 * @param classList 存放class文件对象的列表
	 */
	private void readClassFile(File dir, List<File> classList) {
		System.out.println("list==="+classList);
		File[] files = dir.listFiles();
		if (files == null) {// 如果目录为空，直接退出  
            return;  
        } 
		for(File f:files) {
			//如果是文件，直接输出名字
			if(f.isFile() && f.getName().lastIndexOf(".class")!=-1) {
				classList.add(f);
			} else if(f.isDirectory()) {
				//如果是文件夹，递归调用
				readClassFile(f, classList);
			}
		}
	}

	/**
	 * 加载class文件，更新到jvm中
	 * @param classFile
	 * @return
	 */
	private Class<?> loadClass(File classFile) {
		System.out.println("path======"+classFile.getPath());
		InputStream input = null;
		ByteArrayOutputStream buffer = null;
		Class<?> cls = null;
		try {
			input = new FileInputStream(classFile);
			buffer = new ByteArrayOutputStream();
			int data;
			while ((data = input.read()) != -1) {
				buffer.write(data);
			}
			byte[] classData = buffer.toByteArray();
			cls = dc.defineClassByName(classData);
		} catch (Exception e) {
			System.out.println("ettttttttttt");
			logger.warn("{} 加载失败, e:{}", classFile.getPath(), e.getMessage());
			return null;
		} finally {
			System.out.println("bbbbbbbbb");
			try {
				if (buffer != null) {
					buffer.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cls;
	}
	
	/**
	 * zip解压  
	 * @param srcFile        zip源文件
	 * @param destDirPath     解压后的目标文件夹
	 * @throws RuntimeException 解压失败会抛出运行时异常
	 */
	private void unZip(File srcFile, String destDirPath) throws RuntimeException {
	    // 判断源文件是否存在
	    if (!srcFile.exists()) {
	        throw new RuntimeException(srcFile.getPath() + "所指文件不存在");
	    }
	    // 开始解压
	    ZipFile zipFile = null;
	    try {
	        zipFile = new ZipFile(srcFile);
	        Enumeration<?> entries = zipFile.entries();
	        while (entries.hasMoreElements()) {
	            ZipEntry entry = (ZipEntry) entries.nextElement();
	            // 如果是文件夹，就创建个文件夹
	            if (entry.isDirectory()) {
	                String dirPath = destDirPath + "/" + entry.getName();
	                File dir = new File(dirPath);
	                dir.mkdirs();
	            } else {
	                // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
	                File targetFile = new File(destDirPath + "/" + entry.getName());
	                // 保证这个文件的父文件夹必须要存在
	                if(!targetFile.getParentFile().exists()){
	                    targetFile.getParentFile().mkdirs();
	                }
	                targetFile.createNewFile();
	                // 将压缩文件内容写入到这个文件中
	                InputStream is = zipFile.getInputStream(entry);
	                FileOutputStream fos = new FileOutputStream(targetFile);
	                int len;
	                byte[] buf = new byte[1024];
	                while ((len = is.read(buf)) != -1) {
	                    fos.write(buf, 0, len);
	                }
	                // 关流顺序，先打开的后关闭
	                fos.close();
	                is.close();
	            }
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("解压失败", e);
	    } finally {
	        if(zipFile != null){
	            try {
	                zipFile.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}