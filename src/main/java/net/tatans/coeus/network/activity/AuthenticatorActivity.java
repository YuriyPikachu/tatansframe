package net.tatans.coeus.network.activity;

import java.lang.reflect.Method;

import org.json.JSONException;

import net.tatans.coeus.network.R;
import net.tatans.coeus.network.account.Accounts;
import net.tatans.coeus.network.account.Accounts.initAccountCallBackInterface;
import net.tatans.coeus.network.tools.JsonUtil;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.network.util.Constants;
import net.tatans.coeus.network.util.HttpProces;
import net.tatans.coeus.network.util.HttpTool;
import net.tatans.coeus.network.util.NetworkUtilities;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Yuliang
 * @time 2014-11-21
 * @version 1.0
 */
public class AuthenticatorActivity extends Activity {
	/** The Intent flag to confirm credentials. */
	public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";

	/** The Intent extra to store password. */
	public static final String PARAM_PASSWORD = "password";

	/** The Intent extra to store username. */
	public static final String PARAM_USERNAME = "username";

	public static final String sEdit = "初始密码为:1,2,3,4,5,6";

	/** The Intent extra to store username. */
	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

	/** The tag used to log to adb console. */
	private static final String TAG = "NetWork";
	public static Boolean bLoginCreate = false; // 用于判断是否登录界面，是的话进行跳转到无线网络连接
	private AccountManager mAccountManager;
	/** Keep track of the login task so can cancel it if requested */
	private UserLoginTask mAuthTask = null;

	private TextView mMessage;
	// private int iAccount = 2;// 判断是在输入用户时候，还是在输入密码时候
	private String mPassword;

	private EditText mPasswordEdit;

	/** Was the original caller asking for an entirely new account? */
	protected boolean mRequestNewAccount = false;
	private static initAccountCallBackInterface callback;
	private String mUsername;


	private EditText mUsernameEdit;

