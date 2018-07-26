package com.haier.im.task.bean;

import lombok.Data;

@Data
public class MessageBody {
    /**
     * 视频文件名称
     */
    private String filename;

    /**
     * secret在上传文件后会返回
     */
    private String secret;

    private String type;

    /**
     * 上传图片消息地址，在上传图片成功后会返回UUID
     */
    private String url;

    private String msg;

    /**
     * 要发送的地址
     */
    private String addr;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lng;

    /**
     * 图片附件大小（单位：字节）
     */
    private Integer file_length;

    /**
     * 图片尺寸
     */
    private String size;

    /**
     * 语音时间（单位：秒）
     */
    private Integer length;

    /**
     * 上传视频缩略图远程地址，在上传视频缩略图后会返回UUID
     */
    private String thumb;

    /**
     * secret在上传缩略图后会返回
     */
    private String thumb_secret;

}
