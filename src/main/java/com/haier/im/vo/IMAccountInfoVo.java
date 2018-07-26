package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.po.IMAccountInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IMAccountInfoVo extends IMAccountInfo {

    @JsonIgnore
    private String searchStr;//模糊搜索条件

    private boolean isFriend;//是否是好友

    private boolean isSelf=false;//是否是自己

    /**
     * po 转为 vo
     *
     * @param po
     * @return
     */
    public IMAccountInfoVo parsePoTo(IMAccountInfo po) {
        IMAccountInfoVo result = new IMAccountInfoVo();
        result.setUserId(po.getUserId())
                .setRealName(po.getRealName())
                .setCommunityRole(po.getCommunityRole())
                .setPortrait(po.getPortrait())
                .setPhone(po.getPhone())
                .setImType(po.getImType())
                .setImId(po.getImId())
                .setActivated(po.isActivated());
        return result;
    }
}
