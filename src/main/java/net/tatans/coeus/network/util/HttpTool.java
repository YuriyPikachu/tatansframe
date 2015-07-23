package net.tatans.coeus.network.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import net.tatans.coeus.network.util.HttpCallable.HttpSingleton;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * @author Yuliang 
 * @time 2014-11-21 
 * @version 1.0
 */
public class HttpTool {
	public boolean hasNetworkConnection(Context context) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		boolean hasNetworkConnection = hasConnectedWifi || hasConnectedMobile;
		return hasNetworkConnection;
	}
	@SuppressLint("NewApi")
	/*public void gotoLoginActivity(Context context){
		AccountManager mAccountManager;
		mAccountManager = AccountManager.get(context);
		Account[] accounts = mAccountManager
				.getAccountsByType(Constants.ACCOUNT_TYPE);
		Intent it = new Intent(context, AuthenticatorActivity.class);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getSharedPreferences("savecookie",Context.MODE_PRIVATE).edit().clear().commit();
		context.startActivity(it);
		mAccountManager.removeAccount(accounts[0],null,null);
	}*/
	public static String httpRequest;
	@SuppressLint("NewApi")
	public String cookieRequestFail(final Context context,HttpUriRequest request) throws JSONException, ParseException, IOException{
		context.getSharedPreferences("savecookie",Context.MODE_PRIVATE).edit().clear().commit();
		HttpResponse httpResponse = HttpSingleton.getInstance().execute(request, context,
				"yes");
		if (null == httpResponse) {
			return null;
		}
		// 获取服务器响应字符串
		String result = EntityUtils.toString(httpResponse
				.getEntity());
		Log.i("NetWork", "HttpTool->cookieRequestFail: "+result);
		return result;
	}
	@SuppressLint("NewApi")
	public void getTicketCall(Context context,String url, getTicketCallBackInterface callBack) throws JSONException {
		AccountManager mAccountManager;
		mAccountManager = AccountManager.get(context);
		Account[] accounts = mAccountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
		String strTGT = null;
		try {
			 strTGT = mAccountManager.getPassword(accounts[0]);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("NetWork", "HttpTool->---:"+e.toString());
		}
		Map<String, String> map = new HashMap<String, String>();
		Log.d("NetWork", "HttpTool->tgt: "+strTGT);
		map.put("TGT", strTGT);
		map.put("service", url);
		NetWork nw = new NetWork();
		String s=nw.httpRequest(Constants.CAS_SERVICE_SERVER, map, context,"no","post");
		//获取TGT失败判断
		if(s.equals("null")){
			return;
		}
		JSONObject juToken1;
		String sResult = null;
		String sCode = null;
		juToken1 = new JSONObject(s);
		sResult = juToken1.getString("result");
		sCode = juToken1.getString("code");
		if("false".equals(sCode)){
			return;
		}
		callBack.getTicketCallBack(sResult);
	}
	public interface getTicketCallBackInterface {
		public void getTicketCallBack(String sTicket);
	}
	public String checkOuath(String sRequestPost,Context context,HttpUriRequest request) throws JSONException, ParseException, IOException{
		if(null!=sRequestPost&&sRequestPost.trim().startsWith("<!DOCTYPE")){
			HttpTool ht = new HttpTool();
			return ht.cookieRequestFail(context,request);
		}
		return sRequestPost;
	}
}
