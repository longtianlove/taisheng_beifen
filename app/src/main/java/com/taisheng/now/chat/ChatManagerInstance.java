package com.taisheng.now.chat;

import android.util.JsonReader;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.bussiness.bean.post.HealthInfo;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.chat.websocket.WebSocketManager;
import com.taisheng.now.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ChatManagerInstance {

    private static ChatManagerInstance instance;

    private ChatManagerInstance() {
    }

    public static ChatManagerInstance getInstance() {
        if (instance == null) {
            instance = new ChatManagerInstance();

        }
        return instance;
    }

    public void init() {
        MLOC.init(SampleAppLike.mcontext);
        initFree();
    }


    private void initFree() {
        MLOC.d("KeepLiveService", "initFree");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 收到消息回调
                WebSocketManager webSocketManager = WebSocketManager.getInstance();
                webSocketManager.setWebSocketListener(new WebSocketManager.WebSocketListener() {
                    @Override
                    public void onConnected(Map<String, List<String>> headers) {
                        Log.e("longtianlove", "websocket-connect");
                        webSocketManager.sendMessage(",mobilefh-join," + UserInstance.getInstance().getUid());
                       Thread thread= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(60000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    webSocketManager.sendMessage(",heartbeat,"+UserInstance.getInstance().getUid());
                                }
                            }
                        });
                        thread.start();
                    }

                    @Override
                    public void onTextMessage(String text) {
                        Log.e("longtianlove", "收到消息:" + text);

                        try {
                            RawRemoteMessage rawRemoteMessage = JSON.parseObject(text, RawRemoteMessage.class);
                            RemoteChatMessage message = new RemoteChatMessage();
                            message.fromId = rawRemoteMessage.send_id;
                            message.contentData = rawRemoteMessage.content;

                            HistoryBean historyBean = new HistoryBean();
                            historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
                            historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
                            historyBean.setLastMsg(message.contentData);
                            historyBean.setConversationId(message.fromId);
                            historyBean.setNewMsgCount(1);
                            historyBean.doctorAvator= Constants.Url.File_Host+rawRemoteMessage.avatar;
                            historyBean.doctorName=rawRemoteMessage.user_name;
                            MLOC.addHistory(historyBean, false);

                            MessageBean messageBean = new MessageBean();
                            messageBean.setConversationId(message.fromId);
                            messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
                            messageBean.setMsg(message.contentData);
                            messageBean.setFromId(message.fromId);
                            MLOC.saveMessage(messageBean);

                            EventManage.AEVENT_C2C_REV_MSG MSG = new EventManage.AEVENT_C2C_REV_MSG();
                            MSG.message = message;
                            EventBus.getDefault().post(MSG);
                        } catch (Exception e) {
                            Log.e("longtianlove", "消息解析失败" + e.getMessage());
                        }

                    }
                });
                webSocketManager.connect();
            }
        });

        thread.start();


        MLOC.userId = UserInstance.getInstance().getUid();
    }
}
