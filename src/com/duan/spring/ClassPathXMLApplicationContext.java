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
	
	//ע��ʹ�����ƣ�������������д��������֮ǰ
	
	public ClassPathXMLApplicationContext(String xmlpath) {
		@SuppressWarnings("deprecation")
		// 1.ʹ��JDOM����Ҫָ��ʹ��ʲô������
		SAXBuilder builder = new SAXBuilder(false);
		try {
			// 2.�õ�Document�������Ժ�Ҫ���е����в������Ƕ����Document������
			Document doc = builder.build(xmlpath);
			// 3.�õ���Ԫ��
			Element beans = doc.getRootElement();
			// 4.�õ�Ԫ�أ��ڵ㣩�ļ���
			List<?> beanlist = beans.getChildren("bean");
			// 5.����List
			for (Iterator<?> iter = beanlist.iterator(); iter.hasNext();) {
				Element bean = (Element) iter.next();
				// 6.ȡ��Ԫ�ص�����
				String key = bean.getAttributeValue("id");
				String classpath = bean.getAttributeValue("class");
				Class<?> c = Class.forName(classpath);
				// 7.ȡ��Ԫ�ص���Ԫ��List
				List<?> childbeanlist = bean.getChildren("param");
				if (childbeanlist != null) {
					Iterator<?> childIterator = childbeanlist.iterator();
					// 8.������Ԫ��List
					while (childIterator.hasNext()) {
						Element param = (Element) childIterator.next();
						String paramName = param.getAttributeValue("name");
						String paramValue = null;
						String paramRef = null;
						paramValue = param.getAttributeValue("value");
						paramRef = param.getAttributeValue("ref");						
						//������з���������
						Method setMethod = null;
						Method[] methods = c.getDeclaredMethods();
						//��������һ��ֵ���ҵ�set����
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
							//�ж�Map���Ƿ��Ѵ��ڣ��������������map��
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
						//��������һ�������ҵ�set����
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
							//�ж�Map���Ƿ��Ѵ��ڣ��������������map��
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
				//����������Ԫ�أ���û�в���ע�룬ֱ�Ӵ浽������
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
