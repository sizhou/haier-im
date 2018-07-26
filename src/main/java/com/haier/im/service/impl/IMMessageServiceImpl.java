package com.haier.im.service.impl;

import com.google.common.collect.Lists;
import com.haier.im.base.IMConstant;
import com.haier.im.base.IMRespEnum;
import com.haier.im.base.OSSClientUtil;
import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.ChatFileResp;
import com.haier.im.controller.reqvo.ChatMessageResp;
import com.haier.im.controller.reqvo.MessageData;
import com.haier.im.dao.IMCommunityUserMapper;
import com.haier.im.dao.IMFriendMapper;
import com.haier.im.dao.IMMsgFileMapper;
import com.haier.im.dao.IMMsgSendMapper;
import com.haier.im.po.*;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMMessageService;
import com.haier.im.vo.IMMessageVo;
import com.haier.im.vo.IMMsgFileVo;
import com.haier.im.vo.ImCommunityUserVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class IMMessageServiceImpl implements IMMessageService {

    @Autowired
    private IMAccountInfoService imAccountInfoService;

    @Resource
    private IMFriendMapper imFriendMapper;
    @Resource
    private IMCommunityUserMapper imCommunityUserMapper;
    @Resource
    private IMMsgSendMapper imMsgSendMapper;
    @Resource
    private IMMsgFileMapper imMsgFileMapper;


    public MessageData dealMsgTypeToData(IMMessageVo c) {
        MessageData messageData = new MessageData();
        messageData.setMsg_id(c.getMessageId());
        messageData.setMsgType(c.getMessageType());
        messageData.setFrom(c.getSenderId());
        messageData.setTo(c.getRecverId());
        messageData.setSend_time(c.getSendTime());
        messageData.setDirection(c.getDirection());
        messageData.setNickname(c.getSenderNickName());
        messageData.setPortrait(c.getSenderPortrait() == null ? null : OSSClientUtil.getPubUrl(c.getSenderPortrait()));
//                messageData.setSend_time(null == c.getSendTime() ? null : DateUtil.toFormatDateString(c.getSendTime(), "yyyy-MM-dd HH:mm:ss"));
        if (c.getMessageType() == IMConstant.MESSAGE_TYPE_TXT) {
            messageData.setMsgDetail(c.getMessageDetail());
        } else if (c.getMessageType() == IMConstant.MESSAGE_TYPE_LOC) {
            messageData.setAddr(c.getAddr());
            messageData.setLat(c.getLat());
            messageData.setLng(c.getLng());
        } else {
            //查询消息文件
            IMMsgFile imMsgFile = imMsgFileMapper.findFileByMessageId(c.getMessageId());
            messageData.setFile_length(imMsgFile.getFileLength());
            messageData.setFilename(imMsgFile.getFileName());
            messageData.setSize(imMsgFile.getSize());
            if (c.getMessageType() == IMConstant.MESSAGE_TYPE_AUDIO || c.getMessageType() == IMConstant.MESSAGE_TYPE_VIDEO) {
                messageData.setLength(imMsgFile.getLength());
            }
            if (c.getMessageType() == IMConstant.MESSAGE_TYPE_VIDEO) {
//                        messageData.setThumb_url(StringUtils.isNotBlank(imMsgFile.getThumbOssUrl()) ? imMsgFile.getThumbOssUrl() : imMsgFile.getThumbEaseUrl());
                if (StringUtils.isNotBlank(imMsgFile.getThumbOssUrl())) {
                    messageData.setThumbUrlOss(OSSClientUtil.getPriUrl(imMsgFile.getThumbOssUrl()));
                } else {
                    messageData.setThumb_url(imMsgFile.getThumbEaseUrl());
                    messageData.setThumb_secret(imMsgFile.getThumbSecret());
                }
            }
            if (StringUtils.isNotBlank(imMsgFile.getOssUrl())) {
                messageData.setUrlOss(OSSClientUtil.getPriUrl(imMsgFile.getOssUrl()));
            } else {
                messageData.setUrl(imMsgFile.getEaseUrl());
                messageData.setSecret(imMsgFile.getSecret());
            }
//                    messageData.setUrl(StringUtils.isNotBlank(imMsgFile.getOssUrl()) ? imMsgFile.getOssUrl() : imMsgFile.getEaseUrl());
        }
        return messageData;
    }


    @Override
    public RespResult msgs(IMMessageVo messageVo) {
        RespResult respResult = new RespResult();
        int size = Integer.valueOf(messageVo.getPageSize());
        int page = Integer.valueOf(messageVo.getPageNo());
        Integer startIndex = size * (page - 1);
        //当前用户是否有效
        IMAccountInfo accountInfo = imAccountInfoService.checkUserExitByUserId(messageVo.getUserId());
        if (accountInfo != null) {
            List<IMMessageVo> tempDetailList = Lists.newArrayList();
            ChatMessageResp chatMessageResp = new ChatMessageResp();
            List<MessageData> messageDataList = Lists.newArrayList();
            //判断当前用户与获取消息的另一方的关系（群组时需要看到所有群组历史消息，为好友时看到双方消息）
            if (messageVo.getSendType().equals(1)) {
                //群聊:用户是否在群组中有查看权限
                IMCommunityUser findEn = new IMCommunityUser().setCommunityId(messageVo.getMessageUserId()).setUserId(messageVo.getUserId());
                List<ImCommunityUserVo> communityUserVos = imCommunityUserMapper.findCommunityUserBy(findEn);
                if (communityUserVos != null && communityUserVos.size() > 0 && Objects.nonNull(communityUserVos.get(0))) {
                    //所有recv为群组id的
                    tempDetailList = imMsgSendMapper.findCommunityMsgs(messageVo.getMessageUserId(), size, startIndex,messageVo.getStartDate(),messageVo.getEndDate());
                    int count = imMsgSendMapper.findCommunityMsgsCount(messageVo.getMessageUserId(),messageVo.getStartDate(),messageVo.getEndDate());
                    if (tempDetailList != null) {
                        for (IMMessageVo vo : tempDetailList
                                ) {
                            messageDataList.add(this.dealMsgTypeToData(vo));
                        }
                    }
                    chatMessageResp = new ChatMessageResp();
                    chatMessageResp.setData(messageDataList);
                    chatMessageResp.setPageNo(messageVo.getPageNo());
                    chatMessageResp.setPageSize(size);
                    chatMessageResp.setTotal(count);
                    respResult.setData(chatMessageResp);
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.FILE_NOPOWER_FAIL.getCode());
                    respResult.setMsg(IMRespEnum.FILE_NOPOWER_FAIL.getMsg());
                }
            } else if (messageVo.getSendType().equals(2)) {
                //单聊:另一用户是否有效用户
                IMAccountInfo otherUser = imAccountInfoService.checkUserExitByImId(messageVo.getMessageUserId());
                if (otherUser != null) {
                    //判断两人是否是好友
                    IMFriend friend1 = imFriendMapper.findFriendshipBy(accountInfo.getUserId(), otherUser.getUserId());
                    IMFriend friend2 = imFriendMapper.findFriendshipBy(otherUser.getUserId(), accountInfo.getUserId());
                    if (friend1 != null || friend2 != null) {
                        //recv为个人或者sender为个人
                        IMMessageVo vo = null;
                        List<IMMsgSend> msgList = imMsgSendMapper.findMsgsByChatImId(accountInfo.getImId(), otherUser.getImId(), size, startIndex,messageVo.getStartDate(),messageVo.getEndDate());
                        int count = imMsgSendMapper.findMsgsCountForChatIm(accountInfo.getImId(), otherUser.getImId(),messageVo.getStartDate(),messageVo.getEndDate());
                        if (msgList != null && msgList.size() > 0) {
                            for (IMMsgSend msg : msgList
                                    ) {
                                if (msg != null) {
                                    vo = new IMMessageVo();
                                    vo = vo.parsePoTo(vo, msg);
                                    messageDataList.add(dealMsgTypeToData(vo));
                                }
                            }
                        }
                        chatMessageResp = new ChatMessageResp();
                        chatMessageResp.setPageNo(messageVo.getPageNo());
                        chatMessageResp.setPageSize(size);
                        chatMessageResp.setTotal(count);
                        chatMessageResp.setData(messageDataList);
                        respResult.setData(chatMessageResp);
                        respResult.setCode(IMRespEnum.SUCCESS.getCode());
                        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                    } else {
                        respResult.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
                        respResult.setMsg("不是好友关系");
                    }
                } else {
                    //单聊时候另一方不是有效账户
                    respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
                    respResult.setMsg("单聊历史消息时，另一方" + IMRespEnum.SYS_USER_NOTEXIT.getMsg());
                }
            } else {
                //暂不支持该聊天类型
                respResult.setCode(IMRespEnum.SEND_TYPE_ERROR.getCode());
                respResult.setMsg(IMRespEnum.SEND_TYPE_ERROR.getMsg());

            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult listChatFiles(IMMessageVo messageVo) {
        RespResult respResult = new RespResult();
        int size = Integer.valueOf(messageVo.getPageSize());
        int page = Integer.valueOf(messageVo.getPageNo());
        Integer startIndex = size * (page - 1);
        ChatFileResp resultData = new ChatFileResp();
        resultData.setPageNo(messageVo.getPageNo());
        resultData.setPageSize(messageVo.getPageSize());
        //当前用户是否有效
        IMAccountInfo accountInfo = imAccountInfoService.checkUserExitByUserId(messageVo.getUserId());
        if (accountInfo != null) {
            IMMsgFileVo vo = null;
            //判断当前用户与获取消息的另一方的关系（群组时需要看到所有群组历史消息，为好友时看到双方消息）
            if (messageVo.getSendType().equals(1)) {
                //群聊:用户是否在群组中有查看权限
                IMCommunityUser findEn = new IMCommunityUser().setCommunityId(messageVo.getMessageUserId()).setUserId(messageVo.getUserId());
                List<ImCommunityUserVo> communityUserVos = imCommunityUserMapper.findCommunityUserBy(findEn);
                if (communityUserVos != null && communityUserVos.size() > 0 && Objects.nonNull(communityUserVos.get(0))) {
                    //所有recv为群组id的
                    //群组文件
                    List<IMMsgFileVo> fileList = imMsgFileMapper.findMsgFilesForCommunity(messageVo.getMessageUserId(), size, startIndex);
                    if (null != fileList && fileList.size() > 0) {
                        fileList.stream().forEach(f -> {
                            if (f.getOssUrl() != null && StringUtils.isNotEmpty(f.getOssUrl().trim())) {
                                f.setOssUrl(OSSClientUtil.getPriUrl(f.getOssUrl()));
                            }
                        });
                    }
                    int count = imMsgFileMapper.findCommunityFileCount(messageVo.getMessageUserId());
                    resultData.setTotal(count);
                    resultData.setFiles(fileList);
                    respResult.setData(resultData);
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.FILE_NOPOWER_FAIL.getCode());
                    respResult.setMsg(IMRespEnum.FILE_NOPOWER_FAIL.getMsg());
                }
            } else if (messageVo.getSendType().equals(2)) {
                IMAccountInfo otherUser = imAccountInfoService.checkUserExitByImId(messageVo.getMessageUserId());
                if (otherUser != null) {
                    IMFriend friend1 = imFriendMapper.findFriendshipBy(accountInfo.getUserId(), otherUser.getUserId());
                    IMFriend friend2 = imFriendMapper.findFriendshipBy(otherUser.getUserId(), accountInfo.getUserId());
                    if (friend1 != null || friend2 != null) {
                        String selfNickName, friendNickName;
                        if (friend1 != null) {
                            selfNickName = (friend1.getSelfNickName() != null && StringUtils.isNotEmpty(friend1.getSelfNickName().trim())) ? friend1.getSelfNickName() : accountInfo.getRealName();
                            friendNickName = (friend1.getFriNickName() != null && StringUtils.isNotEmpty(friend1.getFriNickName().trim())) ? friend1.getFriNickName() : otherUser.getRealName();
                        } else {
                            selfNickName = (friend2.getFriNickName() != null && StringUtils.isNotEmpty(friend2.getFriNickName().trim())) ? friend2.getFriNickName() : accountInfo.getRealName();
                            friendNickName = (friend2.getSelfNickName() != null && StringUtils.isNotEmpty(friend2.getSelfNickName().trim())) ? friend2.getSelfNickName() : otherUser.getRealName();
                        }
                        //聊天文件
                        List<IMMsgFileVo> fileList1 = imMsgFileMapper.findMsgFilesForChatUserId(accountInfo.getImId(), otherUser.getImId(), size, startIndex);
                        int coun = imMsgFileMapper.findUserFileCount(accountInfo.getImId(), otherUser.getImId());
                        if (fileList1 != null && fileList1.size() > 0) {
                            fileList1.stream().forEach(f -> {
                                if (f.getSenderImId() != null && StringUtils.isNotEmpty(f.getSenderImId().trim())) {
                                    if (f.getSenderImId().equals(otherUser.getImId())) {
                                        f.setSenderNickName(friendNickName);
                                    } else if (f.getSenderImId().equals(accountInfo.getImId())) {
                                        f.setSenderNickName(selfNickName);
                                    }
                                }
                            });
                        }
                        resultData.setFiles(fileList1);
                        resultData.setTotal(coun);
                        respResult.setData(resultData);
                        respResult.setCode(IMRespEnum.SUCCESS.getCode());
                        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                        //TODO
                    } else {
                        respResult.setCode(IMRespEnum.FRIEND_NOSHIP.getCode());
                        respResult.setMsg(IMRespEnum.FRIEND_NOSHIP.getMsg());
                    }
                } else {
                    //单聊时候另一方不是有效账户
                    respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
                    respResult.setMsg("单聊历史文件时，另一方" + IMRespEnum.SYS_USER_NOTEXIT.getMsg());
                }
            } else {
                //暂不支持该聊天类型
                respResult.setCode(IMRespEnum.SEND_TYPE_ERROR.getCode());
                respResult.setMsg(IMRespEnum.SEND_TYPE_ERROR.getMsg());

            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

}
