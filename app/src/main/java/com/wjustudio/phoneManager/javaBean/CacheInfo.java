package com.wjustudio.phoneManager.javaBean;

public class CacheInfo {
	public String packageName;
	public String cacheSize;
	public CacheInfo(String packageName, String cacheSize) {
		super();
		this.packageName = packageName;
		this.cacheSize = cacheSize;
	}
	@Override
	public String toString() {
		return "CacheInfo [packageName=" + packageName + ", cacheSize="
				+ cacheSize + "]";
	}
	
	
}
