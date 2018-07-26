package com.haier.im.base;

import com.alibaba.fastjson.JSONObject;
import com.haier.im.dao.IMAccountTokenMapper;
import com.haier.im.po.IMAccountInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UserTokenCheck {


    @Value("${account.server.url}")
    private String accountServerUrl;

    @Value("${account.bytoken.endpoint}")
    private String userByTokenEndpoint;


    @Resource
    private IMAccountTokenMapper imAccountTokenMapper;


    /**
     * 校验用户票据是否有效
     *
     * @param token
     * @return
     */
    public RespResult checkToken(String token) {
        RespResult imBaseResp = new RespResult();
//        if (!Validate.isToken(token)) {
        if (false) {
            imBaseResp.setCode(IMRespEnum.TOKE_ILLE_FAIL.getCode());
            imBaseResp.setMsg(IMRespEnum.TOKE_ILLE_FAIL.getMsg());
        } else {
            //调用接口校验
            RestTemplate restTemplate = new RestTemplate();
            JSONObject object = restTemplate.getForObject(accountServerUrl + userByTokenEndpoint+"?token=" + token, JSONObject.class);

            if (object.getString("code").equals(IMRespEnum.SUCCESS.getCode())) {
                IMAccountInfo imAccountInfo = new IMAccountInfo();
                JSONObject accountUser = object.getJSONObject("data");
                imAccountInfo.setPortrait(accountUser.getString("accountProfilePhoto"));
                imAccountInfo.setPhone(accountUser.getString("phone"));
                imAccountInfo.setAccountId(accountUser.getString("accountId"));
                imAccountInfo.setRealName(accountUser.getString("realName"));
                imBaseResp.setData(imAccountInfo);
                imBaseResp.setCode(IMRespEnum.TOKE_SUCCESS.getCode());
                imBaseResp.setMsg(IMRespEnum.TOKE_SUCCESS.getMsg());
            } else {
                imBaseResp.setCode(object.getString("code"));
                imBaseResp.setMsg(object.getString("msg"));
            }
        }
        return imBaseResp;
    }

    public static String generateIMID(String phone) {
        return MD5Gen.getMD5(phone + "2");
    }






}
