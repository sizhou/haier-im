package com.haier.im.controller.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
public class MessageData implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "msg_id")
    private String msg_id;

    @ApiModelProperty(value = "消息内容（消息类型是文字的时候）")
    private String msgDetail;

    @ApiModelProperty(value = "消息类型（1：文字 2：图片 3：地址 4：语音 5：视频 6：文件）")
    private int msgType;

    @ApiModelProperty(value = "发送者ImId")
    private String from;

    @ApiModelProperty(value = "接收ImId")
    private String to;

    @ApiModelProperty(value = "发送类型（1:群发 2:组发 3:密聊）")
    private String send_type;

    @ApiModelProperty(value = "发送时间")
    private Date send_time;

    @ApiModelProperty(value = "地址（消息类型是地址的时候）")
    private String addr;

    @ApiModelProperty(value = "维度（消息类型是地址的时候）")
    private String lat;

    @ApiModelProperty(value = "经度（消息类型是地址的时候）")
    private String lng;

    @ApiModelProperty(value = "文件大小（消息类型是文件，图片，视频，语音的时候")
    private int file_length;

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "图片/视频缩略图尺寸")
    private String size;

    @ApiModelProperty(value = "语音/视频时长")
    private int length;

    @ApiModelProperty(value = "thumb_secret")
    private String thumb_secret;

    @ApiModelProperty(value = "视频缩略图远程地址")
    private String thumb_url;

    @ApiModelProperty(value = "视频缩略图远程OSS地址")
    private String thumbUrlOss;

    @ApiModelProperty(value = "访问密码")
    private String secret;

    @ApiModelProperty(value = "访问地址")
    private String url;

    @ApiModelProperty(value = "访问OSS地址")
    private String urlOss;

    @ApiModelProperty(value = "direction")
    private String direction;

    @ApiModelProperty(value = "消息扩展字段")
    private Map<String, Object> ext;

    @ApiModelProperty(value = "发送者群组昵称")
    private String nickname;

    @ApiModelProperty(value = "发送者真实姓名")
    private String realName;


    @ApiModelProperty(value = "发送者头像")
    private String portrait;
}
