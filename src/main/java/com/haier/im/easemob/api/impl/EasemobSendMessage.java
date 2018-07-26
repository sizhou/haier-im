package com.haier.im.easemob.api.impl;

import com.haier.im.easemob.api.SendMessageAPI;
import com.haier.im.easemob.common.EasemobAPI;
import com.haier.im.easemob.common.OrgInfo;
import com.haier.im.easemob.common.ResponseHandler;
import com.haier.im.easemob.common.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;
import org.springframework.stereotype.Service;

@Service
public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
