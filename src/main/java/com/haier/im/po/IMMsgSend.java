package com.haier.im.po;

import lombok.Data;

import java.util.Date;

@Data
public class IMMsgSend extends BasePo {

    private String messageId;

    private String messageDetail;

    private Integer messageType; //消息类型（1：文字 2：图片 3：地址 4：语音 5：视频 6：文件）

    private String senderId; //发送方id（用户imId）

    private String recverId;//收消息方(单用户imId或者群组id)

    private Integer sendType; //发送类型（1:群发 2:组发 3:密聊）

    private Date sendTime;

    private String direction;

    private String ext;

    private String addr;
    private String lng;
    private String lat;

}
