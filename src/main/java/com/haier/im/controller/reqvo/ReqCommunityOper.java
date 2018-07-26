package com.haier.im.controller.reqvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReqCommunityOper {

    @ApiModelProperty(value = "操作者Id", required = true)
    private Long operUserId;//操作者

    @ApiModelProperty(value = "被操作者Id", required = true)
    private Long operedUserId;//被操作者

    @ApiModelProperty(value = "群id", required = true)
    private String communityId;//被操作的群

    @ApiModelProperty(value = "昵称（改个人昵称时是必填）", required = false)
    private String operedNickName;

}
