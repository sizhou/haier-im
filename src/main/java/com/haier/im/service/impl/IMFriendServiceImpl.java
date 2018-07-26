package com.haier.im.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haier.im.base.*;
import com.haier.im.controller.reqvo.ReqAddFriend;
import com.haier.im.dao.IMFriendMapper;
import com.haier.im.dao.IMFriendOperMapper;
import com.haier.im.easemob.api.IMUserAPI;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.po.IMFriend;
import com.haier.im.po.IMFriendOper;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMFriendService;
import com.haier.im.vo.IMFriendDetailVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.Collator;
import java.util.*;

@Service
public class IMFriendServiceImpl implements IMFriendService {


    @Autowired
    private IMAccountInfoService imAccountInfoService;

    @Resource
    private IMFriendMapper imFriendMapper;
    @Resource
    private IMFriendOperMapper imFriendOperMapper;

    @Autowired
    private IMUserAPI imUserAPI;

    @Value("${user.default.portrait}")
    private String userDefaultPortrait;


    @Override
    public RespResult addFriendNoNeedAgree(ReqAddFriend imFriend) {
        RespResult respResult = new RespResult();
        //判断id是否存在在当前系统

        IMAccountInfo selfEn = imAccountInfoService.checkUserExitByUserId(imFriend.getSelfUserId());
        IMAccountInfo friAccount = imAccountInfoService.checkUserExitByUserId(imFriend.getFriUserId());
        if (selfEn != null && friAccount != null) {
            //判断当前两人是否已是好友
            boolean isFriend = checkIsFriend(imFriend.getSelfUserId(), imFriend.getFriUserId());
            if (isFriend) {
                respResult.setCode(IMRespEnum.FRIEND_ALREAD.getCode());
                respResult.setMsg(IMRespEnum.FRIEND_ALREAD.getMsg());
            } else {
                //记录日志
                IMFriendOper oper = new IMFriendOper();
                oper.setOperUserId(imFriend.getSelfUserId());
                oper.setOperedUserId(imFriend.getFriUserId());
                oper.setActionType(IMConstant.FRIEND_OPER_ACTION_ADD);
                imFriendOperMapper.addFriendOperLog(oper);
                //好友请求记入好友表
                imFriend.setFriNickName((imFriend.getFriNickName() != null && StringUtils.isNotEmpty(imFriend.getFriNickName().trim())) ? imFriend.getFriNickName().trim() : friAccount.getRealName());
                IMFriend addEn = new IMFriend();
                addEn.parseToAdd(addEn, imFriend);
                addEn.setSelfNickName(selfEn.getRealName());
                addEn.setAgree(true);
                Object obj = imUserAPI.addFriendSingle(selfEn.getImId(), friAccount.getImId());
                int result = 0;
                if (obj != null) {
                    result = imFriendMapper.addFriend(addEn);
                }
                if (result > 0) {
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.SYS_USER_ADD_FAILD.getCode());
                    respResult.setMsg(IMRespEnum.SYS_USER_ADD_FAILD.getMsg());
                }
            }

        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }

