package com.haier.im.easemob.api.impl;

import com.haier.im.easemob.api.FileAPI;
import com.haier.im.easemob.common.EasemobAPI;
import com.haier.im.easemob.common.OrgInfo;
import com.haier.im.easemob.common.ResponseHandler;
import com.haier.im.easemob.common.TokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.UploadAndDownloadFilesApi;
import org.springframework.stereotype.Service;

@Service
public class EasemobFile implements FileAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private UploadAndDownloadFilesApi api = new UploadAndDownloadFilesApi();
    @Override
    public Object downloadFile(final String fileUUID, final String shareSecret, final Boolean isThumbnail) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatfilesUuidGet(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
    }
}
