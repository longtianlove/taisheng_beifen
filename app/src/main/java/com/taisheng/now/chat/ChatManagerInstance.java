package com.taisheng.now.chat;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.application.SampleAppLike;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.chat.websocket.WebSocketManager;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.yuyin.util.Constant;
import com.taisheng.now.yuyin.util.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(60000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    webSocketManager.sendMessage(",heartbeat," + UserInstance.getInstance().getUid());
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
                            historyBean.doctorAvator = Constants.Url.File_Host + rawRemoteMessage.avatar;
                            historyBean.doctorName = rawRemoteMessage.user_name;
                            if (!"2".equals(rawRemoteMessage.message_type)) {
                                //不是手表消息才加入到聊天历史记录当中
                                MLOC.addHistory(historyBean, false);
                            }


                            MessageBean messageBean = new MessageBean();
                            messageBean.setConversationId(message.fromId);
                            messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
                            messageBean.setMsg(message.contentData);
                            messageBean.setFromId(message.fromId);


                            if (!"2".equals(rawRemoteMessage.message_type)) {
                                //如果是医生来的消息
                                MLOC.saveMessage(messageBean);
                                EventManage.AEVENT_C2C_REV_MSG MSG = new EventManage.AEVENT_C2C_REV_MSG();
                                MSG.message = message;
                                EventBus.getDefault().post(MSG);
                            } else {
                                String conntent = messageBean.msg;
                                if (conntent.startsWith("audio[") && conntent.endsWith("]")) {
                                    conntent = conntent.replace("audio[", "");
                                    conntent = conntent.replace("]", "");
                                    String[] temp = conntent.split(",");

                                    String seconds = temp[0];
                                    int secondstemp = Integer.parseInt(seconds);
                                    String filePath = temp[1];
                                    String isSendFail = temp[2];
                                    if (!TextUtils.isEmpty(filePath)) {
                                        //如果是手表来的消息,先下载
                                        ApiUtils.getApiService().downloadAudioFileWithDynamicUrlAsync(Constants.Url.AUDIO_HOST + filePath).enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {


                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String audioFilePath = writeFileToSDCard(response.body());
                                                            if (!TextUtils.isEmpty(audioFilePath)) {
//                                                        audio[2,/storage/emulated/0/wxr/record/584f2a5b-3609-4e27-9f36-27d5be461867.amr,1]
                                                                String content = "audio[" + secondstemp + "," + audioFilePath + ",1]";
                                                                messageBean.msg = content;
                                                                message.contentData = content;
                                                                MLOC.saveMessage(messageBean);
                                                                EventManage.Watch_AEVENT_C2C_REV_MSG MSG = new EventManage.Watch_AEVENT_C2C_REV_MSG();
                                                                MSG.message = message;
                                                                EventBus.getDefault().post(MSG);
                                                            }
                                                        }
                                                    }).start();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }
                                }

                            }
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


    private String writeFileToSDCard(ResponseBody body) {
        try {

            //获取录音保存位置
            String mDirString = FileUtils.getAppRecordDir(SampleAppLike.mcontext).toString();
            File dir = new File(mDirString);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileNameString = generalFileName();
            File futureStudioIconFile = new File(dir, fileNameString);
//            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("taisheng_file", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return futureStudioIconFile.getAbsolutePath();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * 随机生成文件的名称
     *
     * @return
     */
    private String generalFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }
}
