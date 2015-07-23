package net.tatans.coeus.network.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import net.tatans.coeus.network.DefaultHttpClient.CoeusHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
/**
 * 
 * @author Yuliang
 *
 */
public class HttpCallable<T> implements Callable<T>{
	private String requestType,url,oauth;
	private Map<String, String> rawParams;
	private Context context;
	private HttpUriRequest hur = null;
	private File file;
	public HttpCallable(String requestType,String url,String oauth,Map<String, String> rawParams,Context context,File file){
		this.requestType = requestType;
		this.url = url;
		this.oauth = oauth;
		this.rawParams = rawParams;
		this.context = context;
		this.file = file;
	}
	@SuppressWarnings({ "unchecked", "resource" })
	@Override
	public T call() throws Exception {
		Log.i("NetWork", "HttpCallable->rawParams:"+rawParams);
		HttpResponse httpResponse = getHttpResponse();
		if (null == httpResponse) {
			return null;
		}
		if("file".equals(requestType)){
			OutputStream output = null;
			InputStream input = null;
			input = httpResponse.getEntity().getContent();
			if (!file.exists()) {
				file.createNewFile();
				output = new FileOutputStream(file);
				byte buffer[] = new byte[4 * 1024];
				int len;
				while ((len = input.read(buffer)) != -1) {
					output.write(buffer, 0, len);
				}
				output.flush();
			} else
				Log.i("NetWork", "HttpCallable->文件已经存在");
			return (T) file;
		}else{
			String result = EntityUtils.toString(httpResponse
					.getEntity());
			Log.i("NetWork", "HttpCallable->result:"+result);
			if("yes".equals(oauth)){
				HttpTool ht = new HttpTool();
				result = ht.checkOuath(result, context,hur);
			}
			return (T) result;
		}
	}
	private HttpResponse getHttpResponse() throws ClientProtocolException, IOException, JSONException {
		// 创建HttpPost对象。
		if ("post".equals(requestType)||"file".equals(requestType)) {
			hur = new HttpPost(url);
			// 如果传递参数个数比较多的话可以对传递的参数进行封装
			if (rawParams != null && !"null".equals(rawParams)) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (String key : rawParams.keySet()) {
					// 封装请求参数
					params.add(new BasicNameValuePair(key,
							rawParams.get(key)));
				}
				hur.addHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=utf-8");
				// 设置请求参数
				((HttpEntityEnclosingRequestBase) hur).setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			}
		}
		if ("get".equals(requestType)) {
			hur = new HttpGet(url);
		}
		HttpConnectionParams.setConnectionTimeout(hur.getParams(), 10000);
		HttpConnectionParams.setSoTimeout(hur.getParams(), 10000);
		return HttpSingleton.getInstance().execute(hur, context,oauth);
	}
	public static class HttpSingleton{
		private static CoeusHttpClient sInstance = null;
		protected HttpSingleton(){
			
		}
		public static CoeusHttpClient getInstance() {
			if (sInstance == null) {
				sInstance = new CoeusHttpClient();
			}
			return sInstance;
		}	
	}
}

