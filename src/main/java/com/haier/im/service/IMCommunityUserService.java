package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.ReqCommunityOper;
import com.haier.im.vo.ImCommunityUserVo;
import org.apache.ibatis.annotations.Param;

public interface IMCommunityUserService {

    /**
     * 用户在某个群组中的信息
     *
     * @param communityId
     * @param userId
     * @return
     */
    ImCommunityUserVo getUserInOfCommunity(String communityId, Long userId);

    RespResult updUserNickName(@Param("communityId") String communityId, @Param("userId") Long userId, @Param("nickName") String nickName);

    /**
     * 邀请入群
     *
     * @param oper
     * @return
     */
    RespResult invitMember(ReqCommunityOper oper);

    RespResult remvMemberByOwner(ReqCommunityOper oper);

    /**
     * 主动加群
     *
     * @param communityId
     * @param joinUserId
     * @return
     */
    RespResult joinCommunity(String communityId, Long joinUserId);

    /**
     * 退群
     *
     * @param communityId
     * @param userId
     * @return
     */
    RespResult leaveCommunity(String communityId, Long userId);


    /**
     * 判断用户是否在环信的某个群组中
     *
     * @param imId
     * @param groupId
     * @return
     */
    boolean checkUserInEasemoGroup(String imId, String groupId);

}
