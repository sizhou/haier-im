package com.haier.im.easemob.api.impl;

import com.haier.im.easemob.api.IMUserAPI;
import com.haier.im.easemob.common.EasemobAPI;
import com.haier.im.easemob.common.OrgInfo;
import com.haier.im.easemob.common.ResponseHandler;
import com.haier.im.easemob.common.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.UsersApi;
import io.swagger.client.model.RegisterUsers;
import org.springframework.stereotype.Service;

@Service
public class EasemobIMUsers implements IMUserAPI {
    private UsersApi api = new UsersApi();
    private ResponseHandler responseHandler = new ResponseHandler();

    @Override
    public Object createNewIMUserSingle(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameUsersPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, (RegisterUsers) payload, TokenUtil.getAccessToken());
            }
        });
    }

    @Override
    public Object addFriendSingle(final String userName, final String friendName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernamePost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),userName,friendName);
            }
        });
    }

    @Override
    public Object deleteFriendSingle(final String userName, final String friendName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameUsersOwnerUsernameContactsUsersFriendUsernameDelete(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),userName,friendName);
            }
        });
    }

    @Override
    public Object getIMUserAllChatGroups(String userName) {
        return null;
    }


    @Override
    public Object getIMUsersBatch(final Long limit, final String cursor) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameUsersGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),limit+"",cursor);
            }
        });
    }


    @Override
    public Object getIMUserByUserName(final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameUsersUsernameGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME,TokenUtil.getAccessToken(),userName);
            }
        });
    }

}
