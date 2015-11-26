package net.tatans.coeus.network.tools;

import android.util.TypedValue;

/**
* @author 余亮 <br/>
* 常用单位转换的辅助类
*  
*/
public class TatansDensity  
{  
  private TatansDensity()  
  {  
      /** cannot be instantiated **/
      throw new UnsupportedOperationException("cannot be instantiated");  
  }  

  /**
   * dp转px
   *  
   * @param context
   * @param val
   * @return
   */
   public static int dp2px( float dpVal)  
      {  
          return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
                  dpVal, TatansApplication.getContext().getResources().getDisplayMetrics());  
      }  

      /**
       * sp转px
       *  
       * @param context
       * @param val
       * @return
       */
      public static int sp2px( float spVal)  
      {  
          return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  
                  spVal, TatansApplication.getContext().getResources().getDisplayMetrics());  
      }  

      /**
       * px转dp
       *  
       * @param context
       * @param pxVal
       * @return
       */
      public static float px2dp( float pxVal)  
      {  
          final float scale = TatansApplication.getContext().getResources().getDisplayMetrics().density;  
          return (pxVal / scale);  
      }
          /**
           * px转sp
           *  
           * @param fontScale
           * @param pxVal
           * @return
           */
          public static float px2sp( float pxVal)  
          {  
              return (pxVal / TatansApplication.getContext().getResources().getDisplayMetrics().scaledDensity);  
          }  

      }

