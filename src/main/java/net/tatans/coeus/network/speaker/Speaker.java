package net.tatans.coeus.network.speaker;

import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class Speaker implements OnInitListener {
	private static final String talkback_interrupt_action="talkback.interrupt";
	private String TAG ="coeus-tts-speaker";
	private TextToSpeech tts;
	private String text = "";
	private boolean speakInit = false;
	private boolean ready = false;
	private boolean allowed = true;

	private static Speaker singleton;
	public static String UtteranceId="UTTERANCE";
	public static String toFileUtteranceId="ToFileUtteranceId";
	public static String NoUtteranceId="NoUtteranceId";
//	回调实现类
	private UtteranceProgress progress;
	private Context context;

	public static Speaker getInstance(Context ctx) {
        if (singleton == null)
            singleton = new Speaker(ctx);
        return singleton;
    }
	
	private Speaker(Context context) {
		tts = new TextToSpeech(context, this,"com.iflytek.speechcloud");
		progress=new UtteranceProgress();
		tts.setOnUtteranceProgressListener(progress);
		this.context=context;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void allow(boolean allowed) {
		this.allowed = allowed;
	}

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			// Change this to match your
			// locale
			tts.setLanguage(Locale.CHINA);
			
			ready = true;
		}
		if (speakInit) {
			speech(text);
			text="";
			speakInit = false;
		}
	}

	/**
	 * 无回调报读
	 * @param text 报读文本
	 */
	public void speech(String text) {
		
		speech(text, null,  TextToSpeech.QUEUE_FLUSH);
	}

	/**
	 * 播报完成回调
	 * @param text
	 * @param callback
	 */
	public void speech(String text,Callback callback){
		speech(text, callback, TextToSpeech.QUEUE_FLUSH);
	}
	/**
	 * 播报完成回调
	 * @param text
	 * @param queue 播报队列类型   TextToSpeech.QUEUE_FLUSH。刷新队列播报，TextToSpeech.QUEUE_ADD  增加到对列末尾,默认是TextToSpeech.QUEUE_FLUSH
	 */
	public void speech(String text,int queue){
		speech(text, null, queue);
	}
	/**
	 * 播报完成回调
	 * @param text
	 * @param callback
	 * @param queue 播报队列类型   TextToSpeech.QUEUE_FLUSH。刷新队列播报，TextToSpeech.QUEUE_ADD  增加到对列末尾
	 */
	public void speech(String text,Callback callback,int queue){
		Log.i(TAG, text);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
				String.valueOf(AudioManager.STREAM_MUSIC));
		if(callback!=null){
			progress.setCallback(callback);
			hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,UtteranceId);
		}else{
//			不设置参数这不会起效回调 会导致获取媒体焦点失败
			hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,NoUtteranceId);
			progress.setDefaultSpeakerCallback();
		}
		tts.speak(text, queue, hash);
	}
	
	public void pause(int duration) {
		pause(duration, null);
	}
	/**
	 * @param duration 时间长度毫秒
	 * @param callback回调
	 */
	public void pause(int duration,Callback callback) {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
				String.valueOf(AudioManager.STREAM_MUSIC));
		if(callback==null){
			progress.setDefaultSpeakerCallback();
		}else{
			hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,UtteranceId);
			progress.setCallback(callback);
		}
		tts.playSilence(duration, TextToSpeech.QUEUE_FLUSH, hash);
	}
	
	public void stop() {
		tts.stop();
	}

	public void destroy() {
		tts.shutdown();
	}

	public void stopAndContinue(String text) {
		this.stop();
		speech(text);
	}
//	打断talkback
	public static void interruptTalkback(Context context){
		context.sendBroadcast(new Intent(talkback_interrupt_action));
	}
	/**
	 * @param text 文本
	 * @param filename 文件地址
	 * @return 0 成功   -1 失败
	 */
	public int toFile(String text,String filename,Callback callback){
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,toFileUtteranceId);
		progress.setToFileCallback(callback);
		return tts.synthesizeToFile(text, hash, filename);
	}
	
//	speaker回调
	
	/**
	 * 打断talkback和speaker的声音
	 */
	public void stopAllSound(){
		speech("");
		interruptTalkback(context);
	}
}
