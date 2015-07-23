package net.tatans.coeus.network.service;
/*package net.tatans.hyperion.network.service;




import net.tatans.hyperion.network.account.Authenticator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

*//**
 * @author Yuliang 
 * @time 2014-11-21
 * @version 1.0
 *//*
public class AuthenticationService extends Service {

    private static final String TAG = "NetWork";

    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "AuthenticationService->SampleSyncAdapter Authentication Service started.");
        }
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "AuthenticationService->SampleSyncAdapter Authentication Service stopped.");
        }
    }

    @SuppressLint("NewApi")
	@Override
    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "AuthenticationService->getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent);
        }
        return mAuthenticator.getIBinder();
    }
}
*/