package net.tatans.coeus.network.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
/**
 * @author 余亮 <br/> 
 * 软盘操作类
 * <br/> 2015-7-22  
 * 
 */
public class TatansKeyBoard  
{  
  /**
   * 打卡软键盘
   *  
   * @param mEditText
   *            输入框
   * @param mContext
   *            上下文
   */
  @SuppressLint("NewApi")
public static void openKeybord(EditText mEditText, Context mContext)  
  {  
      InputMethodManager imm = (InputMethodManager) mContext  
              .getSystemService(Context.INPUT_METHOD_SERVICE);  
      imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);  
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,  
              InputMethodManager.HIDE_IMPLICIT_ONLY);  
  }  

  /**
   * 关闭软键盘
   *  
   * @param mEditText
   *            输入框
   * @param mContext
   *            上下文
   */
  @SuppressLint("NewApi")
public static void closeKeybord(EditText mEditText, Context mContext)  
  {  
      InputMethodManager imm = (InputMethodManager) mContext  
              .getSystemService(Context.INPUT_METHOD_SERVICE);  

      imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);  
  }  
}
