package com.haier.im.controller;


import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.base.UserTokenCheck;
import com.haier.im.controller.reqvo.ReqCommunity;
import com.haier.im.po.IMCommunity;
import com.haier.im.service.IMAccountTokenService;
import com.haier.im.service.IMCommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Api(tags = "群组")
@RestController
@RequestMapping("im/community/")
public class IMCommunityController {


    @Autowired
    private IMCommunityService imCommunitySevice;
    @Autowired
    private IMAccountTokenService imAccountTokenService;


    @Autowired
    private UserTokenCheck userTokenCheck;

    @ApiOperation(value = "创建群组", httpMethod = "POST", notes = "创建群组")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/create", method = RequestMethod.POST, produces = "application/json")
    public RespResult<List<IMCommunity>> createGroup(@RequestParam String communityName,
                                                     @RequestParam("communityIconFile") MultipartFile iconFile,
                                                     @RequestHeader("token") String token) {
        Long ownerUserId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (Objects.nonNull(communityName) && StringUtils.isNotEmpty(communityName.trim())
                && Objects.nonNull(ownerUserId) && ownerUserId > 0
                && Objects.nonNull(iconFile)) {
            respResult = imCommunitySevice.createCommunity(communityName, ownerUserId, iconFile);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "修改群组信息", httpMethod = "PUT", notes = "修改群组信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/mod", method = RequestMethod.PUT, produces = "application/json")
    public RespResult modCommunity(@RequestBody ReqCommunity reqCommunity,
                                   @RequestHeader("token") String token) {
        Long ownerUserId  = imAccountTokenService.getUserIdByToken(token);
        reqCommunity.setOwnerUserId(ownerUserId);
        RespResult respResult = new RespResult();
        if (reqCommunity != null && reqCommunity.getCommunityId() != null && reqCommunity.getOwnerUserId() != null) {
            respResult = imCommunitySevice.modCommunity(reqCommunity);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }

    @ApiOperation(value = "修改群组图标", httpMethod = "PUT", notes = "修改图标")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/mod/icon", method = RequestMethod.PUT, produces = "application/json")
    public RespResult<List<IMCommunity>> modIconForCommunity(@RequestParam String communityId,
                                                             @RequestParam("communityIconFile") MultipartFile iconFile,
                                                             @RequestHeader("token") String token) {
        Long ownerUserId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (Objects.nonNull(communityId) && StringUtils.isNotEmpty(communityId.trim())
                && Objects.nonNull(ownerUserId) && ownerUserId > 0
                && Objects.nonNull(iconFile)) {
            respResult = imCommunitySevice.modIconForCommunity(communityId, ownerUserId, iconFile);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "删除群组", httpMethod = "DELETE", notes = "删除群组")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/del/{communityId}", method = RequestMethod.DELETE, produces = "application/json")
    public RespResult delGroup(@ApiParam(value = "群组id", required = true) @PathVariable("communityId") String communityId,
                               @RequestHeader("token") String token) {
        Long ownerUserId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (communityId != null && ownerUserId != null && ownerUserId > 0) {
            respResult = imCommunitySevice.delCommunity(communityId, ownerUserId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "搜索群组", httpMethod = "GET", notes = "搜索群组")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/search", method = RequestMethod.GET, produces = "application/json")
    public RespResult searchGroup(@ApiParam(value = "模糊搜索条件", required = true) @RequestParam String searchStr,
                                  @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (searchStr != null && userId != null && userId > 0) {
            respResult = imCommunitySevice.searchCommunityByStr(searchStr, userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "火热群", httpMethod = "GET", notes = "火热群")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/hottest", method = RequestMethod.GET, produces = "application/json")
    public RespResult hottestGroups(@ApiParam(value = "数量", required = false) @RequestParam(required = false, defaultValue = "2") Integer limitCount,
                                    @RequestHeader("token") String token) {
        Long ownerUserId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = imCommunitySevice.findHosttestCommunitys(limitCount);
        return respResult;
    }


    @ApiOperation(value = "推荐群", httpMethod = "GET", notes = "推荐群")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/recommend", method = RequestMethod.GET, produces = "application/json")
    public RespResult recommendGroups(@ApiParam(value = "数量", required = false) @RequestParam(required = false, defaultValue = "2") Integer limitCount,
                                      @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = imCommunitySevice.recommendCommunityForUser(limitCount);
        return respResult;
    }


    @ApiOperation(value = "用户群组列表", httpMethod = "GET", notes = "群组列表")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/user/list", method = RequestMethod.GET, produces = "application/json")
    public RespResult listUserGroups( @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (userId != null && userId > 0) {
            respResult = imCommunitySevice.findCommunityOfInByUserId(userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


    @ApiOperation(value = "群组详情", httpMethod = "GET", notes = "群详情")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/detail", method = RequestMethod.GET, produces = "application/json")
    public RespResult getGroupDetail(@ApiParam(value = "群组id", required = true) @RequestParam String communityId,
                                     @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (communityId != null && userId != null && userId > 0) {
            respResult = imCommunitySevice.getCommunityDetail(communityId, userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }


}
