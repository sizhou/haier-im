package com.haier.im.base;

public class IMConstant {

    /**
     * 账户状态：账户不存在
     */
    public static final Integer ACCOUNT_STATUS_NOEXIST = 0;

    /**
     * 账户状态：账户存在，IM未激活
     */
    public static final Integer ACCOUNT_STATUS_EXIST = 1;

    /**
     * 账户状态：IM已激活
     */
    public static final Integer ACCOUNT_STATUS_ACTIVATE = 2;

    /**
     * 账户角色：普通
     */
    public static final Integer ACCOUNT_ROLE_COM = 1;

    /**
     * 账户角色：客服
     */
    public static final Integer ACCOUNT_ROLE_CUS = 2;

    /**
     * 群组用户初始化状态：已注册
     */
    public static final Integer COMMUNITY_USER_EXIST = 1;

    /**
     * 群组用户初始化状态：未注册
     */
    public static final Integer COMMUNITY_USER_NOEXIST = 0;


    public static final Integer FRIEND_OPER_ACTION_ADD = 1;//加好友
    public static final Integer FRIEND_OPER_ACTION_DEL = 2;//删好友

    public static final Integer GROUP_OPER_ACTION_ADD = 1;//邀请入群
    public static final Integer GROUP_OPER_ACTION_REMOVE = 2;//从群组中删除
    public static final Integer GROUP_OPER_ACTION_LEAVE = 3;//退群


    public static final Integer COMMUNITY_ROLE_OWNER = 1;//群主
    public static final Integer COMMUNITY_ROLE_MANAGER = 2;//管理员
    public static final Integer COMMUNITY_ROLE_MEMBER = 3;//普通成员


    public static final String ROLE_NAME_OWNER = "owner";//群主
    public static final String ROLE_NAME_MANAGER = "manager";//管理员
    public static final String ROLE_NAME_MEMBER = "member";//普通成员


    public static final String HOTTEST_MAX_MEMBER = "affiliationsCount";//按人数推最热门


    /**
     * 消息类型：文字
     */
    public static final Integer MESSAGE_TYPE_TXT = 1;

    /**
     * 消息类型：图片
     */
    public static final Integer MESSAGE_TYPE_IMG = 2;

    /**
     * 消息类型：地址
     */
    public static final Integer MESSAGE_TYPE_LOC = 3;

    /**
     * 消息类型：语音
     */
    public static final Integer MESSAGE_TYPE_AUDIO = 4;

    /**
     * 消息类型：视频
     */
    public static final Integer MESSAGE_TYPE_VIDEO = 5;

    /**
     * 消息类型：文件
     */
    public static final Integer MESSAGE_TYPE_FILE = 6;

}
