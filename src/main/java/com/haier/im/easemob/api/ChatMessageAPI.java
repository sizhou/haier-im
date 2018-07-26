package com.haier.im.easemob.api;

public interface ChatMessageAPI {
    /**
     * 获取聊天记录下载链接
     * GET
     * @param timeStr
     *              目标聊天记录的时间
     * @return
     */
    Object exportChatMessages(String timeStr);
}
