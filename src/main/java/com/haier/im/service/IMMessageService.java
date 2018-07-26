package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.vo.IMMessageVo;

public interface IMMessageService {


    /***
     * 查询历史消息 包括发送人信息
     * @param messageVo
     * @return
     */
    RespResult msgs(IMMessageVo messageVo);

    /**
     * 获取聊天文件
     *
     * @param messageVo
     * @return
     */
    RespResult listChatFiles(IMMessageVo messageVo);

}
