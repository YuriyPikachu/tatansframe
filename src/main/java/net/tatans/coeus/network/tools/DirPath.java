package net.tatans.coeus.network.tools;

import java.io.File;

import android.os.Environment;

public class DirPath {
	/**
	 * 获取或创建Cache目录
	 * 
	 * @param bucket
	 *         文件保存路径
	 * @param 
	 * 		保存文件的名字
	 */			
	public static String getMyCacheDir(String sDir,String apkName) {
		String dir;

		// 保证目录名称正确
		if (sDir != null) {
			if (!sDir.equals("")) {
				if (!sDir.endsWith("/")) {
					sDir = sDir + "/";
				}
			}
		}

		String joyrun_default = "/tatans/";

		if (isSDCardExist()) {
			dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + sDir;
		} else {
			dir = Environment.getDownloadCacheDirectory().toString() + joyrun_default + sDir;
		}

		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		return dir+apkName;
	}
	
	public static boolean isSDCardExist() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}
}
