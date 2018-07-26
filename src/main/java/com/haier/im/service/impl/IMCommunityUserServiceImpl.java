package com.haier.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.im.base.IMConstant;
import com.haier.im.base.IMRespEnum;
import com.haier.im.base.OSSClientUtil;
import com.haier.im.base.RespResult;
import com.haier.im.dao.IMCommunityMapper;
import com.haier.im.dao.IMCommunityOperMapper;
import com.haier.im.dao.IMCommunityUserMapper;
import com.haier.im.easemob.api.ChatGroupAPI;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.po.IMCommunity;
import com.haier.im.po.IMCommunityOper;
import com.haier.im.po.IMCommunityUser;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMCommunityUserService;
import com.haier.im.controller.reqvo.ReqCommunityOper;
import com.haier.im.vo.ImCommunityUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IMCommunityUserServiceImpl implements IMCommunityUserService {

    @Autowired
    private IMCommunityUserMapper imCommunityUserMapper;
    @Autowired
    private IMCommunityMapper imCommunityMapper;
    @Autowired
    private IMCommunityOperMapper imCommunityOperMapper;


    @Autowired
    private IMAccountInfoService imAccountInfoService;


    @Autowired
    private ChatGroupAPI chatGroupAPI;


    @Override
    public ImCommunityUserVo getUserInOfCommunity(String communityId, Long userId) {
        IMCommunityUser findUserEn = new IMCommunityUser();
        findUserEn.setCommunityId(communityId);
        findUserEn.setUserId(userId);
        List<ImCommunityUserVo> result = imCommunityUserMapper.findCommunityUserBy(findUserEn);
        if (result != null && result.size() == 1) {
            ImCommunityUserVo user = result.get(0);
            user.setPortrait(OSSClientUtil.getPubUrl(user.getPortrait()));
            return user;
        }
        return null;
    }


    @Override
    public RespResult updUserNickName(String communityId, Long userId, String nickName) {
        RespResult respResult = new RespResult();
        IMAccountInfo userEn = imAccountInfoService.checkUserExitByUserId(userId);
        if (null != userEn) {
            IMCommunity communityEn = imCommunityMapper.findSingleByCommunityId(communityId);
            //修改群中的个人名片
            int result = imCommunityUserMapper.updUserNickNameBySelf(communityId, userId, nickName);
            if (result > 0) {
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    private RespResult addUserToGroup(IMCommunityUser user, IMCommunity imCommunity, Long operUserId) {
        RespResult respResult = new RespResult();
        IMCommunityUser invitee = new IMCommunityUser();
        //增加群组成员记录
        invitee.setUserId(user.getUserId());
        invitee.setNickName(user.getNickName());
        invitee.setIsDelete(false);
        invitee.setCommunityId(user.getCommunityId());
        invitee.setAccountType(user.getAccountType());
        invitee.setUpdateTime(new Date());
        invitee.setCreateTime(new Date());
        invitee.setCreator(user.getUserId() + "");
        invitee.setInviteAuth(true);
        invitee.setState(user.getState());
        int result = imCommunityUserMapper.addCommunityUser(invitee);
        if (result > 0) {
            //增加群组成员变动记录
            IMCommunityOper imCommunityOper = new IMCommunityOper();
            imCommunityOper.setIsDelete(false);
            imCommunityOper.setOperUserId(operUserId);
            imCommunityOper.setOperedUserId(user.getUserId());
            imCommunityOper.setActionType(IMConstant.GROUP_OPER_ACTION_ADD);
            imCommunityOper.setCreator(user.getUserId() + "");

            result = imCommunityOperMapper.insertOper(imCommunityOper);
            if (result > 0) {
                //更新群组当前人数
                imCommunity.setAffiliationsCount(imCommunity.getAffiliationsCount() + 1);
                imCommunity.setUpdateTime(new Date());
                result = imCommunityMapper.updateByPrimaryKeySelective(imCommunity);
                if (result > 0) {
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.INVITE_COMM_COUNT_FAIL.getCode());
                    respResult.setMsg(IMRespEnum.INVITE_COMM_COUNT_FAIL.getMsg());
                }
            } else {
                respResult.setCode(IMRespEnum.INVITE_USER_OPER.getCode());
                respResult.setMsg(IMRespEnum.INVITE_USER_OPER.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.INVITE_USER_FAILD.getCode());
            respResult.setMsg(IMRespEnum.INVITE_USER_FAILD.getMsg());
        }

        return respResult;
    }


    @Override
    public RespResult invitMember(ReqCommunityOper oper) {
        RespResult respResult = new RespResult();
        IMAccountInfo operUser = imAccountInfoService.checkUserExitByUserId(oper.getOperUserId());
        IMAccountInfo operedUser = imAccountInfoService.checkUserExitByUserId(oper.getOperedUserId());
        if (operUser != null && operedUser != null) {
            //邀请者是否在群主中
            if (getUserInOfCommunity(oper.getCommunityId(), oper.getOperUserId()) != null) {
                //判断被邀请者是否在群组中
                if (getUserInOfCommunity(oper.getCommunityId(), oper.getOperedUserId()) == null) {
                    IMCommunity community = imCommunityMapper.findSingleByCommunityId(oper.getCommunityId());
                    if (community != null) {
                        IMCommunityUser invitee = new IMCommunityUser();
                        invitee.setUserId(oper.getOperedUserId());
                        invitee.setNickName(operedUser.getRealName());
                        invitee.setCommunityId(oper.getCommunityId());
                        invitee.setInviteAuth(community.getAllowinvites());
                        invitee.setAccountType(IMConstant.COMMUNITY_ROLE_MEMBER);
                        //判断是否允许普通用户邀请
                        Boolean allowinvites = community.getAllowinvites();
                        if (allowinvites) {
                            //邀请
                            Object obj = chatGroupAPI.addSingleUserToChatGroup(community.getCommunityId(), operedUser.getImId());
                            respResult = this.addUserToGroup(invitee, community, oper.getOperedUserId());
                        } else {
                            //只允许群主邀请
                            if (community.getOwnerUserId().equals(oper.getOperUserId())) {
                                //邀请
                                Object obj = chatGroupAPI.addSingleUserToChatGroup(community.getCommunityId(), operedUser.getImId());
                                if (obj != null) {
                                    respResult = this.addUserToGroup(invitee, community, oper.getOperedUserId());
                                }
                            } else {
                                //本群只允许群主邀请
                                respResult.setCode(IMRespEnum.INVITE_ONLYOWNER.getCode());
                            }

                        }
                    } else {
                        respResult.setCode(IMRespEnum.GROUP_NOTEXIST.getCode());
                        respResult.setMsg(IMRespEnum.GROUP_NOTEXIST.getMsg());
                    }
                } else {
                    //被邀请者已经在群组中
                    respResult.setCode(IMRespEnum.ALREADY_IN_GROUP.getCode());
                    respResult.setMsg(IMRespEnum.ALREADY_IN_GROUP.getMsg());
                }
            } else {
                // 邀请者不在群组中
                respResult.setCode(IMRespEnum.NOT_IN_GROUP.getCode());
                respResult.setMsg(IMRespEnum.NOT_IN_GROUP.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult remvMemberByOwner(ReqCommunityOper oper) {
        RespResult imBaseResp = new RespResult();
        IMCommunity imCommunity = imCommunityMapper.findSingleByCommunityId(oper.getCommunityId());
        //判断当前用户是否是群主
        if (imCommunity.getOwnerUserId().equals(oper.getOperUserId())
                && !oper.getOperUserId().equals(oper.getOperedUserId())) {
            //判断用户是否初始化环信
            ImCommunityUserVo imUser = new ImCommunityUserVo();
            imUser.setUserId(oper.getOperedUserId());//用户id
            imUser.setCommunityId(oper.getCommunityId());
            IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByUserId(oper.getOperedUserId());
            IMCommunityUser findEn = new IMCommunityUser();
            findEn = imUser.parseVoTo(findEn, imUser);
            List<ImCommunityUserVo> users = imCommunityUserMapper.findCommunityUserBy(findEn);
            if (imAccountInfo != null && users != null && users.size() == 1) {
                imUser = users.get(0);
                //IM账户已激活，需要删除IM环信服务器数据
                boolean d = false;
                if (imUser.getState() == IMConstant.COMMUNITY_USER_EXIST) {
                    Object obj = chatGroupAPI.removeSingleUserFromChatGroup(imUser.getCommunityId(), imAccountInfo.getImId());
                    if (null != obj) {
                        d = true;
                    }
                }
                if (imUser.getState() != IMConstant.ACCOUNT_STATUS_ACTIVATE || d) {
                    imBaseResp = delCommunityUser(oper, imCommunity, imUser.parseVoTo(new IMCommunityUser(), imUser));

                } else {
                    imBaseResp.setCode(IMRespEnum.SYS_DATA_ERROR.getCode());
                    imBaseResp.setMsg(IMRespEnum.SYS_DATA_ERROR.getMsg());
                }
            } else {
                //该用户不存在，或者已经删除
                imBaseResp.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
                imBaseResp.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
            }
        } else {
            imBaseResp.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
            imBaseResp.setMsg(IMRespEnum.GROUP_ACC_NOPOWER.getMsg());
        }
        return imBaseResp;
    }

    private RespResult delCommunityUser(ReqCommunityOper oper, IMCommunity imCommunity, IMCommunityUser imUser) {
        RespResult imBaseResp = new RespResult();
        int result = imCommunityUserMapper.delCommunityUserBy(imUser);
        if (result > 0) {
            //添加群组动作
            IMCommunityOper imCommunityOper = new IMCommunityOper();
            imCommunityOper.setOperUserId(oper.getOperUserId());
            imCommunityOper.setActionType(IMConstant.GROUP_OPER_ACTION_REMOVE);
            imCommunityOper.setOperedUserId(oper.getOperedUserId());
            result = imCommunityOperMapper.insertOper(imCommunityOper);
            if (result > 0) {
                //修改群组现有人数
                imCommunity.setAffiliationsCount(imCommunity.getAffiliationsCount() - 1);
                result = imCommunityMapper.updateByPrimaryKeySelective(imCommunity);
                if (result > 0) {
                    imBaseResp.setCode(IMRespEnum.SUCCESS.getCode());
                    imBaseResp.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    imBaseResp.setCode(IMRespEnum.USER_REV_FAIL.getCode());
                    imBaseResp.setMsg(IMRespEnum.USER_REV_FAIL.getMsg());
                }
            } else {
                imBaseResp.setCode(IMRespEnum.USER_REV_OPER_FAIL.getCode());
                imBaseResp.setMsg(IMRespEnum.USER_REV_OPER_FAIL.getMsg());
            }
        } else {
            imBaseResp.setCode(IMRespEnum.USER_MOD_STATUS_FAIL.getCode());
            imBaseResp.setMsg(IMRespEnum.USER_MOD_STATUS_FAIL.getMsg());
        }
        return imBaseResp;
    }


    @Override
    public RespResult joinCommunity(String communityId, Long joinUserId) {
        RespResult respResult = new RespResult();
        //用户是否存在
        IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByUserId(joinUserId);
        if (imAccountInfo != null) {
            //群组是否有效
            IMCommunity community = imCommunityMapper.findSingleByCommunityId(communityId);
            if (community != null && community.getId() > 0) {
                //是否已经在群中
                ImCommunityUserVo communityUser = getUserInOfCommunity(communityId, joinUserId);
                if (communityUser == null) {
                    //加入群组
                    Object obj = chatGroupAPI.addSingleUserToChatGroup(community.getCommunityId(), imAccountInfo.getImId());
                    if (obj != null) {
                        IMCommunityUser joiner = new IMCommunityUser();
                        joiner.setUserId(joinUserId);
                        joiner.setNickName(imAccountInfo.getRealName());
                        joiner.setCommunityId(communityId);
                        joiner.setInviteAuth(community.getAllowinvites());
                        joiner.setAccountType(IMConstant.COMMUNITY_ROLE_MEMBER);
                        respResult = this.addUserToGroup(joiner, community, joinUserId);
                    }
                } else {
                    respResult.setCode(IMRespEnum.ALREADY_IN_GROUP.getCode());
                    respResult.setMsg(IMRespEnum.ALREADY_IN_GROUP.getMsg());
                }
            } else {
                respResult.setCode(IMRespEnum.GROUP_NOTEXIST.getCode());
                respResult.setMsg(IMRespEnum.GROUP_NOTEXIST.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult leaveCommunity(String communityId, Long userId) {
        RespResult respResult = new RespResult();
        IMAccountInfo imAccountInfo = imAccountInfoService.checkUserExitByUserId(userId);
        if (imAccountInfo != null) {
            IMCommunity community = imCommunityMapper.findSingleByCommunityId(communityId);
            if (community != null) {
                //用户是否在群组中
                IMCommunityUser findEn = new IMCommunityUser();
                findEn.setUserId(userId);
                findEn.setCommunityId(communityId);
                List<ImCommunityUserVo> listuser = imCommunityUserMapper.findCommunityUserBy(findEn);
                if (listuser != null && listuser.size() > 0) {
                    if (listuser.size() == 1) {
                        ImCommunityUserVo user = listuser.get(0);
                        //是否是群主，群主不能退群
                        if (community.getOwnerUserId().equals(userId)) {
                            respResult.setCode(IMRespEnum.OWNER_LEAVE_ERROR.getCode());
                            respResult.setMsg(IMRespEnum.OWNER_LEAVE_ERROR.getMsg());
                        } else {

                            ReqCommunityOper oper = new ReqCommunityOper();
                            oper.setCommunityId(communityId);
                            oper.setOperUserId(userId);
                            oper.setOperedUserId(userId);
                            respResult = this.delCommunityUser(oper, community, user.parseVoTo(new IMCommunityUser(), user));
                            Object obj = chatGroupAPI.removeSingleUserFromChatGroup(communityId, imAccountInfo.getImId());
                        }
                    } else {
                        respResult.setCode(IMRespEnum.SYS_DATA_ERROR.getCode());
                        respResult.setMsg(IMRespEnum.SYS_DATA_ERROR.getMsg());
                    }
                } else {
                    respResult.setData("用户不在群组中");
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                }
            } else {
                respResult.setData("群组不存在");
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    @Override
    public boolean checkUserInEasemoGroup(String userImId, String groupId) {
        Object listObj = chatGroupAPI.getChatGroupUsers(groupId);
        if (listObj != null && userImId != null) {
            JSONObject json = JSON.parseObject(listObj.toString());
            JSONArray members = json.getJSONArray("data");
            for (int i = 0; i < members.size(); i++) {
                JSONObject user = members.getJSONObject(i);
                if (user.containsKey("member")) {
                    String memberId = user.getString("member");
                    if (userImId.equals(memberId)) {
                        return true;
                    }
                }
            }
            System.out.println("=====" + json.toString());
        }
        return false;
    }
}