package net.tatans.coeus.network.account;


import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.util.Constants;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
/**
 * @author Yuliang 
 * @time 2014-11-21 
 * @version 1.0
 */
@SuppressLint("NewApi")
public class AccountAcquire {
	private AccountManager mAccountManager;
	public Account[] AccountsAcquire(Context context){
		mAccountManager = AccountManager.get(context);
		Account[] accounts = mAccountManager
				.getAccountsByType(Constants.ACCOUNT_TYPE);
		return accounts;
	}
	public String getAccountInformation(Context context,String sUser){
		String strUser =("UserId".equals(sUser))?"1":"Administrator"; 
		Account[] accounts = AccountsAcquire(context);
		if (accounts.length == 0) {
			return strUser;
		} else {
			try {
				strUser = mAccountManager.getUserData(accounts[0], sUser);
			} catch (Exception e) {
				TatansToast.showAndCancel("请从官方正常渠道下载该应用");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Log.e("NetWork", "AccountAcquire"+e.toString());
				System.exit(0);
			}
			return strUser;
		}
	}
	
}
