package common.enums;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：IMsgCodeEnums.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2015/11/04<P>
 */
public interface IMsgCodeEnums {

    /**
     * 代碼
     * @return
     */
    public String getCode();

    /**
     * 對應訊息參數檔(properties key)
     *  
     * @return
     */
    public String getPropertiesKey();
}