	private static Context context;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle icicle) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(icicle);
		mAccountManager = AccountManager.get(this);
		mRequestNewAccount = mUsername == null;
		setContentView(R.layout.account_login);
		mUsernameEdit = (EditText) findViewById(R.id.username_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);
		// String Imei = ((TelephonyManager)
		// getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		mUsernameEdit.setText(getSerialNumber());
		bLoginCreate = true;
		mMessage = (TextView) findViewById(R.id.message);
	}

	/**
	 * 
	 * getSerialNumber
	 * 
	 * @return result is same to getSerialNumber1()
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@SuppressLint("NewApi")
	public String getSerialNumber() {
		String serial = null;
		BluetoothAdapter m_BluetoothAdapter = null; 
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
		String m_szBTMAC = m_BluetoothAdapter.getAddress();
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception e) {
			Log.d("NetWork",
					"NetWork->AuthenticatorActivity->getSerialNumber()"
							+ e.toString());

		}
		return m_szBTMAC+serial;

	}

	public void setContext(Context con) {
		context = con;
	}

	public void setAccountCallBack(initAccountCallBackInterface accountCallback) {
		callback = accountCallback;
	}

	public void creatActivity() {
		if (!Accounts.bLogin) {
			Intent it = new Intent(AuthenticatorActivity.this,
					context.getClass());
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(it);
		}
		callback.initAccountCallBack();
		finish();
	}

	/**
	 * 接收服务端返回来的授权
	 * 
	 * @param authToken
	 *            .
	 */
	@SuppressLint("NewApi")
	private void finishLogin(String authToken) {
		Log.i(TAG, "AuthenticatorActivity->finishLogin()");
		final Account account = new Account(mUsername, Constants.ACCOUNT_TYPE);
		String sResult;
		String sId = null;
		String sName = null;
		String sTgt = null;
		try {
			sResult = JsonUtil.analysisJsonUtil(authToken).getString("result");
			JsonUtil juResult = JsonUtil.analysisJsonUtil(sResult);
			sId = juResult.getString("id");
			sName = juResult.getString("username");
			sTgt = juResult.getString("tgt");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle bUser = new Bundle();
		bUser.putString("UserId", sId);
		bUser.putString("UserName", sName);
		if (mRequestNewAccount) {
			try {
				mAccountManager.addAccountExplicitly(account, sTgt, bUser);
			} catch (Exception e) {
				e.printStackTrace();
				TatansToast.showAndCancel("请从官方正常渠道下载该应用");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Log.e("AuthenticatorActivity", e.toString());
				System.exit(0);
			}
			loginProcess("登录成功。" + "为了您的安全请在设置账户信息中修改你的密码。");
		} else {
			mAccountManager.setPassword(account, mPassword);
		}
	}

	/**
	 * 验证账户身份过程.
	 * 
	 * @param authToken
	 *            the authentication token returned by the server, or NULL if
	 *            authentication failed.
	 */
	public void onAuthenticationResult(String authToken) {
		String success = null;
		if (null == authToken || "null".equals(authToken)) {
			return;
		}
		try {
			success = JsonUtil.analysisJsonUtil(authToken).getString("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "AuthenticatorActivity->onAuthenticationResult(" + success+ ")__" + authToken);
		mAuthTask = null;
		if ("true".equals(success)) {
			finishLogin(authToken);
		} else if ("false".equals(success)) {
			Log.e(TAG,
					"AuthenticatorActivity->onAuthenticationResult: failed to authenticate___"
							+ mRequestNewAccount);
			TatansToast.showAndCancel("密码输入错误,请重新输入");
			if (mRequestNewAccount) {
				// "Please enter a valid username/password.
				mMessage.setText(getText(R.string.login_activity_loginfail_text_pwonly));
			} else {
				// "Please enter a valid password." (Used when the
				// account is already in the database but the password
				// doesn't work.)
				mMessage.setText(getText(R.string.login_activity_loginfail_text_pwonly));
			}
		}
	}

	/**
	 * 注册身份过程.
	 * 
	 * @param authToken
	 *            the authentication token returned by the server, or NULL if
	 *            authentication failed.
	 */
	public void onRegisterResult(String authToken, Context context) {
		String success = null;
		if (null == authToken || "null".equals(authToken)) {
			return;
		}
		try {
			success = JsonUtil.analysisJsonUtil(authToken).getString("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "AuthenticatorActivity->onAuthenticationResult(" + success
				+ ")__" + authToken);
		mAuthTask = null;
		if ("true".equals(success)) {
			finishRegister(authToken,context);
		} else if ("false".equals(success)) {
			Log.e(TAG,
					"AuthenticatorActivity->onAuthenticationResult: failed to authenticate___"
							+ mRequestNewAccount);
			// speaker.speech("密码输入错误,请重新输入");
		}
	}

	/**
	 * 接收服务端返回来的授权
	 * 
	 * @param authToken
	 *            .
	 */
	@SuppressLint("NewApi")
	private void finishRegister(String authToken,Context context) {
		Log.i(TAG, "AuthenticatorActivity->finishRegister()");
		final Account account = new Account(getSerialNumber(),Constants.ACCOUNT_TYPE);
		AccountManager mAccountManager2 = AccountManager.get(context);
		String sResult;
		String sId = null;
		String sName = null;
		String sTgt = null;
		try {
			sResult = JsonUtil.analysisJsonUtil(authToken).getString("result");
			JsonUtil juResult = JsonUtil.analysisJsonUtil(sResult);
			sId = juResult.getString("id");
			sName = juResult.getString("username");
			sTgt = juResult.getString("tgt");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle bUser = new Bundle();
		bUser.putString("UserId", sId);
		bUser.putString("UserName", sName);
		try {
			mAccountManager2.addAccountExplicitly(account, sTgt, bUser);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Log.e("AuthenticatorActivity", e.toString());
		}
	}

	/**
	 * 返回消息到输出信息.
	 */
	private CharSequence getMessage() {
		getString(R.string.label);
		if (TextUtils.isEmpty(mPassword)) {
			// We have an account but no password
			TatansToast.showAndCancel("密码不能为空，请输入密码");
			return getText(R.string.login_activity_loginfail_text_pwmissing);
		}
		return null;
	}

	/**
	 * Represents an asynchronous task used to authenticate a user against the
	 * SampleSync Service
	 */
	@SuppressLint("NewApi")
	public class UserLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// We do the actual work of authenticating the user
			// in the NetworkUtilities class.
			try {
				return NetworkUtilities.authenticate(mUsername, mPassword,
						AuthenticatorActivity.this);
			} catch (Exception ex) {
				Log.e(TAG,
						"AuthenticatorActivity->UserLoginTask.doInBackground: failed to authenticate");
				return null;
			}
		}

		@Override
		protected void onPostExecute(final String authToken) {
			// On a successful authentication, call back into the Activity to
			// communicate the authToken (or null for an error).
			onAuthenticationResult(authToken);
		}

	}


	

	// 为了更好的体验先进行网络判断
	public void isConnectNetwork(Context con) {
		HttpTool ht = new HttpTool();
		if (!ht.hasNetworkConnection(context)) {
			HttpProces.cancelHttp(context);
			return;
		}
		//reportHelp();
	}

	/*public void onKey1LongClick() {
		Speaker speakerPorcess = getSpeaker();
		speakerPorcess
				.setOnSpeechCompletionListener(new onSpeechCompletionListener() {
					@Override
					public void onCompletion(int arg0) {
						if (arg0 == 0) {
							getSpeaker().setOnSpeechCompletionListener(null);
							// startActivity(new
							// Intent(Settings.ACTION_SETTINGS));
							Intent intent = new Intent();
							String activityName = "net.tatans.hyperion.settings.activities.WifiSettingActivity";
							String pkgName = "net.tatans.hyperion.settings";
							intent.setComponent(new ComponentName(pkgName,
									activityName));
							try {
								startActivity(intent);
							} catch (Exception e) {
								speaker.speech("该应用还为安装");
							}
						}
					}
				});
		getSpeaker().speech("正在进入无线网络设置界面");
	}*/

	@SuppressLint("NewApi")
	public void onLoginClick(View view) {
		// TODO Auto-generated method stub
		if (mRequestNewAccount) {
			mUsername = mUsernameEdit.getText().toString();
		}
		mPassword = mPasswordEdit.getText().toString();
		if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
			mMessage.setText(getMessage());
		} else {
			mAuthTask = new UserLoginTask();
			mAuthTask.execute();
		}
	}

	/*
	@Override
	public void onKey3Click() {
		if (Accounts.bLogin) {
			finish();
			Activity aContext = (Activity) context;
			aContext.finish();
		} else {
			speaker.speech("请输入正确的密码");
		}
	}*/


	private void loginProcess(String str) {
		/*Speaker speakerPorcess = getSpeaker();
		speakerPorcess
				.setOnSpeechCompletionListener(new onSpeechCompletionListener() {
					@Override
					public void onCompletion(int arg0) {
						if (arg0 == 0) {
							getSpeaker().setOnSpeechCompletionListener(null);
							creatActivity();
						}
					}
				});
		getSpeaker().speech(str);*/
	}

	/*protected void reportHelp() {
		speaker.speech("当前所在:登录界面，若想体验更好的服务请先登录,长按A键可以进入无线网络连接" + sEdit);
	}*/
}
