package net.tatans.coeus.network.callback;

import net.tatans.coeus.network.josn.analysis.JsonDeepUtil;
import net.tatans.coeus.network.util.HttpProces;

/**
 * 
 * @author 余亮
 *
 * @param <T> 目前泛型支持 String,File, 以后扩展：JSONObject,Bitmap,byte[],XmlDom
 */
public abstract class HttpRequestCallBack<T> {
	
	private boolean progress = true;
	private int rate = 1000 * 1;//每秒
	private JsonDeepUtil jdu = new JsonDeepUtil();
//	private Class<T> type;
//	
//	public AjaxCallBack(Class<T> clazz) {
//		this.type = clazz;
//	}
	private Class<?> c;
	public Class<?> getJsonAnaylsisClass(){
		return c;
	}
	
	public boolean isProgress() {
		return progress;
	}
	
	public int getRate() {
		return rate;
	}
	
	/**
	 * 设置进度,而且只有设置了这个了以后，onLoading才能有效。
	 * @param progress 是否启用进度显示
	 * @param rate 进度更新频率
	 */
	public HttpRequestCallBack<T> progress(boolean progress , int rate) {
		this.progress = progress;
		this.rate = rate;
		return this;
	}
	
	public void onStart(){HttpProces.startHttp();};
	/**
	 * onLoading方法有效progress
	 * @param count
	 * @param current
	 */
	public void onLoading(long count,long current){};
	public void onSuccessSuper(T t){
		HttpProces.successHttp();
		onSuccess(t);
	}
	
	public void onSuccess(T t){
		c= getJsonAnaylsisClass();
		if(t instanceof String&&c!=null){
			try {
				Object obj = jdu.getEntityJson((String)t, c);
				onSuccessAnalysis(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void onSuccessAnalysis(Object obj) {
		
	}

	public void onFailure(Throwable t,String strMsg){HttpProces.failHttp();};
	
	
}
