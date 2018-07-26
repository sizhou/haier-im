package com.haier.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.haier.im.base.*;
import com.haier.im.dao.IMCommunityMapper;
import com.haier.im.dao.IMCommunityUserMapper;
import com.haier.im.easemob.api.ChatGroupAPI;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.po.IMCommunity;
import com.haier.im.po.IMCommunityUser;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMCommunityService;
import com.haier.im.service.IMCommunityUserService;
import com.haier.im.vo.IMCommunityVo;
import com.haier.im.vo.ImCommunityUserVo;
import com.haier.im.controller.reqvo.ReqCommunity;
import io.swagger.client.model.Group;
import io.swagger.client.model.ModifyGroup;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class IMCommunityServiceImpl implements IMCommunityService {

    @Autowired
    private IMAccountInfoService imAccountInfoService;
    @Autowired
    private IMCommunityUserService imCommunityUserService;

    @Autowired
    private IMCommunityMapper imCommunityMapper;

    @Autowired
    private IMCommunityUserMapper imCommunityUserMapper;

    @Autowired
    private ChatGroupAPI chatGroupAPI;


    @Override
    @Transactional
    public RespResult createCommunity(String communityName, Long ownerUserId, MultipartFile iconFile) {
        RespResult respResult = new RespResult();
        IMAccountInfo owner = imAccountInfoService.checkUserExitByUserId(ownerUserId);
        if (owner != null) {
            Group group = new Group();
            group.setApproval(false);//加入公开群是否需要批准，默认值是false（加入公开群不需要群主批准），此属性为必选的，私有群必须为true
            group.setDesc("普通群");//群组描述，此属性为必须的
            group.setGroupname(communityName);//群组名称，此属性为必须的。
            group.setMaxusers(500);//群组成员最大数（包括群主），值为数值类型，默认值200，最大值2000，此属性为可选的
            group.setOwner(owner.getImId());//群组的管理员，此属性为必须的
            group.setPublic(true);//是否是公开群，此属性为必须的
            //环信创建群组
            Object groupObj = chatGroupAPI.createChatGroup(group);
            if (null != groupObj) {
                JSONObject groupJson = JSON.parseObject(groupObj.toString());
                JSONObject data = groupJson.getJSONObject("data");
                String imGroupId = data.getString("groupid");
                //群组记录到群组表
                IMCommunity addEn = new IMCommunity();
                addEn.setAffiliationsCount(1);
                addEn.setOwnerUserId(ownerUserId);
                addEn.setCommunityId(imGroupId);
                addEn.setCreateTime(new Date());
                addEn.setCreator(owner.getRealName());
                addEn.setUpdateTime(new Date());
                addEn.setCommunityName(communityName);
                //上传群组图标
                String portraitKey = OSSClientUtil.uploadImg2Oss(iconFile, true, addEn.getCommunityIcon());
                addEn.setCommunityIcon(portraitKey);
                int communityResult = imCommunityMapper.addCommunity(addEn);
                if (communityResult > 0) {
                    //群主作为群用户加入群用户表
                    IMCommunityUser addUser = new IMCommunityUser();
                    addUser.setAccountType(IMConstant.COMMUNITY_ROLE_OWNER);
                    addUser.setCommunityId(imGroupId);
                    addUser.setInviteAuth(true);
                    addUser.setNickName(owner.getRealName());
                    addUser.setUserId(owner.getUserId());
                    addUser.setState(IMConstant.COMMUNITY_USER_EXIST);
                    addUser.setCreateTime(new Date());
                    addUser.setCreator(owner.getRealName());
                    addUser.setUpdateTime(new Date());
                    int communitUserResult = imCommunityUserMapper.addCommunityUser(addUser);
                    if (communitUserResult > 0) {
                        respResult.setCode(IMRespEnum.SUCCESS.getCode());
                        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                    } else {
                        respResult.setCode(IMRespEnum.DEF_GROUP_PERSON_ADD_FAIL.getCode());
                        respResult.setMsg(IMRespEnum.DEF_GROUP_PERSON_ADD_FAIL.getMsg());
                    }
                } else {
                    respResult.setCode(IMRespEnum.GROUP_ADD_FAIL.getCode());
                    respResult.setCode(IMRespEnum.GROUP_ADD_FAIL.getMsg());
                }
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult modCommunity(ReqCommunity reqCommunity) {
        RespResult respResult = new RespResult();
        IMCommunity communityDB = imCommunityMapper.findSingleByCommunityId(reqCommunity.getCommunityId());
        if (communityDB != null) {
            //判断具有修改社群权限
            if (communityDB.getOwnerUserId().equals(reqCommunity.getOwnerUserId())) {
                //有修改权限
                if (StringUtils.isNotBlank(reqCommunity.getCommunityDesc())
                        || StringUtils.isNotBlank(reqCommunity.getCommunityName())
                        || StringUtils.isNotBlank(reqCommunity.getCommunityNotice())) {
                    //修改环信社群信息
                    ModifyGroup modifyGroup = new ModifyGroup();
                    modifyGroup.setDescription(reqCommunity.getCommunityDesc());
                    modifyGroup.setGroupname(reqCommunity.getCommunityName());
                    Object obj = chatGroupAPI.modifyChatGroup(reqCommunity.getCommunityId(), modifyGroup);
                    if (null != obj) {
                        //修改社群信息
                        IMCommunity community = new IMCommunity();
                        community = community.parseReqTo(community, reqCommunity);
                        int result = imCommunityMapper.updateByPrimaryKeySelective(community);
                        if (result > 0) {
                            respResult.setCode(IMRespEnum.SUCCESS.getCode());
                            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                        } else {
                            respResult.setCode(IMRespEnum.GROUP_MOD_FAIL.getCode());
                            respResult.setMsg(IMRespEnum.GROUP_MOD_FAIL.getMsg());
                        }
                    } else {
                        respResult.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
                        respResult.setMsg(IMRespEnum.GROUP_ACC_NOPOWER.getMsg());
                    }
                } else {
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                }
            } else {
                respResult.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
                respResult.setMsg(IMRespEnum.GROUP_ACC_NOPOWER.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.GROUP_NOTEXIST.getCode());
            respResult.setMsg(IMRespEnum.GROUP_NOTEXIST.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult modIconForCommunity(String communityId, Long ownerUserId, MultipartFile iconFile) {
        RespResult respResult = new RespResult();
        IMCommunity communityDB = imCommunityMapper.findSingleByCommunityId(communityId);
        if (communityDB != null) {
            //判断具有修改社群权限
            if (communityDB.getOwnerUserId().equals(ownerUserId)) {
                //上传社群头像
                String portraitKey = OSSClientUtil.uploadImg2Oss(iconFile, true, communityDB.getCommunityIcon());
                IMCommunity updEn = new IMCommunity().setCommunityId(communityId).setCommunityIcon(portraitKey);
                int result = imCommunityMapper.updateByPrimaryKeySelective(updEn);
                if (result > 0) {
                    respResult.setData(Maps.newHashMap().put("portrait", portraitKey));
                    respResult.setCode(IMRespEnum.SUCCESS.getCode());
                    respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                } else {
                    respResult.setCode(IMRespEnum.GROUP_MOD_FAIL.getCode());
                    respResult.setMsg(IMRespEnum.GROUP_MOD_FAIL.getMsg());
                }
            } else {
                respResult.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
                respResult.setMsg(IMRespEnum.GROUP_ACC_NOPOWER.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.GROUP_NOTEXIST.getCode());
            respResult.setMsg(IMRespEnum.GROUP_NOTEXIST.getMsg());
        }
        return respResult;
    }

    @Override
    public RespResult delCommunity(String communityId, Long userId) {
        RespResult respResult = new RespResult();
        //判断群是否存在，群主是否为当前用户
        IMCommunity community = imCommunityMapper.findSingleByCommunityId(communityId);
        if (community != null) {
            //当前userId是否是群主id
            if (community.getOwnerUserId() != null && community.getOwnerUserId().equals(userId)) {
                //调用环信服务删除群组
                Object obj = chatGroupAPI.deleteChatGroup(communityId);
                if (null != obj) {
                    //修改社群信息
                    IMCommunity delEn = new IMCommunity();
                    delEn.setCommunityId(communityId);
                    int result = imCommunityMapper.updDelBy(delEn);
                    if (result > 0) {
                        //删除该社群的所有成员
                        IMCommunityUser delUser = new IMCommunityUser();
                        delUser.setCommunityId(communityId);
                        int resultFlag = imCommunityUserMapper.delCommunityUserBy(delUser);
                        if (resultFlag > 0) {
                            respResult.setCode(IMRespEnum.SUCCESS.getCode());
                            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
                        }
                    }
                }
            } else {
                respResult.setCode(IMRespEnum.GROUP_ACC_NOPOWER.getCode());
                respResult.setMsg(IMRespEnum.GROUP_ACC_NOPOWER.getMsg());
            }
        } else {
            respResult.setCode(IMRespEnum.GROUP_NOTEXIST.getCode());
            respResult.setMsg(IMRespEnum.GROUP_NOTEXIST.getMsg());
        }
        return respResult;
    }


    @Override
    public RespResult searchCommunityByStr(String searchStr, Long userId) {
        RespResult respResult = new RespResult();
        if (imAccountInfoService.checkUserExitByUserId(userId) != null) {
            //群基本信息
            IMCommunityVo findEn = new IMCommunityVo();
            findEn.setSearchStr("%" + searchStr + "%");
            List<IMCommunity> result = imCommunityMapper.findCommunitiesByCondition(findEn);
            if (result != null && result.size() > 0) {
                //处理群组图标
                result.stream().forEach(c -> {
                    c.setCommunityIcon(OSSClientUtil.getPubUrl(c.getCommunityIcon()));
                });
                respResult.setData(result);
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
            } else {
                //没有符合的群组
                respResult.setData(null);
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg() + "没有符合的群");
            }
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }

        return respResult;
    }

    @Override
    public RespResult findCommunityOfInByUserId(Long userId) {
        RespResult respResult = new RespResult();
        if (imAccountInfoService.checkUserExitByUserId(userId) != null) {
            List<IMCommunity> result = null;
            IMCommunityUser findEn = new IMCommunityUser();
            findEn.setUserId(userId);
            List<ImCommunityUserVo> communityUserList = imCommunityUserMapper.findCommunityUserBy(findEn);
            if (communityUserList != null && communityUserList.size() > 0) {
                List<String> idList = communityUserList.stream().map(ImCommunityUserVo::getCommunityId)
                        .collect(Collectors.toList());
                if (idList != null && idList.size() > 0) {
                    result = imCommunityMapper.findCommunitiesByIn(idList);
                }
            }
            if (null != result && Objects.nonNull(respResult)) {
                result.stream().forEach(c -> {
                    c.setCommunityIcon(OSSClientUtil.getPubUrl(c.getCommunityIcon()));
                });
            }
            respResult.setData(result);
            respResult.setCode(IMRespEnum.SUCCESS.getCode());
            respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        } else {
            respResult.setCode(IMRespEnum.SYS_USER_NOTEXIT.getCode());
            respResult.setMsg(IMRespEnum.SYS_USER_NOTEXIT.getMsg());
        }
        return respResult;
    }

    //推荐最新的
    @Override
    public RespResult recommendCommunityForUser(Integer conditionLimit) {
        RespResult respResult = new RespResult();
        //推荐群
        IMCommunityVo condition = new IMCommunityVo();
        condition.setConditionLimit(conditionLimit);
        condition.setOrderClause("create_time DESC");//最新的
        //TODO
        List<IMCommunity> result = imCommunityMapper.findCommunitiesByCondition(condition);
        if (result != null && result.size() > 0) {
            result.stream().forEach(community -> community.setCommunityIcon(OSSClientUtil.getPubUrl(community.getCommunityIcon())));
        }
        respResult.setData(result);
        respResult.setCode(IMRespEnum.SUCCESS.getCode());
        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        return respResult;
    }

    //人数最多
    @Override
    public RespResult findHosttestCommunitys(Integer conditionLimit) {
        RespResult respResult = new RespResult();
        IMCommunityVo condition = new IMCommunityVo();
        condition.setConditionLimit(conditionLimit);
        condition.setOrderClause("affiliations_count desc");//人数最多
        //TODO
        List<IMCommunity> result = imCommunityMapper.findCommunitiesByCondition(condition);
        if (result != null && result.size() > 0) {
            result.stream().forEach(community -> community.setCommunityIcon(OSSClientUtil.getPubUrl(community.getCommunityIcon())));
        }
        respResult.setData(result);
        respResult.setCode(IMRespEnum.SUCCESS.getCode());
        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        return respResult;
    }

    @Override
    public RespResult getCommunityDetail(String communityId, Long userId) {
        RespResult respResult = new RespResult();
        //用户是否有效
        if (imAccountInfoService.checkUserExitByUserId(userId) != null) {
            //判断群是否存在
            IMCommunity community = imCommunityMapper.findSingleByCommunityId(communityId);
            if (community != null) {
                IMCommunityVo resultData = new IMCommunityVo();
                resultData = resultData.parsePoTo(resultData, community);
                //处理图片
                resultData.setCommunityIcon(OSSClientUtil.getPubUrl(resultData.getCommunityIcon()));
                //群成员列表
                IMCommunityUser findUserEn = new IMCommunityUser();
                findUserEn.setCommunityId(communityId);
                Map<String, Object> memberMap = new HashMap<>();
                List<ImCommunityUserVo> managerList = new ArrayList<>();
                List<ImCommunityUserVo> memList = new ArrayList<>();
                List<ImCommunityUserVo> alluserList = imCommunityUserMapper.findCommunityUserBy(findUserEn);
                //按类型分组
                if (alluserList != null & alluserList.size() > 0) {
                    for (ImCommunityUserVo u : alluserList
                            ) {
                        u.setPortrait(OSSClientUtil.getPubUrl(u.getPortrait()));
                        Integer roleType = u.getAccountType();
                        if (roleType.equals(IMConstant.COMMUNITY_ROLE_OWNER)) {
                            memberMap.put(IMConstant.ROLE_NAME_OWNER, u);
                        } else if (roleType.equals(IMConstant.ROLE_NAME_MANAGER)) {
                            managerList.add(u);
                        } else {
                            memList.add(u);
                        }
                    }
                }
                memberMap.put(IMConstant.ROLE_NAME_MANAGER, managerList);
                memberMap.put(IMConstant.ROLE_NAME_MEMBER, memList);
                resultData.setMembers(memberMap);
                //当前用户是否已加入
                Boolean isMember = false;
                if (imCommunityUserService.getUserInOfCommunity(communityId, userId) != null) {
                    isMember = true;
                }
                resultData.setMembered(isMember);
                respResult.setData(resultData);
                respResult.setCode(IMRespEnum.SUCCESS.getCode());
                respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
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
}
