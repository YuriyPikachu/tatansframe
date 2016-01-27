package net.tatans.coeus.network.DefaultHttpClient;

import java.io.IOException;
import java.util.List;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.util.HttpProces;
import net.tatans.coeus.network.util.HttpTool;
import net.tatans.coeus.network.util.HttpTool.getTicketCallBackInterface;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
/**
 * 
 * @author Yuliang
 *
 */
public class CoeusHttpClient extends DefaultHttpClient{
	
	public String httpRequest;
	public CoeusHttpClient(
            final ClientConnectionManager conman,
            final HttpParams params) {
        super(conman, params);
    }
	public CoeusHttpClient() {
        super(null, null);
    }
	/**
	 * 
	 * @param HttpUriRequest
	 *            发送请求的Post
	 * @param context
	 *            当前Context上下文
	 * @return 服务器响应字符串 （有验证）
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 */
	public HttpResponse execute(HttpUriRequest request,final Context context,String oauth) throws ClientProtocolException, IOException,JSONException {
			HttpResponse httpRespone = null;
			final HttpTool ht = new HttpTool();
			if(!ht.hasNetworkConnection(context)){
				HttpProces.failHttp();
				TatansToast.showAndCancel("当前网络未连接，请重新连接网络再尝试。"); 
				return null;
			}
			HttpProces.startHttp();
			if("yes".equals(oauth)){
				String sCookie = context.getSharedPreferences("savecookie",Context.MODE_PRIVATE).getString("cookie","false");
				Log.d("NetWork", "HyperionHttpClient->cookie:"+context+" - "+sCookie);
				if("false".equals(sCookie)){
					ht.getTicketCall(context,request.getURI().toString(), new getTicketCallBackInterface(){
						@Override
						public void getTicketCallBack(String sTicket) {
							httpRequest = sTicket;
						}
					});
					
					if(httpRequest==null){
						/*final Speaker speakerPorcess=Speaker.getInstance(context);
						speakerPorcess.setOnSpeechCompletionListener(new onSpeechCompletionListener() {
							@Override
							public void onCompletion(int arg0) {
								if(arg0 == 0) {
									speakerPorcess.setOnSpeechCompletionListener(null);
									ht.gotoLoginActivity(context);
								}
							}
						});
						speakerPorcess.speech("当前账号信息过期，请重新登录。");*/
						return null;
					}
					if(request instanceof HttpPost){
						HttpPost post = new HttpPost(request.getURI().toString()+"?ticket="+httpRequest);
						post.setEntity(((HttpEntityEnclosingRequestBase) request).getEntity());
						request=post;
					}
					if(request instanceof HttpGet){
						if(request.getURI().toString().indexOf("?")==-1)
							request = new HttpGet(request.getURI().toString()+"?ticket="+httpRequest);
						else
							request = new HttpGet(request.getURI().toString()+"&ticket="+httpRequest);
					}
				}else{
					request.setHeader("Cookie", "JSESSIONID=" + sCookie); 
				}
			}
			try {
				httpRespone= execute(request, (HttpContext) null);
			} catch (ConnectTimeoutException e) {
				TatansToast.showAndCancel("网络访问超时,请重新连接。");
				HttpProces.failHttp();
				Log.d("NetWork", "HyperionHttpClient->execute"+e.toString());
			}
			Log.i("NetWork", "HyperionHttpClient->code: "+httpRespone.getStatusLine().getStatusCode()+" url: "+request.getURI().toString());
			if(httpRespone.getStatusLine().getStatusCode()==200){
				HttpProces.successHttp();
				List<Cookie> cookies = getCookieStore().getCookies();
				if(cookies.size()!=0&&"yes".equals(oauth)){
					context.getSharedPreferences("savecookie", Context.MODE_PRIVATE).edit().putString("cookie", cookies.get(0).getValue()).commit();
				}
			}else{
				HttpProces.failHttp();
				return null;
			}
	        return httpRespone;
	}
}
