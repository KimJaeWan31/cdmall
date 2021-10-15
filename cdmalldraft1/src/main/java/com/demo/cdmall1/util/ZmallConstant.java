package com.demo.cdmall1.util;

public interface ZmallConstant {
	public final static String DEFAULT_PROFILE_NAME = "default.png";
	
	public final static String PROFILE_FOLDER = "c:/upload/profile/";
	public final static String PET_PROFILE_FOLDER = "c:/upload/pet_profile/";
	public static final String ATTACHMENT_FOLDER = "c:/upload/attachment/";
	public static final String IMAGE_FOLDER ="c:/upload/image/";
	public static final String IBIMAGE_FOLDER ="c:/upload/ibimage/";
	public static final String TEMP_FOLDER = "c:/upload/temp/";
	public static final String PRODUCT_FOLDER = "c:/upload/product/";
	public static final String PRODIMAGE_FOLDER ="c:/upload/productimage/";
	
	public final static String PROFILE_URL = "/display?imagename=";
	public static final String ATTACHMENT_URL = "/attachment/filename=";
	public static final String IMAGE_URL = "http://localhost:8081/board/image?imagename=";
	public static final String TEMP_URL = "http://localhost:8081/temp/image?imagename=";
	public static final String PRODUCT_URL = "http://localhost:8081/product/image?imagename=";
	
	public static final String CK_FIND_PATTERN = "http://localhost:8081/temp";
	public static final String CK_REPLACE_PATTERN = "http://localhost:8081/board";
	
	
}
