package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.po.IMCommunity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class IMCommunityVo extends IMCommunity {

    @JsonIgnore
    private Long modUserId;

    @JsonIgnore
    private String searchStr;//模糊查询条件

    private boolean isMembered;//是否是群成员



    //群成员
    private Map<String,Object> members = new HashMap<>();//群成员

    @JsonIgnore
    private Integer conditionLimit;//要求结果集总数

    @JsonIgnore
    private String orderClause;//排序规则
    //TODO


    public IMCommunityVo parsePoTo(IMCommunityVo result,IMCommunity po){
        result.setCommunityId(po.getCommunityId());
        result.setCommunityName(po.getCommunityName());
        result.setOwnerUserId(po.getOwnerUserId());
        result.setCommunityNotice(po.getCommunityNotice());
        result.setCommunityDesc(po.getCommunityDesc());
        result.setMembersonly(po.getMembersonly());
        result.setMaxusers(po.getMaxusers());
        result.setCommunityIcon(po.getCommunityIcon());
        result.setAffiliationsCount(po.getAffiliationsCount());
        result.setAllowinvites(po.getAllowinvites());
        result.setCommunityPublic(po.getCommunityPublic());
        result.setInviteNeedConfirm(po.getInviteNeedConfirm());
        return result;
    }


}
