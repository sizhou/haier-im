package com.haier.im.easemob.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrgInfo {

    public static String ORG_NAME;
    public static String APP_NAME;
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String GRANT_TYPE;

    @Value("${im.org.name}")
    private String orgName;
    @Value("${im.app.name}")
    private String appName;
    @Value("${im.client.id}")
    private String clientId;
    @Value("${im.client.secret}")
    private String clientSecret;
    @Value("${im.grant.type}")
    private String grantType;

    @PostConstruct
    public void init(){
        ORG_NAME = orgName;
        APP_NAME = appName;
        CLIENT_ID = clientId;
        CLIENT_SECRET = clientSecret;
        GRANT_TYPE = grantType;
    }


}
