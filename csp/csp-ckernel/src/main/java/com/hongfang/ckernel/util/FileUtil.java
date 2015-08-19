package com.hongfang.ckernel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：FileUtil.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/8/3<P>
 */
public class FileUtil {
	public static String readIStoString(ServletInputStream is) throws IOException {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    if (is != null) {
	        StringBuilder sb = new StringBuilder();
	        String line;

	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	            while ((line = reader.readLine()) != null) {
	                sb.append(line).append("\n");
	            }
	        } finally {
	            is.close();
	        }
	        return sb.toString();
	    } else {
	        return "";
	    }
	}
}
