package com.haier.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.im.base.*;
import com.haier.im.dao.IMAccountInfoMapper;
import com.haier.im.dao.IMAccountTokenMapper;
import com.haier.im.easemob.api.IMUserAPI;
import com.haier.im.easemob.model.IMUserModel;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.po.IMAccountToken;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMAccountTokenService;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class IMAccountTokenServiceImpl implements IMAccountTokenService {

    @Resource
    private IMAccountTokenMapper imAccountTokenMapper;

    @Resource
    private IMAccountInfoMapper imAccountInfoMapper;


    @Autowired
    private IMAccountInfoService imAccountInfoService;
    @Autowired
    private IMUserAPI imUserAPI;

    @Value("${im.token.expired.long}")
    private Integer expiredLong;
    @Value("${user.default.portrait}")
    private String defaultPortrait;


    private IMUserModel RegisterEasemoboUser(String phone) {
        IMUserModel imUserResult = new IMUserModel();

        //注册环信账户
        RegisterUsers registerUsers = new RegisterUsers();
        User user = new User();

        // TODO: 2018/3/22 账号密码规则需要优化
        user.setPassword(MD5Gen.getMD5(phone + "1"));
        user.setUsername(MD5Gen.getMD5(phone + "2"));

        imUserResult.setUserName(user.getUsername());

        registerUsers.add(user);

        //判断时候需要重新注册
        Object obj = imUserAPI.getIMUserByUserName(user.getUsername());
        boolean isExit = false;
        if (null != obj) {
            //已经存在
            System.out.println("===is already exit==" + obj.toString());
            isExit = true;
        } else {
            //创建环信账户
            obj = imUserAPI.createNewIMUserSingle(registerUsers);
            System.out.println("====new user ==" + obj.toString());
            isExit = false;
        }

        if (null != obj) {
            JSONObject json = JSON.parseObject(obj.toString());
            JSONArray arr = json.getJSONArray("entities");
            if (arr.size() == 1) {
                JSONObject jsonUser = arr.getJSONObject(0);
                imUserResult.setActivated(jsonUser.getBoolean("activated"));
            }
        } else {
            //注册环信失败
            imUserResult.setActivated(false);
        }
        return imUserResult;
    }


    private long addUserToIM(String phone, boolean activated) {
        long resultUserId = 0;
        IMAccountInfo accountEn = imAccountInfoMapper.selectSingleAccountBy(new IMAccountInfo().setPhone(phone));
        if (accountEn == null ||accountEn.getUserId() == null ) {
            IMAccountInfo imAccountInfo = new IMAccountInfo()
                    .setPhone(phone)
                    .setRealName(phone)
                    .setCommunityRole(IMConstant.ACCOUNT_ROLE_COM)
                    .setActivated(activated)
                    .setImId(UserTokenCheck.generateIMID(phone))
                    .setPortrait(defaultPortrait);
            imAccountInfo.setCreateTime(new Date());
            imAccountInfo.setCreator(imAccountInfo.getPhone());
            //生成用户表示id
            Long userId = Calendar.getInstance().getTime().getTime();
            imAccountInfo.setUserId(userId);
            int result = imAccountInfoMapper.insertNonil(imAccountInfo);
            if (result > 0) {
                resultUserId = userId;
            }
        } else {
            resultUserId = accountEn.getUserId();
        }
        return resultUserId;

    }


    @Override
    public RespResult authIMUserByPhone(String phone) {
        RespResult respResult = new RespResult();
        IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByPhone(phone);
        if (imAccountInfo == null) {
            IMUserModel imUserModel = this.RegisterEasemoboUser(phone);
            long userId = this.addUserToIM(phone, imUserModel.isActivated());
            if (userId > 0) {
                String token = this.createToken(userId);
                respResult.setData(token);
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            } else {
                //失败
                respResult.setCode(IMRespEnum.SYS_USER_ADD_FAILD.getCode());
                respResult.setMsg(IMRespEnum.SYS_USER_ADD_FAILD.getMsg());
            }
        } else {
            Long currentUserId = imAccountInfo.getUserId();
            String currentValidateToken = "";
            //token
            IMAccountToken imAccountToken = imAccountTokenMapper.findSingleTokenByUserId(imAccountInfo.getUserId());
            if (imAccountToken == null || imAccountToken.getExpireTime().before(new Date())) {
                currentValidateToken = this.createToken(currentUserId);
            } else {
                currentValidateToken = imAccountToken.getToken();
            }
            respResult.setData(currentValidateToken);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            if (!imAccountInfo.isActivated()) {
                boolean isActived = this.RegisterEasemoboUser(phone).isActivated();
                imAccountInfoMapper.updateAccountBy(new IMAccountInfo().setUserId(currentUserId).setActivated(isActived));
            }
        }
        return respResult;
    }


    @Override
    public String createToken(long userId) {
        //生成token
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replaceAll("-", "");
        //存表
        IMAccountToken imAccountToken = new IMAccountToken();
        imAccountToken.setUserId(userId);
        imAccountToken.setToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + expiredLong);
        imAccountToken.setExpireTime(calendar.getTime());
        imAccountTokenMapper.upsertToken(imAccountToken);
        return token;
    }

    @Override
    public boolean checkToken(String token) {
        IMAccountToken imAccountToken = imAccountTokenMapper.findSingleTokenBytoken(token);
        if (imAccountToken != null && imAccountToken.getExpireTime().after(new Date())) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Long getUserIdByToken(String token) {
        IMAccountToken imAccountToken = imAccountTokenMapper.findSingleTokenBytoken(token);
        if (imAccountToken != null && imAccountToken.getExpireTime().after(new Date())) {
            return imAccountToken.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean clearToken(String token) {
        return false;
    }
}
