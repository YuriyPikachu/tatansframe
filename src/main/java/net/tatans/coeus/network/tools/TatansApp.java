package net.tatans.coeus.network.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
* @author 余亮 <br/>
* 跟App相关的辅助类  
*/
public class TatansApp  
{  

  private TatansApp()  
  {  
      /**cannot be instantiated **/
      throw new UnsupportedOperationException("cannot be instantiated");  

  }  

  /**
   * 获取应用程序名称
   */
  public static String getAppName()  
  {  
      try
      {  
          PackageManager packageManager = TatansApplication.getContext().getPackageManager();  
          PackageInfo packageInfo = packageManager.getPackageInfo(  
        		  TatansApplication.getContext().getPackageName(), 0);  
          int labelRes = packageInfo.applicationInfo.labelRes;  
          return TatansApplication.getContext().getResources().getString(labelRes);  
      } catch (NameNotFoundException e)  
      {  
          e.printStackTrace();  
      }  
      return null;  
  }  

  /**
   * [获取应用程序版本名称信息]
   *  
   * @return 当前应用的版本名称
   */
  public static String getVersionName()  
  {  
      try
      {  
          PackageManager packageManager = TatansApplication.getContext().getPackageManager();  
          PackageInfo packageInfo = packageManager.getPackageInfo(  
        		  TatansApplication.getContext().getPackageName(), 0);  
          return packageInfo.versionName;  

      } catch (NameNotFoundException e)  
      {  
          e.printStackTrace();  
      }  
      return null;  
  }  

}
