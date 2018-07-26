package com.haier.im.controller.reqvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "文件实体")
public class ChatFileData implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "msgId")
    private String msgId;

    @ApiModelProperty(value = "发送者")
    private String from;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型（1：文字 2：图片 3：地址 4：语音 5：视频 6：文件）")
    private Integer fileType;

    @ApiModelProperty(value = "文件类型缩略图")
    private String fileTypeThum;

    @ApiModelProperty(value = "文件大小")
    private Integer fileSize;

    @ApiModelProperty(value = "下载路径")
    private String fileUrl;

    @ApiModelProperty(value = "创建时间")
    private String fileDate;
}
