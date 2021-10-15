package com.demo.cdmall1;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

import com.demo.cdmall1.web.interceptor.MyInterceptor;


// imageBoard 이미지 불러오는 경로 잡아주는 설정파일

@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Autowired
	MyInterceptor interceptor;
	
	@Value("${resource.path}")
	private String resourcePath;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(uploadPath)
				.addResourceLocations(resourcePath);
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor).addPathPatterns("/member/**").addPathPatterns("/admin/**");
		
	}
}
