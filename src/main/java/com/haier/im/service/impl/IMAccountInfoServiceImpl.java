package com.haier.im.service.impl;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.OSSClientUtil;
import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.SelfAccountInfoReq;
import com.haier.im.dao.IMAccountDetailMapper;
import com.haier.im.dao.IMAccountInfoMapper;
import com.haier.im.easemob.api.IMUserAPI;
import com.haier.im.po.IMAccountDetail;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMFriendService;
import com.haier.im.vo.IMAccountDetailVo;
import com.haier.im.vo.IMAccountInfoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class IMAccountInfoServiceImpl implements IMAccountInfoService {

    @Resource
    private IMAccountInfoMapper imAccountInfoMapper;

    @Resource
    private IMAccountDetailMapper imAccountDetailMapper;

    @Autowired
    private IMFriendService imFriendService;

    @Autowired
    private IMUserAPI imUserAPI;

    @Value("${im.user.default.portrait}")
    private String defaultPortrait;

    static final String USER_KEY = "userId";


    @Override
    public RespResult findAccountsBy(IMAccountInfoVo findAccountVo, Long userId) {
        RespResult respResult = null;
        if (findAccountVo != null) {
            respResult = new RespResult();
            List<IMAccountInfo> accountList = imAccountInfoMapper.selectAccountsBy(findAccountVo);
            List<IMAccountInfoVo> resultList = new ArrayList<>();
            if (accountList != null && accountList.size() > 0) {
                List<IMAccountInfoVo> result = new ArrayList<>();
                IMAccountInfoVo vo;
                for (IMAccountInfo acc : accountList) {
                    vo = new IMAccountInfoVo();
                    vo = vo.parsePoTo(acc);
                    //判断是否是好友
                    vo.setFriend(imFriendService.checkIsFriend(userId, acc.getUserId()));
                    vo.setPortrait(OSSClientUtil.getPubUrl(acc.getPortrait()));
                    resultList.add(vo);
                }
                respResult.setData(resultList);
            }
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult findAccountInfo(IMAccountInfo findIdEn, Long userId) {
        RespResult respResult = null;
        if (findIdEn != null && (findIdEn.getUserId() != null || findIdEn.getImId() != null) || findIdEn.getPhone() != null) {
            respResult = new RespResult();
            IMAccountInfo account = imAccountInfoMapper.selectSingleAccountBy(findIdEn);
            IMAccountInfoVo resultData = null;
            if (account != null) {
                resultData = new IMAccountInfoVo();
                resultData = resultData.parsePoTo(account);
                if (account.getUserId().equals(userId)) {
                    //是否是自己
                    resultData.setSelf(true);
                } else {
                    //判断是否是好友
                    resultData.setFriend(imFriendService.checkIsFriend(userId, account.getUserId()));
                }
                resultData.setPortrait(OSSClientUtil.getPubUrl(account.getPortrait()));
            }
            respResult.setData(resultData);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult findSingleAccountDetail(IMAccountInfo findIdEn, Long userId) {
        RespResult respResult = null;
        if (findIdEn != null && (findIdEn.getUserId() != null || findIdEn.getImId() != null) || findIdEn.getPhone() != null) {
            respResult = new RespResult();
            IMAccountDetailVo account = imAccountInfoMapper.selectSingleAccountDetailBy(findIdEn);
            if (account != null) {
                if (account.getUserId().equals(userId)) {
                    //是否是自己
                    account.setSelf(true);
                } else {
                    //判断是否是好友
                    account.setFriend(imFriendService.checkIsFriend(userId, account.getUserId()));
                }
                account.setPortrait(OSSClientUtil.getPubUrl(account.getPortrait()));
            }
            respResult.setData(account);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        }
        return respResult;
    }


    @Override
    @Transactional
    public RespResult updAccountDetailBySelf(SelfAccountInfoReq reqUpdEn, Long userId) {
        RespResult respResult = new RespResult();
        int result,result2 = 0;
        if (reqUpdEn.getNickName() != null && StringUtils.isNotEmpty(reqUpdEn.getNickName())) {
            IMAccountInfo updateEn = new IMAccountInfo();
            updateEn.setUserId(userId).setRealName(reqUpdEn.getNickName());
            result = imAccountInfoMapper.updateAccountBy(updateEn);
        }
        if (reqUpdEn.getSex() != null || reqUpdEn.getBirthDay() != null) {
            IMAccountDetail detail = imAccountDetailMapper.findAccountDetailByUserId(userId);
            if (detail != null) {
                IMAccountDetail updEn = new IMAccountDetail();
                updEn.setSex(reqUpdEn.getSex()).setBirthday(reqUpdEn.getBirthDay());
                result2 = imAccountDetailMapper.updAccountDetailByUserId(updEn, userId);
            } else {
                IMAccountDetail addEn = new IMAccountDetail();
                addEn.setSex(reqUpdEn.getSex()).setBirthday(reqUpdEn.getBirthDay()).setUserId(userId);
                result2 = imAccountDetailMapper.insertAccountDetail(addEn);
            }
        }
        respResult.setCode(IMRespEnum.SUCCESS.getCode());
        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        return respResult;
    }


    @Override
    public RespResult updAccountportailBySelf(MultipartFile selfPortrait, Long userId) {
        RespResult respResult = new RespResult();
        IMAccountInfo accountInfo = imAccountInfoMapper.selectSingleAccountBy(new IMAccountInfo().setUserId(userId));
        if (accountInfo != null){
            String portraitKey = OSSClientUtil.uploadImg2Oss(selfPortrait, true, accountInfo.getPortrait());
            IMAccountInfo updEn = new IMAccountInfo();
            updEn.setUserId(userId).setPortrait(portraitKey);
            int result = imAccountInfoMapper.updateAccountBy(updEn);
            if (result > 0){
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            }else {
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
            }
        }else{
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

    @Override
    public IMAccountInfo checkUserExitByUserId(Long userId) {
        IMAccountInfo findEn = new IMAccountInfo();
        findEn.setUserId(userId);
        IMAccountInfo user = imAccountInfoMapper.selectSingleAccountBy(findEn);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public IMAccountInfo checkUserExitByImId(String imId) {
        IMAccountInfo findEn = new IMAccountInfo();
        findEn.setImId(imId);
        IMAccountInfo user = imAccountInfoMapper.selectSingleAccountBy(findEn);
        if (user != null) {
            return user;
        }
        return null;
    }


    @Override
    public IMAccountInfo checkUserExitByPhone(String phone) {
        IMAccountInfo findEn = new IMAccountInfo();
        findEn.setPhone(phone);
        IMAccountInfo user = imAccountInfoMapper.selectSingleAccountBy(findEn);
        if (user != null) {
            return user;
        }
        return null;
    }


}
