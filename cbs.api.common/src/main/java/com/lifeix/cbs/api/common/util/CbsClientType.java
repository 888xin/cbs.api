package com.lifeix.cbs.api.common.util;

public enum CbsClientType {

	// web
	ROI_WEB("key:roi_web" , "" , "web"),
		
	// android
	ROI_ANDROID("key:roi_android", "http://apps.L99.com/" , "Android"),
	
	// iPhone
	ROI_IPHONE("key:roi_iphone", "http://apps.L99.com/" , "iPhone");

	private final String key;
	
	private final String url;
	
	private final String name;
	
	
	CbsClientType(String key , String url , String name) {
		this.key = key;
		this.url = url;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param source
	 * @return
	 */
	public static CbsClientType getClient(String source) {
		if(source == null || source.trim().equals("")) return ROI_WEB;
		for (CbsClientType val : CbsClientType.values()) {
			if (val.getKey().toLowerCase().equals(source.toLowerCase())) {
				return val;
			}
		}
		return ROI_WEB;
	}

}
