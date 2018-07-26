package com.haier.im.task.bean;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class MessageDO {
    private String chat_type;

    private MessagePayLoad payload;

    private String from;

    private String to;

    private String msg_id;
    @Expose
    private String timestamp;
    @Expose
    private String direction;

}
