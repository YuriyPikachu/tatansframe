package net.tatans.coeus.network.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
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
   * @param message
   */
  public static void showShort(CharSequence message)  
  {  
      if (isShow){
    	  mToast =Toast.makeText(TatansApplication.getContext(), message, Toast.LENGTH_SHORT);
    	  mToast.show();
      }  
  }  

  /**
   * 短时间显示Toast
   *  
   * @param message
   */
  public static void showShort( int message)  
  {  
      if (isShow){
    	  mToast=Toast.makeText(TatansApplication.getContext(), message, Toast.LENGTH_SHORT);
    	  mToast.show();
      }  
  }  
  /**
   * 短时间显示Toast把前面cancel掉
   *  
   * @param message
   */
  public static void showAndCancel( CharSequence message)  
  {  
	  cancel();
      if (isShow){
    	  mToast=Toast.makeText(TatansApplication.getContext(), message, Toast.LENGTH_SHORT);
    	  mToast.show();
      }  
  }  
  /**
   * 长时间显示Toast
   *  
   * @param message
   */
  public static void showLong( CharSequence message)  
  {  
      if (isShow){
    	  mToast= Toast.makeText(TatansApplication.getContext(), message, Toast.LENGTH_LONG);
    	  mToast.show();
      }  
  }  

  /**
   * 长时间显示Toast
   *  
   * @param message
   */
  public static void showLong( int message)  
  {  
      if (isShow){
    	  mToast= Toast.makeText(TatansApplication.getContext(), message, Toast.LENGTH_LONG);
    	  mToast.show();
      }  
  }  
  /**
   * 自定义显示Toast时间
   *  
   * @param message
   * @param duration
   */
  public static void show( CharSequence message, int duration)  
  {  
      if (isShow){
    	  mToast=Toast.makeText(TatansApplication.getContext(), message, duration);
    	  mToast.show();
      } 
  }  

  /**
   * 自定义显示Toast时间
   *  
   * @param message
   * @param duration
   */
  public static void show(int message, int duration)  
  {    
      if (isShow){
    	  mToast=Toast.makeText(TatansApplication.getContext(), message, duration);
    	  mToast.show();
      }  
  }  
  /**
   * 取消Toast显示
   *  
   */
  @SuppressLint("NewApi")
public static void cancel()  
  {  
      if (isShow){
    	  AccessibilityManager accessibilityManager = (AccessibilityManager) TatansApplication.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
	      accessibilityManager.interrupt();
    	  if(null!=mToast)
    		  mToast.cancel();
      }  
  }
}

