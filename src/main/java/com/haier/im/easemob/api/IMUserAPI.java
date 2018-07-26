package com.haier.im.easemob.api;

/**
 * This interface is created for RestAPI of User Integration, it should be synchronized with the API list.
 */
public interface IMUserAPI {
    /**
     * 注册IM用户[单个] <br>
     * POST
     *
     * @param payload
     *            <code>{"username":"${用户名}","password":"${密码}", "nickname":"${昵称值}"}</code>
     * @return
     */
    Object createNewIMUserSingle(Object payload);

    /**
     * 给IM用户的添加好友 <br>
     * POST
     *
     * @param userName
     *            用戶名或用戶ID
     * @param friendName
     *            好友用戶名或用戶ID
     * @return
     */
    Object addFriendSingle(String userName, String friendName);

    /**
     * 解除IM用户的好友关系 <br>
     * DELETE
     *
     * @param userName
     *            用戶名或用戶ID
     * @param friendName
     *            好友用戶名或用戶ID
     * @return
     */
    Object deleteFriendSingle(String userName, String friendName);

    /**
     * 获取用户参与的群组 <br>
     * GET
     *
     * @param userName
     *            用戶名或用戶ID
     * @return
     * @see http://docs.easemob.com/doku.php?id=start:100serverintegration:
     *      60groupmgmt
     */
    Object getIMUserAllChatGroups(String userName);


    Object getIMUsersBatch(Long limit, String cursor);

    /**
     * 获取IM用户[单个] <br>
     * GET
     *
     * @param userName
     *            用戶名或用戶ID
     * @return
     */
    Object getIMUserByUserName(String userName);
}
