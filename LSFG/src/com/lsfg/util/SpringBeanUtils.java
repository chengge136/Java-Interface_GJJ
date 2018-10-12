package com.lsfg.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

/**
 * 线程中无法使用注解注入对象，web容器启动之前不会注入线程中的对象，线程启动时web容器也无法感知
 * 从spring上下文直接获取spring容器中的对象，将线程的分发与实现分离。
 * @author lpeng.
 */
@Component
public class SpringBeanUtils {
	private static ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz, String beanName) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        if (context == null) {
            context = applicationContext;
        }
        return (T) context.getBean(beanName);
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
    	SpringBeanUtils.applicationContext = applicationContext;
    }
	
}
