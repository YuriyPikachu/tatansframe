package net.tatans.coeus.network.account;


import net.tatans.coeus.network.activity.AuthenticatorActivity;
import net.tatans.coeus.network.util.HttpProces;
import net.tatans.coeus.network.util.NetworkUtilities;
import android.content.Context;
import android.content.Intent;

/**
 * @author Yuliang 
 * @time 2014-11-21 
 * @version 1.0
 */
public class Accounts {
	public static Boolean bLogin = false;//判断是登录首次登录，还是token过期
	/**
	 * @param context
	 *            当前Context上下文
	 * @return 账户注册保存信息
	 */
	public void registerAccount(Context context){
		AuthenticatorActivity aa = new AuthenticatorActivity();
		aa.onRegisterResult(NetworkUtilities.register(aa.getSerialNumber(), "",context), context);
	}
	/**
	 * @param context
	 *            当前Context上下文
	 * @return 从账户中获取当前用户Id
	 */
	public static String getUserId(Context context) {
		AccountAcquire acount = new AccountAcquire();
		return acount.getAccountInformation(context, "UserId");
	}
	/**
	 * @param context
	 *            当前Context上下文
	 * @return 从账户中获取当前用户名字
	 */
	public static String getUserName(Context context) {
		AccountAcquire acount = new AccountAcquire();
		return acount.getAccountInformation(context, "UserName");
	}
	/**
	 * @param context 
	 * 		 当前Context上下文
	 */
	public void initAccountCall(Context context,initAccountCallBackInterface callback){
		AccountAcquire acount = new AccountAcquire();
		HttpProces.initHttp(context);
		AuthenticatorActivity aa = new AuthenticatorActivity();
		aa.setContext(context);
		aa.setAccountCallBack(callback);
		if (acount.AccountsAcquire(context).length == 0) {
			bLogin=true;
			Intent it = new Intent(context, AuthenticatorActivity.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
		}else{
			callback.initAccountCallBack();
		}
	}
	public interface initAccountCallBackInterface {
		public void initAccountCallBack();
	}
}
