package com.haier.im.easemob.api.impl;

import com.haier.im.easemob.api.ChatMessageAPI;
import com.haier.im.easemob.common.EasemobAPI;
import com.haier.im.easemob.common.OrgInfo;
import com.haier.im.easemob.common.ResponseHandler;
import com.haier.im.easemob.common.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.ChatHistoryApi;
import org.springframework.stereotype.Service;

@Service
public class EasemobChatMessage implements ChatMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private ChatHistoryApi api = new ChatHistoryApi();
    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatmessagesTimeGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(),timeStr);
            }
        });
    }
}
