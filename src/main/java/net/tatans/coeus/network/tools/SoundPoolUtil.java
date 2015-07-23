package net.tatans.coeus.network.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

/**
 * @author wmt
 */
public class SoundPoolUtil implements Runnable {
	private boolean flag = false;
	private String path;
	private Map<Integer, Integer> map;

	private Context context;
	private List<Integer> CurList = new ArrayList<Integer>();;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private AssetManager assetManager;
	private AssetFileDescriptor soundFile;// 声音流
	private int CurPlay = 0;

	private AudioManager mgr;
	private float streamVolumeCurrent;
	private float streamVolumeMax;
	private float volume;

	private SoundPlayListener soundPlayListener = new SoundPlayImpl();
	private SoundFileListener soundFileListener = new SoundFileImpl();

	/**
	 * @param context
	 */
	public SoundPoolUtil(Context context, String path) {
		this.context = context;
		this.path = path;
		assetManager = context.getAssets();
		soundPoolMap = new HashMap<Integer, Integer>();
		mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = streamVolumeCurrent / streamVolumeMax;
		new Thread(this).start();
	}

	/**
	 * @param context
	 */
	public SoundPoolUtil(Context context, Map<Integer, Integer> map) {
		this.map = map;
		this.context = context;
		soundPoolMap = new HashMap<Integer, Integer>();
		mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = streamVolumeCurrent / streamVolumeMax;
		new Thread(this).start();
	}

	public void setSoundFileListener(SoundFileListener soundFileListener) {
		this.soundFileListener = soundFileListener;
	}

	public void setSoundPlayListener(SoundPlayListener soundPlayListener) {
		this.soundPlayListener = soundPlayListener;
	}

	@Override
	public void run() {
		 CurList= new ArrayList<Integer>();
		if (path != null) {
			initSound(path);
		} else if (map != null) {
			initSound(map);
		}
	}

	/**
	 * 加载音效资源
	 * @param map
	 */
	@SuppressLint("NewApi") 
	private void initSound(Map<Integer, Integer> map) {
		soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 100);
		for (Entry<Integer, Integer> m : map.entrySet()) {
			Integer tmp = m.getValue();
			soundPoolMap.put(m.getKey(), soundPool.load(context, tmp, 1));
		}
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				flag = true;
			}
		});
	}

	/**
	 * 加载音效资源
	 * @param path
	 */
	@SuppressLint("NewApi")
	private void initSound(String path) {
		try {
			soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 100);
			String[] fileNames = assetManager.list(path);
			if (!path.endsWith(File.separator))
				path = path + File.separator;
			soundFileListener.getfileList(fileNames);
			for (int i = 0; i < fileNames.length; i++) {
				soundFile = assetManager.openFd(path + fileNames[i]);
				soundPoolMap.put(i + 1, soundPool.load(soundFile, 1));
			}
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					flag = true;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * play (int soundID, float leftVolume, float rightVolume, int priority, int
	 * loop, float rate); 参数： 1、Map中取值 2、当前音量 默认为volume 3、最大音量 默认为volume 4、优先级
	 * 默认为1 5、重播次数默认为0 6、播放速度默认为1f
	 * @param soundtype
	 * @param loop
	 * @param left
	 * @param right
	 * @param rate
	 */
	public void soundPlay(int soundtype, int loop, float left, float right,
			float rate) {
		if (isFlag())
			if (soundPlayListener.playProperty(soundPool, soundPoolMap.get(soundtype), left, right, rate, volume)){
				CurPlay = soundPool.play(soundPoolMap.get(soundtype), volume * left, volume * right, 1, loop, rate);
				CurList.add(CurPlay);
			}
	}

	/**
	 * @param soundtype
	 * @param loop
	 * @param left
	 * @param right
	 */
	public void soundPlay(int soundtype, int loop, float left, float right) {
		soundPlay(soundtype, loop, volume * left, volume * right, 1f);
	}

	/**
	 * @param soundtype
	 * @param loop
	 * @param rate
	 */
	public void soundPlay(int soundtype, int loop, float rate) {
		soundPlay(soundtype, loop, 1, 1, rate);
	}

	/**
	 * @param soundtype
	 * @param loop
	 */
	public void soundPlay(int soundtype, int loop) {
		soundPlay(soundtype, loop, 1f);
	}

	/**
	 * @param soundtype
	 */
	public void soundPlay(int soundtype) {
		soundPlay(soundtype, 0);
	}
	
	
	/**
	 * 释放音效资源
	 * 
	 * @param soundtype
	 * @param left
	 * @param right
	 */
	public void BgsPlay(int soundtype, float left, float right) {
		if (CurPlay != 0)
			soundPool.stop(CurPlay);
		CurPlay = soundPool.play(soundPoolMap.get(soundtype), volume * left,
				volume * right, 1, -1, 1f);
	}

	public void stop() {
		soundPool.stop(CurPlay);
		CurList.remove(soundPool);
	}
	
	public void stopAll(){
		for(Integer cur :CurList){
			soundPool.stop(cur);
		}
		CurList.clear();
	}

	public void ClearBgs() {
		if (CurPlay != 0)
			soundPool.stop(CurPlay);
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public interface SoundPlayListener {
		/**
		 * 可以获取该值进行播放,可以修改查看参数,返回true代表播放默认值,返回true代表不播放默认的值;
		 * 其他属性代表当前设置的值,可以使用对应的soundPlay()方法来重新设置参数
		 * @param soundPool 可以使用该对象来播放自己的声音
		 * @param soundtype
		 * @param left
		 * @param right
		 * @param rate
		 * @param volume
		 */
		boolean playProperty(SoundPool soundPool, int soundtype, float left,
				float right, float rate, float volume);
	}

	public class SoundPlayImpl implements SoundPlayListener {
		@Override
		public boolean playProperty(SoundPool soundPool, int soundMapType,
				float left, float right, float rate, float volume) {
			return true;
		}
	}

	public interface SoundFileListener {
		void getfileList(String[] file);
	}

	public class SoundFileImpl implements SoundFileListener {
		@Override
		public void getfileList(String[] files) {
			Log.e("TAG", "文件名:\t");
			for (String file : files) {
				Log.e("TAG", file + "\t");
			}
		}
	}
}