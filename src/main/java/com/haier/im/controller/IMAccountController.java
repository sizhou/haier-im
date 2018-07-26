package com.haier.im.controller;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.base.UserTokenCheck;
import com.haier.im.base.Validate;
import com.haier.im.controller.reqvo.SelfAccountInfoReq;
import com.haier.im.po.IMAccountInfo;
import com.haier.im.po.IMCommunity;
import com.haier.im.service.IMAccountInfoService;
import com.haier.im.service.IMAccountTokenService;
import com.haier.im.vo.IMAccountInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "im用户初始化")
@RestController
@RequestMapping("im/account")
public class IMAccountController {

    @Autowired
    private UserTokenCheck userTokenCheck;

    @Autowired
    private IMAccountInfoService imAccountInfoService;

    @Autowired
    private IMAccountTokenService imAccountTokenService;



    @ApiOperation(value = "授权到IM账户中", httpMethod = "GET", notes = "授权IM")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
    public RespResult authIMAccount(@RequestParam String phone) {
        RespResult imBaseResp = new RespResult();
        if (Validate.checkTel(phone)) {
            //认证
            imBaseResp = imAccountTokenService.authIMUserByPhone(phone);
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return imBaseResp;
    }


    @ApiOperation(value = "搜索账户", httpMethod = "GET", notes = "账户搜索")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/find", method = RequestMethod.GET, produces = "application/json")
    public RespResult find(@ApiParam(value = "模糊搜索的条件", required = true) @RequestParam("searchStr") String searchStr,
                           @RequestHeader("token") String token) {
        RespResult imBaseResp = null;
        Long userId = imAccountTokenService.getUserIdByToken(token);
        if (null != userId && userId > 0){
        if (searchStr != null) {
            IMAccountInfoVo findVo = new IMAccountInfoVo();
            findVo.setSearchStr("%" + searchStr + "%");
            findVo.setIsDelete(false);
            imBaseResp = imAccountInfoService.findAccountsBy(findVo, userId);
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }}else{
            imBaseResp.setCode(IMRespEnum.AUTH_FAILD.getCode());
            imBaseResp.setMsg(IMRespEnum.AUTH_FAILD.getMsg());
        }
        return imBaseResp;
    }


    @ApiOperation(value = "查看账户信息", httpMethod = "GET", notes = "账户基本信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/info", method = RequestMethod.GET, produces = "application/json")
    public RespResult findUserInfo(@ApiParam(value = "查看者用户Id(为空时查看自己信息)", required = false) @RequestParam(value = "lookUserId",required = false) Long lookUserId,
                                   @RequestHeader("token") String token) {
        RespResult imBaseResp = null;
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        if (lookUserId == null || lookUserId < 1){
            lookUserId = userId;
        }
        if (lookUserId != null && lookUserId > 0 && userId != null && userId > 0) {
            IMAccountInfo findIdEn = new IMAccountInfo();
            findIdEn.setUserId(lookUserId);
            imBaseResp = imAccountInfoService.findAccountInfo(findIdEn, userId);
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return imBaseResp;
    }


    @ApiOperation(value = "根据imid查看账户信息", httpMethod = "GET", notes = "账户基本信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/info/imid/", method = RequestMethod.GET, produces = "application/json")
    public RespResult findUserInfoByImId(@ApiParam(value = "imId", required = true) @RequestParam("imId") String imId,
                                   @RequestHeader("token") String token) {
        RespResult imBaseResp = null;
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        if (imId != null && StringUtils.isNotEmpty(imId.trim())) {
            IMAccountInfo findIdEn = new IMAccountInfo();
            findIdEn.setImId(imId);
            imBaseResp = imAccountInfoService.findAccountInfo(findIdEn, userId);
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return imBaseResp;
    }





    @ApiOperation(value = "查看详细信息", httpMethod = "GET", notes = "账户详细信息")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/detail", method = RequestMethod.GET, produces = "application/json")
    public RespResult findUserDetail(@ApiParam(value = "查看者用户Id(为空时查看自己信息)", required = false) @RequestParam(value = "lookUserId",required = false) Long lookUserId,
                                   @RequestHeader("token") String token) {
        RespResult imBaseResp = null;
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        if (lookUserId == null || lookUserId < 1){
            lookUserId = userId;
        }
        if (lookUserId != null && lookUserId > 0 && userId != null && userId > 0) {
            IMAccountInfo findIdEn = new IMAccountInfo();
            findIdEn.setUserId(lookUserId);
            imBaseResp = imAccountInfoService.findSingleAccountDetail(findIdEn, userId);
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return imBaseResp;
    }

    @ApiOperation(value = "修改个人资料", httpMethod = "PUT", notes = "修改个人资料")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/mod", method = RequestMethod.PUT, produces = "application/json")
    public RespResult modCommunity(@RequestBody SelfAccountInfoReq selfAccountInfoReq,
                                   @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (selfAccountInfoReq != null ) {
            respResult = imAccountInfoService.updAccountDetailBySelf(selfAccountInfoReq,userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }

    @ApiOperation(value = "修改个人头像", httpMethod = "PUT", notes = "修改头像")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/sec/mod/portrait", method = RequestMethod.PUT, produces = "application/json")
    public RespResult<List<IMCommunity>> modIconForCommunity(@RequestParam("selfPortrait") MultipartFile selfPortrait,
                                                             @RequestHeader("token") String token) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        if (selfPortrait != null) {
            respResult = imAccountInfoService.updAccountportailBySelf(selfPortrait,userId);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }





}
