package net.tatans.coeus.network.josn.analysis;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

@SuppressWarnings("unchecked")
@SuppressLint("DefaultLocale")
public class JsonDeepUtil {

	/**
	 * 使用该方法可以将json字符串转为一个实体类
	 * 
	 * @param jsonStr
	 * @param c
	 * @return
	 * @throws JSONException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> T[] getEntity(String jsonStr, Class<T> c) throws JSONException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		T[] array = (T[]) Array.newInstance(c, 1);
		if (jsonStr.charAt(0) == '[') {
			JSONArray jsonArray = new JSONArray(jsonStr);
			return getEntity(jsonArray, c);
		} else if (jsonStr.charAt(0) == '{') {
			JSONObject jsonObject = new JSONObject(jsonStr);
			array[0] = getEntity(jsonObject, c);
		}
		return array;
	}

	/**
	 * @param jsonStr
	 * @param c
	 * @return
	 * @throws JSONException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> T getEntityJson(String jsonStr, Class<T> c) throws JSONException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		JSONObject jsonObject = new JSONObject(jsonStr);
		return getEntity(jsonObject, c);
	}

	/**
	 * 使用该方法可以将json格式转为一个实体类
	 * 
	 * @param jsonObject
	 * @param c
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> T getEntity(JSONObject jsonObject, Class<T> c) throws InstantiationException, IllegalAccessException, JSONException,
			IllegalArgumentException, InvocationTargetException {
		T t = c.newInstance();
		Field[] fs = c.getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			String key = f.getName();
			Type keyType = f.getGenericType();
			String keyU = key.replaceFirst(key.substring(0, 1), key.substring(0, 1).toUpperCase());
			Method[] methods = c.getDeclaredMethods();
			for (Method method : methods) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == 1 && method.getName().equals("set" + keyU)) {
					Class<?> clazz = clazzs[0];
					LoggerJsonMsg.e("方法名：" + method.getName() + "类型名：" + clazz);
					boolean lean = clazz.isArray();
					if (clazz.equals(keyType)) {
						// Object obj = jsonObject.get(key);
						Object obj = getValue(key, jsonObject, clazz);
						if (obj != null) {
							if (obj instanceof JSONArray) {
								JSONArray jsonArray = (JSONArray) obj;
								int len = jsonArray.length();
								Class<?> clazs = clazz.getComponentType();
								Object[] array = (Object[]) Array.newInstance(clazs, len);
								for (int i = 0; i < len; i++) {
									array[i] = getEntity(jsonArray.getJSONObject(i), clazs);
								}
								method.invoke(t, new Object[] { array });
							} else if (obj instanceof JSONObject) {
								if (lean) {
									Class<?> clazs = clazz.getComponentType();
									Object[] array = (Object[]) Array.newInstance(clazs, 1);
									array[0] = getEntity((JSONObject) obj, clazz);
									method.invoke(t, new Object[] { array });
								} else {
									method.invoke(t, getEntity((JSONObject) obj, clazz));
								}
							} else {
								LoggerJsonMsg.e("方法名：" + method.getName() + "类型名：" + clazz + "返回值" + obj);
								method.invoke(t, obj);
							}
						}
					}
				}
			}
		}
		return t;
	}

	/**
	 * 使用该方法可以将json数组转为一个实体类
	 * 
	 * @param jsonArray
	 * @param c
	 * @return
	 * @throws JSONException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public <T> T[] getEntity(JSONArray jsonArray, Class<T> c) throws JSONException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		int len = jsonArray.length();
		T[] array = (T[]) Array.newInstance(c, len);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObjectArray = jsonArray.getJSONObject(i);
			array[i] = getEntity(jsonObjectArray, c);
		}
		return array;
	}

	/**
	 * 遍历json中的key值，让其与实体类属性名匹配,匹配成功则取出值
	 */
	private <E> E getValue(String key, JSONObject jsonObject, Class<E> clazz) throws JSONException {
		LoggerJsonMsg.v("属性名：" + key + "类型：" + clazz);
		// Object result = jsonObject.get(key);
		if (clazz.equals(String.class)) {
			return (E) jsonObject.getString(key);
		} else if (clazz.equals(int.class)) {
			Integer result = jsonObject.getInt(key);
			return (E) result;
		} else if (clazz.equals(long.class)) {
			Long result = jsonObject.getLong(key);
			return (E) result;
		} else if (clazz.equals(boolean.class)) {
			Boolean result = jsonObject.getBoolean(key);
			return (E) result;
		} else if (clazz.equals(double.class)) {
			Double result = jsonObject.getDouble(key);
			return (E) result;
		} else {
			LoggerJsonMsg.e("属性名：" + key);
			try {
				Object result = jsonObject.get(key);
				LoggerJsonMsg.e("数组值：" + result.toString());
				return (E) result;
			} catch (JSONException e) {
				LoggerJsonMsg.e("属性错误：在json字符串中没有对用的json格式" + key);
				return null;
			}
		}
	}
}
