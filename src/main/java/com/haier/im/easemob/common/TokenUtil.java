package com.haier.im.easemob.common;

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TokenUtil {
    public static Token BODY;
    private static AuthenticationApi API = new AuthenticationApi();
    private static String ACCESS_TOKEN;
    private static Double EXPIREDAT = -1D;

    public static TokenUtil tokenUtil;

    @PostConstruct
    public void init() {
        tokenUtil = this;
    }

    static {
        BODY = new Token().clientId(OrgInfo.CLIENT_ID).grantType(OrgInfo.GRANT_TYPE).clientSecret(OrgInfo.CLIENT_SECRET);
        System.out.println("token="+BODY);
    }


    /**
     * get token from server
     */
    public static void initTokenByProp() {
        String resp = null;
        try {
            resp = API.orgNameAppNameTokenPost(OrgInfo.ORG_NAME, OrgInfo.APP_NAME, BODY);
        } catch (ApiException e) {
//            logger.error(e.getMessage());
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, Map.class);
        ACCESS_TOKEN = " Bearer " + map.get("access_token");
        System.out.println(ACCESS_TOKEN);
        EXPIREDAT = System.currentTimeMillis() + (Double) map.get("expires_in") * 1000;
        System.out.println(EXPIREDAT);
    }

    /**
     * get Token from memory Or Redis
     *
     * @return
     */
    public static String getAccessToken() {
        if (ACCESS_TOKEN == null || isExpired()) {
            initTokenByProp();
        }
        return ACCESS_TOKEN;
    }

    private static Boolean isExpired() {
        return System.currentTimeMillis() > EXPIREDAT;
    }
}
