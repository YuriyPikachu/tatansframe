package net.tatans.coeus.network.tools;

import android.content.Context;
import android.widget.Toast;
/**
 * @author 余亮 <br/> 
 * Toast的相关操作
 * <br/> 2015-7-22  
 * 
 */
public class TatansToast  
{  
  private TatansToast()  
  {  
      /** cannot be instantiated**/
      throw new UnsupportedOperationException("cannot be instantiated");  
  }  
  private static Toast mToast;
  public static boolean isShow = true;  
/**
   * 短时间显示Toast
   *  
   * @param context
   * @param message
   */
  public static void showShort(Context context, CharSequence message)  
  {  
      if (isShow){
    	  mToast =Toast.makeText(context, message, Toast.LENGTH_SHORT);
    	  mToast.show();
      }  
  }  

  /**
   * 短时间显示Toast
   *  
   * @param context
   * @param message
   */
  public static void showShort(Context context, int message)  
  {  
      if (isShow){
    	  mToast=Toast.makeText(context, message, Toast.LENGTH_SHORT);
    	  mToast.show();
      }  
  }  
  /**
   * 长时间显示Toast
   *  
   * @param context
   * @param message
   */
  public static void showLong(Context context, CharSequence message)  
  {  
      if (isShow){
    	  mToast= Toast.makeText(context, message, Toast.LENGTH_LONG);
    	  mToast.show();
      }  
  }  

  /**
   * 长时间显示Toast
   *  
   * @param context
   * @param message
   */
  public static void showLong(Context context, int message)  
  {  
      if (isShow){
    	  mToast= Toast.makeText(context, message, Toast.LENGTH_LONG);
    	  mToast.show();
      }  
  }  
  /**
   * 自定义显示Toast时间
   *  
   * @param context
   * @param message
   * @param duration
   */
  public static void show(Context context, CharSequence message, int duration)  
  {  
      if (isShow){
    	  mToast=Toast.makeText(context, message, duration);
    	  mToast.show();
      } 
  }  

  /**
   * 自定义显示Toast时间
   *  
   * @param context
   * @param message
   * @param duration
   */
  public static void show(Context context, int message, int duration)  
  {  
      if (isShow){
    	  mToast=Toast.makeText(context, message, duration);
    	  mToast.show();
      }  
  }  
  /**
   * 取消Toast显示
   *  
   */
  public static void cancel()  
  {  
      if (isShow){
    	  if(null!=mToast)
    		  mToast.cancel();
      }  
  }
}

