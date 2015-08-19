package com.hongfang.ckernel.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：JsonUtil.java<br>
 * 描             述 ：Json 格式使用元件<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/3/4<P>
 */
public final class JsonUtil {
	
	public static String toJson(Object obj) {
		Gson g = initGson();
		return g.toJson(obj);
	}
	
	public static <T> T toObjectList(String json, Type collectionType) {
		T obj = initGson().fromJson(json, collectionType);
		return obj;
	}
	
	public static <T> T toObject(String json, Class<T> clazz) {
		T obj = initGson().fromJson(json, clazz);
		return obj;
	}

	public static boolean isJSONValid(String test) {
	      try {
	          initGson().fromJson(test, Object.class);
	          return true;
	      } catch(com.google.gson.JsonSyntaxException ex) { 
	          return false;
	      }
	}
	
	private static Gson initGson() {
		Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		return g;
	}
}
