package net.tatans.coeus.network.tools;

import net.tatans.coeus.exception.ApplicationException;
import android.app.Application;
import android.content.Context;

public class TatansApplication extends Application{
	/**
	 * Global application context.
	 */
	private static Context sContext;

	/**
	 * Construct of LitePalApplication. Initialize application context.
	 */
	public TatansApplication() {
		sContext = this;
	}

    /**
     * Initialize to make  ready to work. If you didn't configure LitePalApplication
	 * in the AndroidManifest.xml, make sure you call this method as soon as possible. In
	 * Application's onCreate() method will be fine.
	 *
     * @param context
	 * 		Application context.
     */
    public static void initialize(Context context) {
        sContext = context;
    }

	/**
	 * Get the global application context.
	 * 
	 * @return Application context.
	 * @throws org.litepal.exceptions.GlobalException
	 */
	public static Context getContext() {
		if (sContext == null) {
			throw new ApplicationException("没有注册TatansApplication类");
		}
		return sContext;
	}
}
