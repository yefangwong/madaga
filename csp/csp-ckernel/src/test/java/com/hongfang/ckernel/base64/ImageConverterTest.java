/**
 * Copyright (c) 2018 Cornelius.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Cornelius. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of license agreement you entered
 * into with Cornelius.
 */
package com.hongfang.ckernel.base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hongfang.ckernel.image.ImageComparator;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import junit.framework.Assert;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：ImageConverterTest.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : Mark Wong <br>
 * @version : 1.0.0 Dec 15, 2018
 *          <P>
 */
public class ImageConverterTest {
	String fileName = "2017苗栗陶Banner-881x204px(1).jpg";
	static List<String> dumpStorage = new ArrayList<String>();
	
	@Test
	public void executeImageToString() throws IOException {
		System.out.println("executeImageToString");
		ClassLoader classLoader = getClass().getClassLoader();
		String filePath = 
				java.net.URLDecoder.decode(classLoader.getResource(fileName).getFile(), "UTF-8");
		File f = new File(filePath);
		if (f.exists()) {
			System.out.println(String.format("file name=%s, size=%s", 
					f.getName(), f.length()));
			String encodedImageString = convertImage2String(f);
			dumpStorage.add(encodedImageString);
			System.out.println(encodedImageString);
		} else {
			System.out.println("file not exists.");
		}
	}
	
	@Test
	public void executeStringToImage() throws Exception{
		System.out.println("executeStringToImage");
		if (this.dumpStorage.isEmpty()) {
			throw new Exception("encoded string can not be null.");
		}
		byte[] imgBytes = Base64.decode(dumpStorage.get(0));
		System.out.println("size=" + imgBytes.length);
		String outEncImgFileName = new StringBuffer(fileName).append(".enc").toString();
		Files.write(Paths.get("src/test/resources").resolve(new File(outEncImgFileName).toPath()),
				imgBytes);
		// 1.equal size
		File encImgFile = new File("src/test/resources/" + outEncImgFileName);
		File srcImgFile = new File("src/test/resources/" + fileName);
		long encImgSize = encImgFile.length();
		long srcImgSize = srcImgFile.length();
		System.out.println("enc img size=" + encImgFile.length());
		System.out.println("src img size=" + srcImgFile.length());

		Assert.assertEquals(encImgSize, srcImgSize);
		// 2.equal content
		// compare by RGB color 2-D array.
		// TODO 
		//Assert.assertEquals(true, ImageComparator.compare(srcImgFile, encImgFile));
	}
	
	private String convertImage2String(File f) throws IOException {
		return Base64.encode(Files.readAllBytes(f.toPath()));
	}

}
