package com.hongfang.csp.webframeworx.common.mediator;
/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：平台基礎抽象同事類別<br>
 * 程 式 代 號 ：Colleague.java<br>
 * 描             述 ：抽象同事類別，可以作為平台後台團隊成員基礎類別<br>
 * 公             司 ：HongFang Intelligent Technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Wong Ye Fang <br>
 * @version  : 1.0.0 2022/06/04<P>
 */
public abstract class Colleague implements Runnable {
    protected Mediator mediator;
    public void setMedium(Mediator mediator) {
        this.mediator = mediator;
    }
    public abstract void receive(String msg);
    public abstract void send(MediatorType type, String msg);
}
