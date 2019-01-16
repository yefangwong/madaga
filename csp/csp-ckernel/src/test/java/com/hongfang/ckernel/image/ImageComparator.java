package com.hongfang.ckernel.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageComparator {

	public static boolean compare(File srcImgFile, File encImgFile) {
		BufferedImage srcImg = null;
		try {
			srcImg = ImageIO.read(srcImgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage encImg = null;
		try {
			encImg = ImageIO.read(encImgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compareImage(srcImg, encImg);
	}
	
	private static boolean compareImage(BufferedImage src, BufferedImage enc) {
		boolean result = false;
		System.out.println("img width:" + src.getWidth());
		System.out.println("img height:" + src.getHeight());
		System.out.println("img x:" + src.getMinX());
		System.out.println("img y:" + src.getMinY());
		String[][] srcRGBArray = new String[src.getWidth()][src.getHeight()];
		for (int x = src.getMinX(); x < src.getWidth(); x++) {
			for (int y = src.getMinY(); y < src.getHeight(); y++) {
				srcRGBArray[x][y] = 
						new StringBuilder(src.getRGB(x, y)).append(",").toString();
			}
		}
		printArray(srcRGBArray);
		return result;
	}
	
	private static void printArray(String[][] arry) {
		for (int i=0; i<arry.length; i++) {
			for (int j=0; j<arry.length; j++) {
				System.out.println(arry[i][j]);
			}
		}
	}

}
