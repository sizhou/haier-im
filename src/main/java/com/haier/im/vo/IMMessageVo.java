package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.po.IMMsgSend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class IMMessageVo {

    @JsonIgnore
    private Long id;

    private String messageId;

    private String messageDetail;

    private Integer messageType; //消息类型（1：文字 2：图片 3：地址 4：语音 5：视频 6：文件）

    private String senderId; //发送方id（单用户id或者群组id）

    private String recverId;//收消息方(单用户id或者群组id)

    private Integer sendType; //发送类型（1:群发 2:组发 3:密聊）

    private Date sendTime;

    private String direction;

    private String addr;
    private String lng;
    private String lat;


    private String ext;

    private Integer pageSize;

    @JsonIgnore
    private Integer startRow;

    private Integer pageNo;

    @JsonIgnore
    private String startDate;

    @JsonIgnore
    private String endDate;

    //用户
    private String messageUserId;//聊天的另一方（群组或者用户）

    private Long userId;//当前用户Id

    private String senderNickName;//发送者昵称（好友昵称/群组中的昵称）

    private String senderPortrait;//发送者用户头像

    private String recvNickName;//接受者昵称

    private String recvPortrait;//接收者用户头像


    public IMMessageVo parsePoTo(IMMessageVo result, IMMsgSend po) {
        result.setMessageId(po.getMessageId()).setMessageDetail(po.getMessageDetail())
                .setMessageType(po.getMessageType()).setSenderId(po.getSenderId())
                .setRecverId(po.getRecverId()).setSendType(po.getSendType())
                .setSendTime(po.getSendTime()).setDirection(po.getDirection())
                .setAddr(po.getAddr()).setLat(po.getLat()).setLng(po.getLng())
                .setExt(po.getExt());
        return result;
    }
}
