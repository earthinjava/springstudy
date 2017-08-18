package com.duan.spring;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ClassPathXMLApplicationContext implements BeanFactory {
	
	private Map<String, Object> beanMap = new HashMap<String, Object>();
	
	//注意使用限制：被引用类配置写在引用类之前
	
	public ClassPathXMLApplicationContext(String xmlpath) {
		@SuppressWarnings("deprecation")
		// 1.使用JDOM首先要指定使用什么解析器
		SAXBuilder builder = new SAXBuilder(false);
		try {
			// 2.得到Document，我们以后要进行的所有操作都是对这个Document操作的
			Document doc = builder.build(xmlpath);
			// 3.得到根元素
			Element beans = doc.getRootElement();
			// 4.得到元素（节点）的集合
			List<?> beanlist = beans.getChildren("bean");
			// 5.遍历List
			for (Iterator<?> iter = beanlist.iterator(); iter.hasNext();) {
				Element bean = (Element) iter.next();
				// 6.取得元素的属性
				String key = bean.getAttributeValue("id");
				String classpath = bean.getAttributeValue("class");
				Class<?> c = Class.forName(classpath);
				// 7.取得元素的子元素List
				List<?> childbeanlist = bean.getChildren("param");
				if (childbeanlist != null) {
					Iterator<?> childIterator = childbeanlist.iterator();
					// 8.遍历子元素List
					while (childIterator.hasNext()) {
						Element param = (Element) childIterator.next();
						String paramName = param.getAttributeValue("name");
						String paramValue = null;
						String paramRef = null;
						paramValue = param.getAttributeValue("value");
						paramRef = param.getAttributeValue("ref");						
						//获得所有方法的数组
						Method setMethod = null;
						Method[] methods = c.getDeclaredMethods();
						//若参数是一个值，找到set方法
						if (paramValue != null) {
							String methodName = "set"
									+ paramName.substring(0, 1).toUpperCase()
									+ paramName.substring(1);							
							for (Method method : methods) {
								String methodname1 = method.getName();
								if (methodname1.equals(methodName)) {
									setMethod = method;
								}
							}							
							//判断Map中是否已存在，将建及对象存入map中
							if (beanMap.containsKey(key)) {
								Object obj = beanMap.get(key);
								setMethod.invoke(obj, paramValue);
								beanMap.put(key, obj);
							} else {
								Object obj = c.newInstance();
								setMethod.invoke(obj, paramValue);
								beanMap.put(key, obj);
							}

						}
						//若参数是一个对象，找到set方法
						if (paramRef != null) {								
							String methodName = "set"
									+ paramName.substring(0, 1).toUpperCase()
									+ paramName.substring(1);							
							for (Method method : methods) {
								String methodname1 = method.getName();
								if (methodname1.equals(methodName)) {
									setMethod = method;
									System.out.println(setMethod);
								}
							}
							//判断Map中是否已存在，将建及对象存入map中
							if (beanMap.containsKey(key)) {
								Object obj = beanMap.get(key);								
								setMethod.invoke(obj, beanMap.get(paramRef));								
								beanMap.put(key, obj);
							} else {
								Object obj = c.newInstance();								
								setMethod.invoke(obj, beanMap.get(paramRef));
								beanMap.put(key, obj);
							}
						}
						
					}
				}
				//若不含有子元素，则没有参数注入，直接存到容器中
				if (!beanMap.containsKey(key)) {
					beanMap.put(key, c.newInstance());
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {		
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object getBean(String name) {
		return beanMap.get(name);
	}

}
