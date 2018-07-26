package com.haier.im.controller.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReqCommunity {

    @ApiModelProperty(value = "群组Id", required = true)
    private String communityId; //群Id

    @ApiModelProperty(value = "群主id", required = true)
    private Long ownerUserId; //群主id

    @ApiModelProperty(value = "群组名称", required = false)
    private String communityName; //群组名称

    @ApiModelProperty(value = "群组头像", required = false)
    private String communityIcon; //群组头像

    @ApiModelProperty(value = "群公告", required = false)
    private String communityNotice; //群公告

    @ApiModelProperty(value = "群组名称", required = false)
    private String communityDesc;//群描述

    @ApiModelProperty(value = "是否需要批准", required = false)
    private Boolean membersonly;//是否需要批准

    @ApiModelProperty(value = "最大成员数", required = false)
    private Integer maxusers;//最大成员数

    @ApiModelProperty(value = "是否是公开群", required = false)
    private Boolean communityPublic = true;//是否是公开群

    @ApiModelProperty(value = "是否允许成员邀请", required = false)
    private Boolean allowinvites = true;//是否允许成员邀请


}
