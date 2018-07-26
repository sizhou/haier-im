package com.haier.im.controller;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.service.IMAccountTokenService;
import com.haier.im.service.IMCommunityUserService;
import com.haier.im.controller.reqvo.ReqCommunityOper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "群用户管理")
@RestController
@RequestMapping("im/communityuser")
public class IMCommunityUserController {

    @Autowired
    private IMCommunityUserService imCommunityUserService;
    @Autowired
    private IMAccountTokenService imAccountTokenService;


    @ApiOperation(value = "修改群名片", httpMethod = "PUT", notes = "改群名片")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/mod/selfcard", method = RequestMethod.PUT, produces = "application/json")
    public RespResult modCommunityCard(@ApiParam(value = "群组id", required = true) @RequestParam String communityId,
                                       @ApiParam(value = "昵称", required = true) @RequestParam String nickName,
                                       @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (communityId != null && userId != null && nickName != null && StringUtils.isNotEmpty(nickName.trim())) {
            respResult = imCommunityUserService.updUserNickName(communityId, userId, nickName);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "邀请成员", httpMethod = "POST", notes = "邀请成员")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/invite/{communityId}/users/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public RespResult invite(@ApiParam(value = "群组id", required = true) @PathVariable("communityId") String communityId,
                             @ApiParam(value = "被邀请者用户id", required = true) @PathVariable("memberId") Long memberId,
                             @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && communityId != null && memberId != null){
            ReqCommunityOper oper = new ReqCommunityOper();
            oper.setOperUserId(userId).setCommunityId(communityId).setOperedUserId(memberId);
        if (oper != null && oper.getOperUserId() != null && oper.getOperedUserId() != null && oper.getCommunityId() != null) {
            respResult = imCommunityUserService.invitMember(oper);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }}else{
            respResult.setCode(IMRespEnum.AUTH_FAILD.getCode());
            respResult.setMsg("不是当前用户的token");
        }
        return respResult;
    }


    /**
     * 群主操作
     *
     * @return
     */
    @ApiOperation(value = "踢群成员", httpMethod = "POST", notes = "群主踢成员")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "sec/owner/removeMember/{communityId}/users/{memberId}", method = RequestMethod.POST, produces = "application/json")
    public RespResult remvMember(@ApiParam(value = "群组id", required = true) @PathVariable("communityId") String communityId,
                                 @ApiParam(value = "被邀请者用户id", required = true) @PathVariable("memberId") Long memberId,
                                 @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && communityId != null && memberId != null) {
            ReqCommunityOper oper = new ReqCommunityOper();
            oper.setOperUserId(userId).setOperedUserId(memberId).setCommunityId(communityId);
            respResult = imCommunityUserService.remvMemberByOwner(oper);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    /**
     * 普通成员
     *
     * @return
     */
    @ApiOperation(value = "加群", httpMethod = "PUT", notes = "加群")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/member/join", method = RequestMethod.PUT, produces = "application/json")
    public RespResult memberJoin(@ApiParam(value = "群组id", required = true) @RequestParam String communityId,
                                 @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && communityId != null) {
            respResult = imCommunityUserService.joinCommunity(communityId, userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "退群", httpMethod = "DELETE", notes = "退群")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/member/leave/{communityId}", method = RequestMethod.DELETE, produces = "application/json")
    public RespResult memberLeave(@ApiParam(value = "群组id", required = true) @PathVariable("communityId") String communityId,
                                  @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (communityId != null && userId != null) {
            respResult = imCommunityUserService.leaveCommunity(communityId,userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


}
