package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.SelfAccountInfoReq;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.vo.IMAccountInfoVo;
import org.springframework.web.multipart.MultipartFile;

public interface IMAccountInfoService {

    /**
     * 按照条件查询
     *
     * @param findAccountVo
     * @return
     */
    RespResult findAccountsBy(IMAccountInfoVo findAccountVo, Long userId);

    /**
     * 查看用户基本信息
     *
     * @param findIdEn
     * @param userId
     * @return
     */
    RespResult findAccountInfo(IMAccountInfo findIdEn, Long userId);

    /**
     * 查看用户详情
     *
     * @param findIdEn
     * @param userId
     * @return
     */
    RespResult findSingleAccountDetail(IMAccountInfo findIdEn, Long userId);

    /**
     * 修改个人信息
     *
     * @param updEn
     * @param userId
     * @return
     */
    RespResult updAccountDetailBySelf(SelfAccountInfoReq updEn, Long userId);

    /**
     * 修改个人头像
     *
     * @param selfPortrait
     * @param userId
     * @return
     */
    RespResult updAccountportailBySelf(MultipartFile selfPortrait, Long userId);

    /**
     * 根据id获取用户信息
     *
     * @param userId
     * @return
     */
    IMAccountInfo checkUserExitByUserId(Long userId);

    IMAccountInfo checkUserExitByImId(String imId);

    IMAccountInfo checkUserExitByPhone(String phone);


}
