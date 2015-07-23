package net.tatans.coeus.network.speaker;

/**
 * speaker 回调
 * @author 周焕 
 *
 */
public abstract class Callback{
	public void onDone(){};
	
	public void onError(){};
	
	public void onStart(){};
}