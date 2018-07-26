package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.ReqCommunity;
import org.springframework.web.multipart.MultipartFile;

public interface IMCommunityService {

    /***
     * 创建群
     * @param communityName
     * @param ownerUserId
     * @param iconFile
     * @return
     */
    RespResult createCommunity(String communityName,Long ownerUserId,MultipartFile iconFile);

    /**
     * 修改群信息
     *
     * @param reqCommunity
     * @return
     */
    RespResult modCommunity(ReqCommunity reqCommunity);


    /**
     * 更换群组图标
     * @param communityId
     * @param ownerUserId
     * @param iconFile
     * @return
     */
    RespResult modIconForCommunity(String communityId,Long ownerUserId,MultipartFile iconFile);

    /**
     * 群主删除群组
     *
     * @param communityId
     * @param userId
     * @return
     */
    RespResult delCommunity(String communityId, Long userId);

    /**
     * 搜索群
     *
     * @param searchStr
     * @param userId
     * @return
     */
    RespResult searchCommunityByStr(String searchStr, Long userId);

    /**
     * 获取用户参与的所有群组
     *
     * @param userId
     * @return
     */
    RespResult findCommunityOfInByUserId(Long userId);

    /***
     * 为用户推荐群
     * @param conditionLimit
     * @return
     */
    RespResult recommendCommunityForUser(Integer conditionLimit);


    /***
     * 推荐火热群
     * @param conditionLimit(推荐个数)
     * @return
     */
    RespResult findHosttestCommunitys(Integer conditionLimit);

    /**
     * 群详情
     * (群名称，id,群成员列表，群公告,是否已经加入...)
     *
     * @param communityId
     * @param userId
     * @return
     */
    RespResult getCommunityDetail(String communityId, Long userId);


}
