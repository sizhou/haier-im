package com.haier.im.controller.reqvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class BeFriendListResp implements Serializable {

    @JsonIgnore
    private Long friendTabId;

    private Long friendUserId;

    private String nickName;//昵称

    @JsonIgnore
    private Integer friendType;

    private String portrait;//头像

    private String imId;//imId

    private boolean isFriend;//最终是否成为好友


}
