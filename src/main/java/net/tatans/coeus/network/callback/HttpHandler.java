
package net.tatans.coeus.network.callback;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;


import net.tatans.coeus.network.DefaultHttpClient.CoeusHttpClient;
import net.tatans.coeus.network.util.HttpProces;
import net.tatans.coeus.network.util.HttpTool;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

/**
 * 
 * @author Yuliang
 *
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class  HttpHandler  <T> extends  AsyncTask<Object, Object, Object> implements EntityCallBack{

	private final CoeusHttpClient client;
	private final HttpContext httpcontext;
	
	private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
	private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();
	
	private final AjaxCallBack<T> callback;
	private Context context;
	private int executionCount = 0;
	private String targetUrl = null; //下载的路径
	private boolean isResume = false; //是否断点续传
	private String charset;
	private String oauth;

	public HttpHandler(CoeusHttpClient client, HttpContext httpcontext, AjaxCallBack<T> callback,String charset,Context context,String oauth) {
		this.client = client;
		this.httpcontext = httpcontext;
		this.callback = callback;
		this.charset = charset;
		this.context = context;
		this.oauth = oauth;
	}


	private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
		if(isResume && targetUrl!= null){
			File downloadFile = new File(targetUrl);
			long fileLen = 0;
			if(downloadFile.isFile() && downloadFile.exists()){
				fileLen = downloadFile.length();
			}
			if(fileLen > 0)
				request.setHeader("RANGE", "bytes="+fileLen+"-");
		}
		
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client.getHttpRequestRetryHandler();
		while (retry) {
			try {
				if (!isCancelled()) {
					HttpResponse response;
					if("yes".equals(oauth)){
						HttpProces.bSound=false;
						 response = client.execute(request, context, oauth);
					}else{
						 response = client.execute(request, httpcontext);
					}
					if (!isCancelled()) {
						handleResponse(response,request);
					} 
				}
				return;
			} catch (UnknownHostException e) {
				publishProgress(UPDATE_FAILURE, e,"unknownHostException：can't resolve host");
				Log.d("DEBUG",e.getMessage());
				return;
			} catch (IOException e) {
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,httpcontext);
			} catch (NullPointerException e) {
				// HttpClient 4.0.x 之前的一个bug
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,httpcontext);
			}catch (Exception e) {
				cause = new IOException("Exception" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,httpcontext);
			}
		}
		if(cause!=null)
			throw cause;
		else
			throw new IOException("未知网络错误");
	}

	@Override
	protected Object doInBackground(Object... params) {
		if(params!=null && params.length == 3){
			targetUrl = String.valueOf(params[1]);
			isResume = (Boolean) params[2];
		}
		try {
			publishProgress(UPDATE_START); // 开始
			makeRequestWithRetries((HttpUriRequest)params[0]);
			
		} catch (IOException e) {
			publishProgress(UPDATE_FAILURE,e,e.getMessage()); // 结束
		}

		return null;
	}

	private final static int UPDATE_START = 1;
	private final static int UPDATE_LOADING = 2;
	private final static int UPDATE_FAILURE = 3;
	private final static int UPDATE_SUCCESS = 4;

	@SuppressWarnings("unchecked")
	@Override
	protected void onProgressUpdate(Object... values) {
		int update = Integer.valueOf(String.valueOf(values[0]));
		switch (update) {
		case UPDATE_START:
			if(callback!=null)
				callback.onStart();
			break;
		case UPDATE_LOADING:
			if(callback!=null)
				callback.onLoading(Long.valueOf(String.valueOf(values[1])),Long.valueOf(String.valueOf(values[2])));
			break;
		case UPDATE_FAILURE:
			if(callback!=null)
				callback.onFailure((Throwable)values[1],(String)values[2]);
			break;
		case UPDATE_SUCCESS:
			if(callback!=null){
				if(null==context){
					callback.onSuccessSuper((T)values[1]);
				}else{
					HttpTool ht = new HttpTool();
					try {
						callback.onSuccessSuper((T)ht.checkOuath((String)values[1], context,(HttpUriRequest)values[2]));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callback.onFailure(e,e.getMessage());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callback.onFailure(e,e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						callback.onFailure(e,e.getMessage());
					}catch (Exception e) {
						e.printStackTrace();
						callback.onFailure(e,e.getMessage());
					}
				}
			}
			break;
		default:
			break;
		}
		super.onProgressUpdate(values);
	}
	
	public boolean isStop() {
		return mFileEntityHandler.isStop();
	}


	/**
	 * @param stop 停止下载任务
	 */
	public void stop() {
		mFileEntityHandler.setStop(true);
	} 

	private void handleResponse(HttpResponse response,HttpUriRequest request) {
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() >= 300) {
			String errorMsg = "response status error code:"+status.getStatusCode();
			if(status.getStatusCode() == 416 && isResume){
				errorMsg += " \n maybe you have download complete.";
			}
			publishProgress(UPDATE_FAILURE,new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()),errorMsg);
		} else {
			try {
				HttpEntity entity = response.getEntity();
				Object responseBody = null;
				if (entity != null) {
					time = SystemClock.uptimeMillis();
					if(targetUrl!=null){
						responseBody = mFileEntityHandler.handleEntity(entity,this,targetUrl,isResume);
					}
					else{
						responseBody = mStrEntityHandler.handleEntity(entity,this,charset);
					}
						
				}
				publishProgress(UPDATE_SUCCESS,responseBody,request);
				
			} catch (IOException e) {
				publishProgress(UPDATE_FAILURE,e,e.getMessage());
			}
			
		}
	}
	
	
	private long time;
	@Override
	public void callBack(long count, long current,boolean mustNoticeUI) {
		if(callback!=null && callback.isProgress()){
			if(mustNoticeUI){
				publishProgress(UPDATE_LOADING,count,current);
			}else{
				long thisTime = SystemClock.uptimeMillis();
				if(thisTime - time >= callback.getRate()){
					time = thisTime ;
					publishProgress(UPDATE_LOADING,count,current);
				}
			}
		}
	}
	

}
