/**
 * Copyright (c) 2014 Cornelius.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Cornelius. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of license agreement you entered
 * into with Cornelius.
 */
package com.hongfang.ckernel.models.internel;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：ClazzUtil.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/19<P>
 */
public class ClazzUtil {
   public static Class getClaz(String objType){
        String pkg = "com.hongfang.ckernel.models.";
        try {
            return Class.forName(pkg + toCamelCase(objType));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Unknown obj type " + objType,ex);
        }
   }

   public static String toCamelCase(String name) {
       return toCamelCase(name.split("_"));
   }

   public static String toCamelCase(String[] parts) {
       StringBuilder buff = new StringBuilder();
       for (String part : parts) {
           if (part.isEmpty()) {
               continue;
           }
           buff.append(Character.toUpperCase(part.charAt(0)));
           buff.append(part.substring(1));
       }
       return buff.toString();
   }
}
