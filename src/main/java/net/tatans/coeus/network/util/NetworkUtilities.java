package net.tatans.coeus.network.util;

import java.util.HashMap;
import java.util.Map;

import net.tatans.coeus.network.tools.HttpUtil;
import android.app.Activity;
import android.content.Context;

/**
 * @author Yuliang 
 * @time 2014-11-21 
 * @version 1.0
 */
final public class NetworkUtilities {
	private NetworkUtilities() {
	}

	/**
	 * Connects to the SampleSync test server, authenticates the provided
	 * username and password.
	 * 
	 * @param username
	 *            The server account username
	 * @param password
	 *            The server account password
	 * @return String The authentication token returned by the server (or null)
	 */
	public static String authenticate(String username, String password,Activity context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		String strPostRequest = null;
		strPostRequest = HttpUtil.postRequest(Constants.CAS_GRANT_SERVER,map,context);
		return strPostRequest;
	}
	public static String register(String username, String password,Context context) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", "123456");
		String strPostRequest = null;
		strPostRequest = HttpUtil.postRequest(Constants.CAS_GRANT_SERVER,map,context);
		return strPostRequest;
	}

}
