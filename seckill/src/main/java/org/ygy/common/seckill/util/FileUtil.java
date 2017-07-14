package org.ygy.common.seckill.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class FileUtil {
	
	private static Logger       logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 默认编码
	 */
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
     * 加载多个Properties配置文件，同属性值以后加载配置文件为准
     * @param resourcesPaths
     * @return
     */
    public static Properties loadMultiProperties(String... resourcesPaths){
		Properties props = new Properties();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
		for (String location : resourcesPaths) {
			if (null == location || "".equals(location.trim())) {
				continue;
			}
			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
			} catch (IOException e) {
				logger.error("加载配置文件"+location+"失败", e);
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException e) {
						logger.error("流关闭异常", e);
					}
				}
			}
		}
		return props;
	}
}
