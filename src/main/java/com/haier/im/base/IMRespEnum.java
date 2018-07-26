package com.haier.im.base;

public enum IMRespEnum implements ICDEnum {

    SUCCESS("0", "成功", "成功"),


    AUTH_FAILD("403", "token验证失效", "token失效，请重新授权"),




    EXIST("010100030101", "存在", "存在"),
    NOTEXIST("010100030102", "服务不可用", "服务不可用"),
    FAIL("010100030103", "失败", "失败"),
    SYSTEM_OVERTIME("010100030104", "系统超时", "系统超时"),
    SYS_PARAM_ERROR("010100030105", "请求接口参数有误", "参数有误"),
    SYS_DATA_ERROR("010100030106", "数据异常", "数据异常"),

    SYS_STAT_SUCCESS("010100010106", "上传成功", "上传成功"),
    SYS_STAT_FAIL("010100010107", "上传失败", "上传失败"),


    /**
     * 系统账户相关
     */
    SYS_USER_NOTEXIT("010100031000", "账户不存在或已被删除", "用户id传递错误"),
    SYS_USER_ADD_FAILD("010100031001", "账户添加失败", "账户录入系统失败"),
    SYS_USER_DEL_FAILD("010100031002", "账户删除失败", "账户删除失败"),


    /**
     * 环信账户相关
     */
    IM_INIT_FAIL("010100010201", "环信账户初始化失败", "用户初始化失败"),
    IM_THRID_FAIL("010100010202", "环信注册失败", "用户初始化失败"),
    IM_ADDFRIEND_FAILD("010100010203", "环信添加好友失败", "好友添加失败"),
    IM_REMVFRIEND_FAILD("010100010204", "环信删除好友失败", "好友删除失败"),
    INIT_MOD_ACC("010100011103", "账户初始化时，对于已经存在的账户，更新账户时异常", "初始化失败"),


    /**
     * 系统群组相关
     */
    DEF_GROUP_PERSON_ADD_FAIL("010100011002", "查询用户所有群组信息时，创建用户信息失败", "社群创建失败"),
    GROUP_ADD_FAIL("010100010401", "社群创建失败", "社群创建失败"),
    GROUP_ACC_NOPOWER("010100010402", "权限不足", "权限不足"),
    GROUP_NOTEXIST("010100010403", "社群不存在", "社群不存在"),
    GROUP_MOD_FAIL("010100010404", "修改社群信息失败", "修改社群信息失败"),

    INVITE_ONLYOWNER("010100011101", "邀请者权限不足,只允许群主邀请", "权限不足"),
    INVITE_NOPOWER("010100011102", "APP成员邀请成员入群时，邀请者权限不足", "权限不足"),
    NOT_IN_GROUP("010100011103", "用户不在群组中", "不在群组中"),
    ALREADY_IN_GROUP("010100011104", "用户已经在群组中", "已在群组中"),
    INVITE_COMM_COUNT_FAIL("010100011110", "APP成员邀请成员入群时，修改群组人数信息时异常", "邀请失败"),
    INVITE_USER_OPER("010100011107", "APP成员邀请成员入群时，增加群组用户变动信息时异常", "邀请失败"),
    INVITE_USER_FAILD("010100011106", "APP成员邀请成员入群时，增加群组用户信息时异常", "邀请失败"),
    USER_REV_FAIL("010100010601", "删除社群成员时，更新社群人数时异常", "删除社群成员失败"),
    USER_REV_OPER_FAIL("010100010602", "删除社群成员时，插入删除操作记录时异常", "删除社群成员失败"),
    USER_REV_NOEXIST("010100010603", "删除社群成员时，该用户不存在，或者已经删除", "删除社群成员失败"),
    USER_MOD_STATUS_FAIL("010100010604", "删除社群成员时，更新会员状态异常", "删除社群成员失败"),
    OWNER_LEAVE_ERROR("010100010605", "群主不能退群", "群主退群失败"),

    /**
     * 好友
     */
    FRIEND_ALREAD("010110010000", "已经是好友", "已经是好友"),
    FRIEND_NOSHIP("010110010001", "不是好友关系", "不是好友"),
    FRIEND_NOT_INVITED("010110010002", "没有被请求成为好友", "没有被邀请"),



    /**
     * 历史消息和文件
     */
    FILE_NOPOWER_FAIL("010100011117", "查询群组共享文件权限不足", "权限不足"),
    MSG_NOPOWER_FAIL("010100011118", "查询群组历史消息权限不足", "权限不足"),
    SEND_TYPE_ERROR("010100011119", "目前支持群聊和好友聊天", "聊天类型暂不支持"),


    FILE_KEY_FAIL("010100010501", "参数错误", "参数错误"),


    //TODO
    TOKE_SUCCESS("010100010108", "票据校验成功", "票据校验成功"),
    TOKE_ILLE_FAIL("010100010109", "票据非法", "票据非法");

    private String code;
    private String desc;
    private String msg;

    private IMRespEnum(String code, String desc, String msg) {
        this.code = code;
        this.desc = desc;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
