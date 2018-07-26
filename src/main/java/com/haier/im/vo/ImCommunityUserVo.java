package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.po.IMCommunityUser;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Data
public class ImCommunityUserVo {

    @JsonIgnore
    private Long id;
    private String communityId;
    private Integer accountType;//账户类型（1：群主，2：管理员，3：普通群成员）
    private String nickName;
    private Long userId;//IMAccountInfo中id
    @JsonIgnore
    private Integer state;//状态
    private Boolean inviteAuth;//是否有权邀请别人

    public IMCommunityUser parseVoTo(IMCommunityUser result, ImCommunityUserVo vo) {
        result.setCommunityId(vo.getCommunityId())
                .setAccountType(vo.getAccountType())
                .setNickName(vo.getNickName()).
                setUserId(vo.getUserId())
                .setInviteAuth(vo.getInviteAuth())
                .setState(vo.getState());
        return result;
    }


    private String portrait;//用户头像
    private String imId;//用户imId
    //TODO


    public ImCommunityUserVo parsePoTo(ImCommunityUserVo result, IMCommunityUser po) {
        try {
            BeanUtils.copyProperties(result, po);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}
