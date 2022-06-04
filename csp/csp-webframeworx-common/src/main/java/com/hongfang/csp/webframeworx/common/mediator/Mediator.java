package com.hongfang.csp.webframeworx.common.mediator;
/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：平台協調網路需求作業<br>
 * 程 式 代 號 ：Mediator.java<br>
 * 描             述 ：負責 Web 平台網路請求的轉發管理<br>
 * 公             司 ：HongFang Innovation Technology.<br><br>
 * 【 資 料 來 源】  ：<br>
 * 【 輸 出 報 表】  ：<br>
 * 【 異 動 紀 錄】  ：<br>
 *
 * @author : Wong Ye Fang <br>
 * @version : 1.0.0 2022/06/04<P>
 */
public abstract class Mediator {
    public abstract void register(MediatorType type, Colleague colleague);
    public abstract void relay(MediatorType type, String msg);                                                // 轉發
}
