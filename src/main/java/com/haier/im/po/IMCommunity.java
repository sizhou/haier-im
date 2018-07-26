package com.haier.im.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.controller.reqvo.ReqCommunity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IMCommunity extends BasePo {

    private String communityId;

    private String communityName; //群组名称

    private Long ownerUserId; //群主

    private String communityIcon; //群组头像

    private String communityNotice; //群公告

    private String communityDesc;//群描述

    private Integer affiliationsCount;//现有成员总数

    @JsonIgnore
    private Integer type;//预留字段

    @JsonIgnore
    private Integer communityStatus;//群组状态

    @JsonIgnore
    private Boolean communityPublic;//是否是公开群

    @JsonIgnore
    private Boolean allowinvites;//是否允许成员邀请

    @JsonIgnore
    private Integer maxusers;//群成员上限

    @JsonIgnore
    private Boolean membersonly;//加群是否需要群主或管理员审批

    @JsonIgnore
    private Boolean inviteNeedConfirm;//邀请时是否需要被邀请人同意


    public IMCommunity parseReqTo(IMCommunity result, ReqCommunity reqCommunity) {
        result.setCommunityId(reqCommunity.getCommunityId());
        result.setCommunityName(reqCommunity.getCommunityName());
        result.setOwnerUserId(reqCommunity.getOwnerUserId());
        result.setCommunityNotice(reqCommunity.getCommunityNotice());
        result.setCommunityDesc(reqCommunity.getCommunityDesc());
        result.setMembersonly(reqCommunity.getMembersonly());
        result.setMaxusers(reqCommunity.getMaxusers());
        result.setCommunityIcon(reqCommunity.getCommunityIcon());
        return result;
    }



}
