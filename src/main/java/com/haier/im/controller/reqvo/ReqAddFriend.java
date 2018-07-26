package com.haier.im.controller.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReqAddFriend {


    @ApiModelProperty(value = "用户id", required = true)
    private Long selfUserId;

    @ApiModelProperty(value = "好友id", required = true)
    private Long friUserId;

    @ApiModelProperty(value = "好友昵称（修改好友昵称时必填）", required = false)
    private String friNickName;


}
