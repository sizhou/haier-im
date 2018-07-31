package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.ReqAddFriend;

public interface IMFriendService {



    RespResult addFriendNoNeedAgree(ReqAddFriend imFriend);


    /***
     * 添加好友
     * @param imFriend
     * @return
     */
    RespResult addFriend(ReqAddFriend imFriend);

    /**
     * 是否同意成为好友
     *
     * @param usreId
     * @param sendImId
     * @param isAgree
     * @return
     */
    RespResult dealBeFriend(Long usreId, String sendImId, boolean isAgree);

    /**
     * 删除好友
     *
     * @param selfUserId
     * @param friendUserId
     * @return
     */
    RespResult remFriend(Long selfUserId, Long friendUserId);

    /**
     * 好友列表
     *
     * @param userId
     * @return
     */
    RespResult listFriends(Long userId);


    /**
     * 查询所有我请求的加好友的记录
     * @param userId
     * @return
     */
    RespResult listBeFriendsRequest(Long userId);



    /***
     * 修改好友信息（昵称）
     * @param userId
     * @param friendUserId
     * @param friendNickName
     * @return
     */
    RespResult updFriendNickName(Long userId,Long friendUserId,String friendNickName);

    /**
     * 判断两人是否好友
     *
     * @param userId
     * @param userId2
     * @return
     */
    boolean checkIsFriend(Long userId, Long userId2);

}
