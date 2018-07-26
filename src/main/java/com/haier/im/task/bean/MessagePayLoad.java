package com.haier.im.task.bean;

import lombok.Data;

import java.util.List;

@Data
public class MessagePayLoad {
    private String from;

    private String to;

    private MessageExt ext;

    private List<MessageBody> bodies;
}
