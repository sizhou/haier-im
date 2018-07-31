package com.haier.im.controller;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.service.IMAccountTokenService;
import com.haier.im.service.IMFriendService;
import com.haier.im.controller.reqvo.ReqAddFriend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "好友管理")
@RestController
@RequestMapping("im/friend")
public class IMFriendController {

    @Autowired
    private IMFriendService imFriendService;
    @Autowired
    private IMAccountTokenService imAccountTokenService;


    @ApiOperation(value = "用户添加好友（不需要同意）", httpMethod = "POST", notes = "加好友")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/add/ineed/{friUserId}", method = RequestMethod.POST, produces = "application/json")
    public RespResult addFriendNoAgree(@PathVariable Long friUserId,
                          @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && friUserId != null && !userId.equals(friUserId)) {
            ReqAddFriend reqAddFriend = new ReqAddFriend();
            reqAddFriend.setSelfUserId(userId).setFriUserId(friUserId);
            respResult = imFriendService.addFriendNoNeedAgree(reqAddFriend);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }




    @ApiOperation(value = "用户添加好友", httpMethod = "POST", notes = "申请加为好友")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/add/{friUserId}", method = RequestMethod.POST, produces = "application/json")
    public RespResult add(@PathVariable Long friUserId,
                          @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && friUserId != null && !userId.equals(friUserId)) {
            ReqAddFriend reqAddFriend = new ReqAddFriend();
            reqAddFriend.setSelfUserId(userId).setFriUserId(friUserId);
            respResult = imFriendService.addFriend(reqAddFriend);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "处理好友请求", httpMethod = "POST", notes = "处理好友请求")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/deal/befriend", method = RequestMethod.POST, produces = "application/json")
    public RespResult add(@ApiParam(value = "发送好友请求者imid", required = true) @RequestParam String sendImId,
                          @ApiParam(value = "是否同意加为好友", required = true) @RequestParam Boolean isAgree,
                          @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (sendImId != null && userId != null && isAgree != null) {
            respResult = imFriendService.dealBeFriend(userId, sendImId, isAgree);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "解除IM用户好友关系", httpMethod = "DELETE", notes = "解除IM用户好友关系")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/remove/{friendUserId}", method = RequestMethod.DELETE, produces = "application/json")
    public RespResult remove(@PathVariable("friendUserId") Long friendUserId,
                             @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && friendUserId != null && !friendUserId.equals(userId)) {
            respResult = imFriendService.remFriend(userId, friendUserId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "查询好友列表", httpMethod = "GET", notes = "好友列表")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/list", method = RequestMethod.GET, produces = "application/json")
    public RespResult list( @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null) {
            respResult = imFriendService.listFriends(userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "修改好友昵称", httpMethod = "POST", notes = "修改昵称")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/updNickName", method = RequestMethod.POST, produces = "application/json")
    public RespResult updNickName(@RequestParam Long friendUserId ,
                             @RequestParam String friendNickName,
                             @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (friendUserId != null && userId != null && friendNickName != null
                && StringUtils.isNotEmpty(friendNickName.trim())
                && !friendUserId.equals(userId)) {
            respResult = imFriendService.updFriendNickName(userId,friendUserId,friendNickName);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }




    @ApiOperation(value = "查询历史好友请求", httpMethod = "GET", notes = "好友请求")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/list/be", method = RequestMethod.GET, produces = "application/json")
    public RespResult listBeFriendsRequest( @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null) {
            respResult = imFriendService.listBeFriendsRequest(userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }



}
