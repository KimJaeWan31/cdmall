package com.demo.cdmall1.util;

public interface ZmallConstant {
	public final static String DEFAULT_PROFILE_NAME = "default.png";
	
	public final static String PROFILE_FOLDER = "d:/upload/profile/";
	public static final String ATTACHMENT_FOLDER = "d:/upload/attachment/";
	public static final String IMAGE_FOLDER ="d:/upload/image/";
	public static final String TEMP_FOLDER = "d:/upload/temp/";
	public static final String PRODUCT_FOLDER = "d:/upload/product/";
	
	public final static String PROFILE_URL = "/display?imagename=";
	public static final String ATTACHMENT_URL = "/attachment/filename=";
	public static final String IMAGE_URL = "http://localhost:8081/board/image?imagename=";
	public static final String TEMP_URL = "http://localhost:8081/temp/image?imagename=";
	public static final String PRODUCT_URL = "http://localhost:8081/product/image?imagename=";
	
	public static final String CK_FIND_PATTERN = "http://localhost:8081/temp";
	public static final String CK_REPLACE_PATTERN = "http://localhost:8081/board";
	
	public static final String IBIMAGE_FOLDER ="c:/upload/ibimage/";
}
