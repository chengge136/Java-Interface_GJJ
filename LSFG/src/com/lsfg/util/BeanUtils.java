package com.lsfg.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

public class BeanUtils {

	private static final Log log = LogFactory.getLog(BeanUtils.class);

    public static Class<?> getPropertyType(String fieldName, Class<?> cls) {
        try {
            PropertyDescriptor desc = new PropertyDescriptor(fieldName, cls);
            return desc.getPropertyType();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void setPropertyValue(Object source, String fieldName, Object value) {
        try {
            PropertyDescriptor desc = new PropertyDescriptor(fieldName, source.getClass());
            desc.getWriteMethod().invoke(source, value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ��һ���ַ���ת����Date���ͣ��ܹ���������
     * 1������ yyyy-MM-dd HH:mm:ss
     * 2������ yyyy-MM-dd
     * @param value
     * @return
     */
    private static Date convertToDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            return sdf.parse(value);
        } catch (ParseException ex) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return sdf.parse(value);
            } catch (ParseException ex1) {
            	
            }
        }
        return null;
    }

    /**
     * �������ֻ�Ǹ���Ʒ������ת���õģ���Ϊ
     * ��Ʒ������ֻ��String,BigDecimal,Integer,Date������
     * @param value  Ҫת����ֵ
     * @param type   Ҫת��������
     * @return       ת���õ�ֵ
     */
    public static Object convertToObject(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (type == Integer.class) {
            return Integer.valueOf(value);
        } else if (type == Date.class) {
            return convertToDate(value);
        }
        return value;
    }

    public static Object getPropertyValue(Object source, String fieldName) {
        try {
            PropertyDescriptor desc = new PropertyDescriptor(fieldName, source.getClass());
            return desc.getReadMethod().invoke(source);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(BeanUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void addAttribute(Map<String, Object> map, String startWith, Method method, Object obj) {
        String methodName = method.getName();
        if (methodName.startsWith(startWith) && methodName.length() > startWith.length()) {
            StringBuilder sb = new StringBuilder(methodName.substring(startWith.length()));
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            try {
                Object result = method.invoke(obj);
                if (result != null) {
                    map.put(sb.toString(), result);
                }
            } catch (Exception ex) {
                log.error(null, ex);
            }
        }
    }

    public static Map<String, Object> convertObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Method[] ms = obj.getClass().getDeclaredMethods();
        for (Method m : ms) {
            if ((m.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
                addAttribute(map, "get", m, obj);
                addAttribute(map, "is", m, obj);
            }

        }
        return map;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        assert false : "cannot create new instance for: " + clazz;
        return null;
    }

    public static void copyProperties(Object source, Object target, String... ignoredProperties) {
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, ignoredProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deepCopyProperties(Object target, Object resource) {
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] srcDescriptors = propertyUtils.getPropertyDescriptors(resource);
        for (PropertyDescriptor origDescriptor : srcDescriptors) {
            String name = origDescriptor.getName();
            if ("class".equals(name)) {
                continue; // No point in trying to set an object's class
            }
            if (propertyUtils.isReadable(resource, name) && propertyUtils.isWriteable(target, name)) {
                try {
                    Object value = propertyUtils.getSimpleProperty(resource, name);
                    propertyUtils.setProperty(target, name, deepClone(value));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T object) {
        if (object == null) {
            return null;
        }
        if (object.getClass().isPrimitive() || object instanceof String) {
            return object;
        }
        XStream xstream = new XStream(new Dom4JDriver());
        String xml = xstream.toXML(object);
        return (T) xstream.fromXML(xml);
    }

    public static Object newInstance(String clazz) {
        try {
            return newInstance(BeanUtils.class.getClassLoader().loadClass(clazz));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        assert false : "cannot load the class: " + clazz;
        return null;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 != null) {
            return object1.equals(object2);
        }
        return false;
    }

    //for example  class name:"com.base.class"  file name: "file.xml"
    //return "/com/base/file.xml"
    public static String getURLFromClassName(String className, String fileName) {
        className = className.replaceAll("\\.", "/");
        String filePath = className.substring(0, className.lastIndexOf("/") + 1) + fileName;
        return "/" + filePath;
    }
    public static String objToString(Object obj){
  		return null!=obj? obj.toString():null;
  	}
  	
  	public  static Long objToLong(Object obj){
  		return null!=obj? Long.valueOf(obj.toString()):null;
  	}
  	
  	public static Float objToFloat(Object obj){
  		if(null!=obj)
  			return Float.parseFloat(obj.toString());
  		return new Float(0F);
  	}
  	
  	public static BigDecimal objToBig(Object obj){
  		BigDecimal bigDecimal =new BigDecimal(0);
  		if(null!=obj)
  			bigDecimal= new BigDecimal(obj.toString());
  		return bigDecimal;
  	}
  	
	public static Date objToData(Object obj){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
		Date date=null;
		try {
			if(null!=obj)	
				date=format.parse(obj.toString());
        } catch (ParseException ex) {
        	format = new SimpleDateFormat("yyyy-MM-dd");
            try {
            	date=format.parse(obj.toString());
            } catch (ParseException ex1) {
            	ex1.printStackTrace();
            }
        }
		return date;
	}
	
	public static Boolean objToBoolean(Object obj){
		if(null!=obj){
			if("T".equals(obj.toString().trim())){
				return true;
			}
		}
		return false;
	}
}
