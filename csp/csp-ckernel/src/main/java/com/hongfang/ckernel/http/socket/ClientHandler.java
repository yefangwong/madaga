package com.hongfang.ckernel.http.socket;

import com.hongfang.csp.webframeworx.common.mediator.Colleague;
import com.hongfang.csp.webframeworx.common.mediator.Mediator;
import com.hongfang.csp.webframeworx.common.mediator.MediatorType;

import java.awt.image.MemoryImageSource;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：多 Socket Client 回應作業<br>
 * 程 式 代 號 ：ClientHandler.java<br>
 * 描             述 ：將平台 Socket 封裝起來，用來做多 Socket 的回應<br>
 * 公             司 ：HongFang Innovation Technology.<br><br>
 * 【 資 料 來 源】  ：<br>
 * 【 輸 出 報 表】  ：<br>
 * 【 異 動 紀 錄】  ：<br>
 *
 * @author : Wong Ye Fang <br>
 * @version : 1.0.0 2022/06/04<P>
 */
public class ClientHandler implements Runnable {
    private String id;
    private Socket client;
    private BufferedReader in;
    private DataOutputStream out;
    private List<ClientHandler> clients;
    private Mediator mediator;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients, Mediator mediator) throws IOException {
        this.client = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.clients = clients;
        this.mediator = mediator;
    }

    @Override
    public void run() {
        String clientSentence;
        String relayMessage = "";

        try {
            while(true) {
                while (null != (clientSentence = in.readLine())) {                                               // 不是每個 client 都要發命
                    this.id = clientSentence.substring(clientSentence.indexOf("type = "),
                            clientSentence.indexOf(","));
                    System.out.println("[SERVER] get relay message from client: " + clientSentence);

                    // TODO some process: choose the nearest neighborhood
                    relayMessage = clientSentence;
                }
                if (relayMessage != null && !relayMessage.isEmpty()) {
                    if (relayMessage.contains("SERVICE")) {
                        for (ClientHandler clientHandler : clients) {
                            if (clientHandler.id != null && clientHandler.id.contains("SERVICE")) {
                                //mediator.relay(MediatorType.SERVICE, relayMessage);
                                clientHandler.out.writeUTF("[SERVER] relay message chat client: " + relayMessage + "\n");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[SERVER] IOException in client handler: " + e.getMessage());
            System.err.println(e.getStackTrace());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
