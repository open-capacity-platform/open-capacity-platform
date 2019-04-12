package com.open.capacity.autoconfigure;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author owen
 * @create 2017年7月2日
 * 装配bean
 */
public class LoggingConfigurationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// TODO Auto-generated method stub
//		importingClassMetadata.getAllAnnotationAttributes(EnableEcho.class.getName());
		return new String[] { 
				"com.open.capacity.autoconfigure.datasource.DataSourceAspect",
				"com.open.capacity.autoconfigure.log.LogAnnotationAspect"
		};
	}

}
