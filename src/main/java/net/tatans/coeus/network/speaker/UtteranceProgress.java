package net.tatans.coeus.network.speaker;

import android.annotation.SuppressLint;
import android.speech.tts.UtteranceProgressListener;

@SuppressLint("NewApi")
public class UtteranceProgress extends UtteranceProgressListener{
	

	private static Callback defaultCallback =new Callback(){};
	private Callback callback =new Callback(){};
	private Callback toFileCallback =new Callback(){};
	public void setDefault(){
		this.callback=defaultCallback;
		this.toFileCallback=defaultCallback;
	}
	public void setDefaultSpeakerCallback(){
		this.callback=defaultCallback;
	}
	public void setDefaultToFileCallback(){
		this.toFileCallback=defaultCallback;
	}
	@Override
	public void onStart(String arg0) {
		if(arg0.equals(Speaker.UtteranceId)){
			callback.onStart();
		}
		if(arg0.equals(Speaker.toFileUtteranceId)){
			toFileCallback.onStart();
		}
	}
	@Override
	public void onDone(String arg0) {
		if(arg0.equals(Speaker.UtteranceId)){
			callback.onDone();
		}
		if(arg0.equals(Speaker.toFileUtteranceId)){
			toFileCallback.onDone();
		}
	}

	@Override
	public void onError(String arg0) {
		if(arg0.equals(Speaker.UtteranceId)){
			callback.onError();
		}
		if(arg0.equals(Speaker.toFileUtteranceId)){
			toFileCallback.onError();
		}
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setToFileCallback(Callback toFileCallback) {
		this.toFileCallback = toFileCallback;
	}
	
}