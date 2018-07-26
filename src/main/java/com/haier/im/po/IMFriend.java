package com.haier.im.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.controller.reqvo.ReqAddFriend;
import lombok.Data;

@Data
public class IMFriend extends BasePo {

    private Long selfUserId;//在IMAccountInfo表中id
    private String selfNickName; //用户昵称
    private Long friUserId;//好友id
    private String friNickName;
    @JsonIgnore
    private  Integer friType;
    @JsonIgnore
    private boolean isAgree;//是否同意



    public IMFriend parseToAdd(IMFriend result, ReqAddFriend reqAddFriend){
        result.setSelfUserId(reqAddFriend.getSelfUserId());
        result.setFriNickName(reqAddFriend.getFriNickName());
        result.setFriUserId(reqAddFriend.getFriUserId());
        return  result;
    }


}
