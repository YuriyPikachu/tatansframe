package net.tatans.coeus.network.view;

import net.tatans.coeus.network.imp.ISendChar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;


/**
 * Created by SiLiPing on 2015/12/23.
 */
public class NewTextView extends TextView {

    private String TAG = "NewTextView";
    ISendChar iSendChar;

    public NewTextView(Context context,ISendChar iSendChar) {
        super(context);
        this.iSendChar = iSendChar;
    }

    /**
     * (API级别4)当用户在一个视图操作时调用此方法。事件是按照用户操作类型分类,如TYPE_VIEW_CLICKED。
     * 你通常不需要实现该方法,除非你是创建一个自定义视图。
     * @param eventType
     */
    @SuppressLint("NewApi")
	@Override
    public void sendAccessibilityEvent(int eventType) {
        super.sendAccessibilityEvent(eventType);
        if(eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
            iSendChar.onSendChar(this.getText().toString());
        }
    }
}
