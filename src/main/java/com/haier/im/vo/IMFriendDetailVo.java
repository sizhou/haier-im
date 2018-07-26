package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class IMFriendDetailVo {

    @JsonIgnore
    private Long friendTabId;

    private Long friendUserId;

    private String nickName;//昵称

    @JsonIgnore
    private Integer friendType;

    private String portrait;//头像


    private String imId;//imId


//    TODO

}
