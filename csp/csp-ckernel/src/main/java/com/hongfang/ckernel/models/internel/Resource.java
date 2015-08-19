package com.hongfang.ckernel.models.internel;

import java.util.logging.Logger;

import com.google.gdata.util.common.base.CharEscapers;

/**
 * Base class for the CSP model objects.
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Resource.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2014/7/19<P>
 */
public class Resource<T> {
	
    private static final Logger logger = Logger.getLogger(Resource.class.getName());

    protected String nullCheck(String id) {
        if(id == null || id.isEmpty()) {
            throw new RuntimeException("id cannot be null/empty");
        }
        return id;
    }
    
    protected String uri(String ... paths) {
    	StringBuilder strBuf = new StringBuilder();
    	for (String path : paths) {
    		//@deprecated
			//strBuf.append('/').append(CharEscapers.uriEscaper().escape(path));
    		strBuf.append('/').append(path);
    	}
    	return strBuf.toString();
    }
}
