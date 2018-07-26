package com.haier.im.easemob.api;

public interface ChatGroupAPI {
    /**
     * 创建一个群组 <br>
     * POST
     *
     * @param payload
     *            <code>{"groupname":"testrestgrp12","desc":"server create group","public":true,"maxusers":300,"approval":true,"owner":"jma1","members":["jma2","jma3"]}</code>
     * @return
     */
    Object createChatGroup(Object payload);

    /**
     * 修改群组信息 <br>
     * PUT
     *
     * @param groupId
     *            群组标识
     * @param payload
     *            <code>{"groupname":"testrestgrp12",description":"update groupinfo","maxusers":300}</code>
     * @return
     */
    Object modifyChatGroup(String groupId, Object payload);

    /**
     * 删除群组 <br>
     * DELETE
     *
     * @param groupId
     *            群组标识
     * @return
     */
    Object deleteChatGroup(String groupId);

    /**
     * 群组加人[单个] <br>
     * POST
     *
     * @param groupId
     *            群组标识
     * @param userId
     *            用户ID或用户名
     * @return
     */
    Object addSingleUserToChatGroup(String groupId, String userId);

    /**
     * 群组加人[批量] <br>
     * POST
     *
     * @param groupId
     *            群组标识
     * @param payload
     *            用户ID或用户名，数组形式
     * @return
     * @see com.easemob.server.example.comm.body.UserNamesBody
     */
    Object addBatchUsersToChatGroup(String groupId, Object payload);

    /**
     * 群组减人[单个] <br>
     * DELETE
     *
     * @param groupId
     *            群组标识
     * @param userId
     *            用户ID或用户名
     * @return
     */
    Object removeSingleUserFromChatGroup(String groupId, String userId);

    /**
     * 群组减人[批量] <br>
     * DELETE
     *
     * @param groupId
     *            群组标识
     * @param userIds
     *            用户ID或用户名，数组形式
     * @return
     */
    Object removeBatchUsersFromChatGroup(String groupId, String[] userIds);

    /**
     * Get All the Groups
     * @param limit
     * @param cursor
     * @return
     */
    Object getChatGroups(Long limit, String cursor);


    /**
     * 获取群组中所有用户
     * @param groupId
     * @return
     */
    Object getChatGroupUsers(final String groupId);

    /**
     * 群组详情
     * @param groupIds
     * @return
     */
    Object getChatGroupDetails(final String[] groupIds);
}
