package com.haier.im.dao;

import com.haier.im.po.IMFriend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMFriendMapper {

    /**
     * 新增好友
     *
     * @param friend
     * @return
     */
    int addFriend(IMFriend friend);

    /**
     * 是否同意成为好友
     *
     * @param friUserId
     * @param isAgree
     * @return
     */
    int agreeBeFriend(@Param("friUserId") Long friUserId, @Param("selfUserId") Long selfUserId, @Param("isAgree") boolean isAgree);


    /**
     * 删除好友
     *
     * @param selfUserId
     * @param friUserId
     * @return
     */
    int remvFriend(@Param("selfUserId") Long selfUserId, @Param("friUserId") Long friUserId);

    /**
     * 查询主动加的好友
     *
     * @param selfUserId
     * @return
     */
    List<IMFriend> listFriendsBySelfId(@Param("selfUserId") Long selfUserId);

    /**
     * 查询被加为好友的好友
     *
     * @param friUserId
     * @return
     */
    List<IMFriend> listFriendsByFriId(@Param("friUserId") Long friUserId);

    /**
     * 修改用户好友信息
     *
     * @param friend
     * @return
     */
    int updFriendBy(IMFriend friend);

    IMFriend findFriendById(@Param("id") Long id);

    IMFriend findFriendshipBy(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * 判断是否是被邀请成为好友
     * @param sendUserId
     * @param selfUserId
     * @return
     */
    IMFriend checkInvitedBeFriend(@Param("sendUserId") Long sendUserId, @Param("selfUserId") Long selfUserId);


}
