package com.taisheng.now.chat.websocket;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.taisheng.now.Constants;
import com.taisheng.now.bussiness.bean.post.HealthInfo;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketManager {

    private static WebSocketManager instance;

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
            instance.mUri = Constants.Url.WEB_SOCKET_URL;
            instance.mWebSocketFactory = new WebSocketFactory().setConnectionTimeout(DEFAULT_SOCKET_CONNECTTIMEOUT);

        }
        return instance;
    }


    private static final int DEFAULT_SOCKET_CONNECTTIMEOUT = 3000;
    private static final int DEFAULT_SOCKET_RECONNECTINTERVAL = 3000;
    private static final int FRAME_QUEUE_SIZE = 5;

    WebSocketListener mWebSocketListener;
    WebSocketFactory mWebSocketFactory;
    WebSocket mWebSocket;

    private ConnectStatus mConnectStatus = ConnectStatus.CONNECT_DISCONNECT;
    private Timer mReconnectTimer = new Timer();
    private TimerTask mReconnectTimerTask;

    private String mUri;

    public interface WebSocketListener {
        void onConnected(Map<String, List<String>> headers);
        void onTextMessage(String text);
    }

    public enum ConnectStatus {
        CONNECT_DISCONNECT,// 断开连接
        CONNECT_SUCCESS,//连接成功
        CONNECT_FAIL,//连接失败
        CONNECTING;//正在连接
    }



    public WebSocketManager(String deviceToken) {
        this(deviceToken, DEFAULT_SOCKET_CONNECTTIMEOUT);
    }

    public WebSocketManager(String deviceToken, int timeout) {
        mUri = Constants.Url.WEB_SOCKET_URL + deviceToken;
        mWebSocketFactory = new WebSocketFactory().setConnectionTimeout(timeout);
    }

    public void setWebSocketListener(WebSocketListener webSocketListener) {
        mWebSocketListener = webSocketListener;
    }

    public void connect() {
        try {
            mWebSocket = mWebSocketFactory.createSocket(mUri)
                    .setFrameQueueSize(FRAME_QUEUE_SIZE)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(new NVWebSocketListener())
                    .connect();  // 这里我改成了同步调用 异步调用请使用connectAsynchronously()
            setConnectStatus(ConnectStatus.CONNECTING);
        } catch (IOException e) {
            e.printStackTrace();
            reconnect();
        } catch (WebSocketException e) {
            e.printStackTrace();
            reconnect();
        }
    }

    // 客户端像服务器发送消息
    public void sendMessage(String message) {
        try {
//            JSONObject json = new JSONObject();
//            json.put("xxx", xxx);
//            json.put("xxx", xxx);
            synchronized (mWebSocket) {
                mWebSocket.sendText(message);
            }
            Log.e("longtianlove","发送消息"+message);

        } catch (Exception e) {
            Log.e("longtianlove","发送消息失败"+e.getMessage());
        }
    }

    private void setConnectStatus(ConnectStatus connectStatus) {
        mConnectStatus = connectStatus;
    }

    public ConnectStatus getConnectStatus() {
        return mConnectStatus;
    }

    public void disconnect() {
        if (mWebSocket != null) {
            mWebSocket.disconnect();
        }
        setConnectStatus(null);
    }


    public void reconnect() {
        if (mWebSocket != null && !mWebSocket.isOpen() && getConnectStatus() != WebSocketManager.ConnectStatus.CONNECTING) {
            mReconnectTimerTask = new TimerTask() {
                @Override
                public void run() {
                    connect();
                }
            };
            mReconnectTimer.schedule(mReconnectTimerTask, DEFAULT_SOCKET_RECONNECTINTERVAL);
        }
    }


// Adapter的回调中主要做三件事：1.设置连接状态2.回调websocketlistener3.连接失败重连
class NVWebSocketListener extends WebSocketAdapter {

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        System.out.println("OS. WebSocket onConnected");
        setConnectStatus(WebSocketManager.ConnectStatus.CONNECT_SUCCESS);
        if (mWebSocketListener != null) {
            mWebSocketListener.onConnected(headers);
        }
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);
        System.out.println("OS. WebSocket onConnectError");
        setConnectStatus(WebSocketManager.ConnectStatus.CONNECT_FAIL);
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        System.out.println("OS. WebSocket onDisconnected");
        setConnectStatus(WebSocketManager.ConnectStatus.CONNECT_DISCONNECT);
        reconnect();
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);
        System.out.println("OS. WebSocket onTextMessage");
        if (mWebSocketListener != null) {
            mWebSocketListener.onTextMessage(text);
        }
    }
}



}

