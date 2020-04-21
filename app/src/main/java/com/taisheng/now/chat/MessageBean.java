package com.taisheng.now.chat;

public class MessageBean {
    public int id;
    public String conversationId;//聊天的对象，比如一个医生或一个手表，当前对象的聊天内容都放在同一个conversationId
    public String fromId;//聊天内容来源
    public String msg;
    public String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String lastTime) {
        this.time = lastTime;
    }
}