        return respResult;
    }

    @Override
    public RespResult addFriend(ReqAddFriend imFriend) {
        RespResult respResult = new RespResult();
        //判断id是否存在在当前系统

        IMAccountInfo selfEn = imAccountInfoService.checkUserExitByUserId(imFriend.getSelfUserId());
        IMAccountInfo friAccount = imAccountInfoService.checkUserExitByUserId(imFriend.getFriUserId());
        if (selfEn != null && friAccount != null) {
            //判断当前两人是否已是好友
            boolean isFriend = checkIsFriend(imFriend.getSelfUserId(), imFriend.getFriUserId());
            if (isFriend) {
                respResult.setCode(IMRespEnum.FRIEND_ALREAD.getCode());
                respResult.setMsg(IMRespEnum.FRIEND_ALREAD.getMsg());
            } else {
                //记录日志
                IMFriendOper oper = new IMFriendOper();
                oper.setOperUserId(imFriend.getSelfUserId());
                oper.setOperedUserId(imFriend.getFriUserId());
                oper.setActionType(IMConstant.FRIEND_OPER_ACTION_ADD);
                imFriendOperMapper.addFriendOperLog(oper);
                //好友请求记入好友表
                imFriend.setFriNickName((imFriend.getFriNickName() != null && StringUtils.isNotEmpty(imFriend.getFriNickName().trim())) ? imFriend.getFriNickName().trim() : friAccount.getRealName());
                IMFriend addEn = new IMFriend();
                addEn.parseToAdd(addEn, imFriend);
                addEn.setSelfNickName(selfEn.getRealName());
                int result = imFriendMapper.addFriend(addEn);
                if (result > 0) {
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.SYS_USER_ADD_FAILD.getCode());
                    respResult.setMsg(IMRespEnum.SYS_USER_ADD_FAILD.getMsg());
                }
            }

        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }

        return respResult;
    }


    @Override
    public RespResult dealBeFriend(Long userId, String sendImId, boolean isAgree) {
        RespResult respResult = new RespResult();
        //查询账户是否有效
        IMAccountInfo friAcc = imAccountInfoService.checkUserExitByUserId(userId);
        IMAccountInfo sendAcc = imAccountInfoService.checkUserExitByImId(sendImId);
        if (friAcc != null && sendAcc != null) {
            //查看当前用户是否是被邀请成为好友
            IMFriend invitedFriend = imFriendMapper.checkInvitedBeFriend(sendAcc.getUserId(), friAcc.getUserId());
            if (null == invitedFriend || Objects.isNull(invitedFriend)) {
                respResult.setCode(IMRespEnum.FRIEND_NOT_INVITED.getCode());
                respResult.setMsg(IMRespEnum.FRIEND_NOT_INVITED.getMsg());
                return respResult;
            } else {
                Object obj = null;
                if (isAgree) {
                    obj = imUserAPI.addFriendSingle(sendImId, friAcc.getImId());
                } else {
                    obj = "rej";
                }
                if (obj != null) {
                    int updFlag = imFriendMapper.agreeBeFriend(friAcc.getUserId(), sendAcc.getUserId(), isAgree);
                    if (updFlag > 0) {
                        respResult.setCode(IMRespEnum.SUCCESS.getCode());
                        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                    } else {
                        respResult.setCode(IMRespEnum.FAIL.getCode());
                        respResult.setMsg(IMRespEnum.FAIL.getMsg());
                    }
                } else {
                    respResult.setCode(IMRespEnum.FAIL.getCode());
                    respResult.setMsg(IMRespEnum.FAIL.getMsg());
                }
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult remFriend(Long selfUserId, Long friendUserId) {
        RespResult respResult = new RespResult();
        //判断是否有效的用户
        IMAccountInfo selfEn = imAccountInfoService.checkUserExitByUserId(selfUserId);
        IMAccountInfo friEn = imAccountInfoService.checkUserExitByUserId(friendUserId);
        if (selfEn != null && friEn != null) {
            //有效用户
            //从系统中删除
            // (是主动加的)
            int result = imFriendMapper.remvFriend(selfUserId, friendUserId);
            //被动加为好友的
            if (result > 0) {
                System.out.println("activeFriend");
            } else {
                result = imFriendMapper.remvFriend(friendUserId, selfUserId);
            }
            if (result > 0) {
                //从环信删除
                Object obj = imUserAPI.deleteFriendSingle(selfEn.getImId(), friEn.getImId());
                if (obj != null) {
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.IM_REMVFRIEND_FAILD.getCode());
                    respResult.setMsg(IMRespEnum.IM_REMVFRIEND_FAILD.getMsg());
                }
            } else {
                respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
                respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult listFriends(Long userId) {
        RespResult respResult = new RespResult();
        IMAccountInfo userEn = imAccountInfoService.checkUserExitByUserId(userId);
        if (userEn != null) {
            List<IMFriendDetailVo> resultList = new ArrayList<>();
            IMFriendDetailVo vo;
            List<IMFriend> activeList = imFriendMapper.listFriendsBySelfId(userId);
            if (activeList != null && activeList.size() > 0) {
                for (IMFriend en : activeList) {
                    vo = new IMFriendDetailVo();
                    vo.setFriendTabId(en.getId());
                    vo.setFriendUserId(en.getFriUserId());
                    vo.setNickName(en.getFriNickName());
                    vo.setFriendType(en.getFriType());
                    IMAccountInfo tempEn = imAccountInfoService.checkUserExitByUserId(en.getFriUserId());
                    vo.setPortrait((OSSClientUtil.getPubUrl(tempEn.getPortrait() != null ? tempEn.getPortrait() : userDefaultPortrait)));
                    vo.setImId(tempEn.getImId());
                    resultList.add(vo);
                }
            }
            List<IMFriend> passingList = imFriendMapper.listFriendsByFriId(userId);
            LinkedHashMap<String, List<IMFriendDetailVo>> map = Maps.newLinkedHashMap();
            List<PinyinResp> pinyinResultList = Lists.newArrayList();
            if (passingList != null && passingList.size() > 0) {
                for (IMFriend en : passingList) {
                    vo = new IMFriendDetailVo();
                    vo.setFriendTabId(en.getId());
                    vo.setFriendUserId(en.getSelfUserId());
                    vo.setFriendType(en.getFriType());
                    IMAccountInfo tempEn = imAccountInfoService.checkUserExitByUserId(en.getSelfUserId());
                    vo.setNickName((en.getFriNickName() != null && StringUtils.isNotEmpty(en.getFriNickName().trim())) ? en.getFriNickName() : tempEn.getRealName());
                    vo.setPortrait(OSSClientUtil.getPubUrl((tempEn.getPortrait() != null && StringUtils.isNotEmpty(tempEn.getPortrait().trim())) ? tempEn.getPortrait() : userDefaultPortrait));
                    vo.setImId(tempEn.getImId());
                    resultList.add(vo);
                }
            }

            Collections.sort(resultList, (IMFriendDetailVo o1, IMFriendDetailVo o2) -> Collator.getInstance(Locale.CHINESE).compare(o1.getNickName(), o2.getNickName()));
            for (IMFriendDetailVo f : resultList
                    ) {
                String sPinyin = StringUtils.upperCase(Utils.getPinYinFirstChar(f.getNickName()));
                if (!Validate.startCheck("[A-Z]", sPinyin)) {
                    sPinyin = "#";
                }
                if (map.containsKey(sPinyin)) {
                    map.get(sPinyin).add(f);
                } else {
                    List<IMFriendDetailVo> list = new ArrayList<>();
                    list.add(f);
                    map.put(sPinyin, list);
                }
            }
            if (map.containsKey("#")) {
                List<IMFriendDetailVo> list = map.get("#");
                map.remove("#");
                map.put("#", list);
            }


            map.entrySet().forEach(u -> {
                if (u.getKey() != null) {
                    pinyinResultList.add(new PinyinResp().setInitial(u.getKey()).setArrays(u.getValue()));
                }
            });

            respResult.setData(pinyinResultList);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult updFriendNickName(Long userId, Long friendUserId, String friendNickName) {
        RespResult respResult = new RespResult();
        IMFriend imFriend = imFriendMapper.findFriendshipBy(userId, friendUserId);
        IMFriend imFriended = imFriendMapper.findFriendshipBy(friendUserId, userId);
        if (imFriend != null || imFriended != null) {
            IMFriend updEn = new IMFriend();
            if (imFriend != null) {
                updEn.setFriNickName(friendNickName);
                updEn.setId(imFriend.getId());
            } else if (imFriended != null) {
                updEn.setSelfNickName(friendNickName);
                updEn.setId(imFriended.getId());
            }
            int result = imFriendMapper.updFriendBy(updEn);
            if (result > 0) {
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            } else {
                respResult.setCode(IMRespEnum.FAIL.getCode());
                respResult.setMsg(IMRespEnum.FAIL.getMsg());
            }

        } else {
            respResult.setCode(IMRespEnum.FRIEND_NOSHIP.getCode());
            respResult.setMsg(IMRespEnum.FRIEND_NOSHIP.getMsg());
        }
        return respResult;
    }

    @Override
    public boolean checkIsFriend(Long userId1, Long userId2) {
        IMAccountInfo userEn1 = imAccountInfoService.checkUserExitByUserId(userId1);
        IMAccountInfo userEn2 = imAccountInfoService.checkUserExitByUserId(userId2);
        boolean isFriend = false;
        if (userEn1 != null && userEn2 != null) {
            //user1 主动方
            IMFriend imFriend = imFriendMapper.findFriendshipBy(userId1, userId2);
            if (imFriend != null && imFriend.getId() > 0) {
                isFriend = true;
            } else {
                //user1 被动方
                imFriend = imFriendMapper.findFriendshipBy(userId2, userId1);
                if (imFriend != null && imFriend.getId() > 0) {
                    isFriend = true;
                }
            }
        }
        return isFriend;
    }
}
