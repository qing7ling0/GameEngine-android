package com.qq.engine.net;


/**
 * @author wuqingqing
 * 需要在后台下载资源的，需要实现此接口
 */
public interface IDownloadCallback {	
	/** 下载完成后，回调此函数
	 * @param resInfo
	 */
	public void downloadCallback(ResInfo resInfo);
}
