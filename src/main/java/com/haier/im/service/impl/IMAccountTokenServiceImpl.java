package com.haier.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.im.aop.logger.OperLog;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IMAccountTokenServiceImpl implements IMAccountTokenService {

    @Resource
    private IMAccountTokenMapper imAccountTokenMapper;
    @Resource
    private IMAccountInfoMapper imAccountInfoMapper;
    @Resource
    private RedisTemplate<String, Long> redisTemplate;

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


    private IMAccountInfo addUserToIM(String phone, boolean activated) {
        IMAccountInfo resultEn = null;
        IMAccountInfo accountEn = imAccountInfoMapper.selectSingleAccountBy(new IMAccountInfo().setPhone(phone));
        if (accountEn == null || accountEn.getUserId() == null) {
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
                resultEn = imAccountInfo;
            }
        } else {
            resultEn = accountEn;
        }
        return resultEn;

    }


    @Override
    @Transactional
    @OperLog
    public RespResult authIMUserByPhone(String phone) {
        RespResult respResult = new RespResult();
        IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByPhone(phone);
        if (imAccountInfo == null) {
            IMUserModel imUserModel = this.RegisterEasemoboUser(phone);
            IMAccountInfo addResultEn = this.addUserToIM(phone, imUserModel.isActivated());
            if (null != addResultEn && null != addResultEn.getUserId() && addResultEn.getUserId() > 0) {
                String token = this.createToken(addResultEn);
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
                currentValidateToken = this.createToken(imAccountInfo);
            } else {
                currentValidateToken = imAccountToken.getToken();
            }
            if (!redisTemplate.hasKey(currentValidateToken)) {
                redisTemplate.opsForValue().set(currentValidateToken, imAccountInfo.getUserId(), expiredLong, TimeUnit.DAYS);
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
    public String createToken(IMAccountInfo user) {
        //生成token
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replaceAll("-", "");
        //存表
        IMAccountToken imAccountToken = new IMAccountToken();
        imAccountToken.setUserId(user.getUserId());
        imAccountToken.setToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + expiredLong);
        imAccountToken.setExpireTime(calendar.getTime());
        int changeFlag = imAccountTokenMapper.upsertToken(imAccountToken);
        if (changeFlag > 0) {
            //存redis
            redisTemplate.opsForValue().set(token, user.getUserId(), expiredLong, TimeUnit.DAYS);
        }
        return token;
    }

    @Override
    public boolean checkToken(String token) {
        Long userId = this.getUserIdByToken(token);
        if (null != userId && userId > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Long getUserIdByToken(String token) {
        if (redisTemplate.hasKey(token)) {
            Long userId = redisTemplate.opsForValue().get(token);
            if (userId != null && userId > 0) {
                return userId;
            } else {
                //查库
                IMAccountToken imAccountToken = imAccountTokenMapper.findSingleTokenBytoken(token);
                if (imAccountToken != null && imAccountToken.getExpireTime().after(new Date())) {
                    return imAccountToken.getUserId();
                } else {
                    return null;
                }
            }
        } else {
            //查库
            IMAccountToken imAccountToken = imAccountTokenMapper.findSingleTokenBytoken(token);
            if (imAccountToken != null && imAccountToken.getExpireTime().after(new Date())) {
                IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByUserId(imAccountToken.getUserId());
                redisTemplate.opsForValue().set(token, imAccountInfo.getUserId(), imAccountToken.getExpireTime().getTime(),TimeUnit.DAYS);
                return imAccountToken.getUserId();
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean clearToken(String token) {
        imAccountTokenMapper.delTokenByToken(token);
        redisTemplate.delete(token);
        return true;
    }
}
