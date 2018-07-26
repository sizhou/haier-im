package com.haier.im.controller;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.controller.reqvo.ChatFileResp;
import com.haier.im.controller.reqvo.ChatMessageResp;
import com.haier.im.service.IMAccountTokenService;
import com.haier.im.service.IMMessageService;
import com.haier.im.vo.IMMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "消息管理")
@RestController
@RequestMapping(value = {"/im/msg"})
public class IMMessageController {

    @Autowired
    private IMMessageService imMessageService;
    @Autowired
    private IMAccountTokenService imAccountTokenService;


    @ApiOperation(value = "获取历史消息记录", httpMethod = "POST", notes = "获取两个小时前历史消息记录")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/msgs", method = RequestMethod.POST, produces = "application/json")
    public RespResult<ChatMessageResp> list(
            @RequestHeader("token") String token,
            @ApiParam(value = "开始时间", required = true, defaultValue = "2018-03-06 07:00:00") @RequestParam("startTime") String startTime,
            @ApiParam(value = "结束时间", required = true, defaultValue = "2018-03-06 08:00:00") @RequestParam("endTime") String endTime,
            @ApiParam(value = "另一个聊天者Id（群组id，或者好友imId）", required = true) @RequestParam("messageUserId") String messageUserId,
            @ApiParam(value = "消息类型（1:群发 2:单发）", required = true) @RequestParam("sendType") Integer sendType,
            @ApiParam(value = "页码", required = true) @RequestParam("pageNo") Integer pageNo,
            @ApiParam(value = "每页长度", required = true) @RequestParam("pageSize") Integer pageSize) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        if (null == pageNo || pageNo <= 0) {
            pageNo = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        RespResult respResult = new RespResult();
        if (messageUserId != null && StringUtils.isNotEmpty(messageUserId.trim())
                && userId != null && userId > 0
                && (sendType.equals(1) || sendType.equals(2) || sendType.equals(3))) {
            IMMessageVo imMessageVo = new IMMessageVo();
            imMessageVo.setStartDate(startTime);
            imMessageVo.setEndDate(endTime);
            imMessageVo.setPageNo(pageNo);
            imMessageVo.setPageSize(pageSize);

            imMessageVo.setUserId(userId);
            imMessageVo.setMessageUserId(messageUserId);
            imMessageVo.setSendType(sendType);
            respResult = imMessageService.msgs(imMessageVo);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }

        return respResult;
    }

    @ApiOperation(value = "获取群共享文件", httpMethod = "POST", notes = "获取群共享文件")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/getFiles", method = RequestMethod.POST, produces = "application/json")
    public RespResult<ChatFileResp> getGroupFiles(@RequestHeader("token") String token,
                                                  @ApiParam(value = "另一个聊天者Id（群组，或者好友imId）", required = true) @RequestParam("messageUserId") String messageUserId,
                                                  @ApiParam(value = "消息类型（1:群发 2:单发）", required = true) @RequestParam("sendType") Integer sendType,
                                                  @ApiParam(value = "页码", required = true) @RequestParam("pageNo") Integer pageNo,
                                                  @ApiParam(value = "每页长度", required = true) @RequestParam("pageSize") Integer pageSize) {
        Long userId  = imAccountTokenService.getUserIdByToken(token);
        RespResult respResult = new RespResult();
        //验证token
        if (null == pageNo || pageNo <= 0) {
            pageNo = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        if (userId != null
                && messageUserId != null
                && (sendType.equals(1) || sendType.equals(2) || sendType.equals(3))) {
            IMMessageVo messageVo = new IMMessageVo();
            messageVo.setMessageUserId(messageUserId);
            messageVo.setUserId(userId);
            messageVo.setSendType(sendType);
            messageVo.setPageNo(pageNo);
            messageVo.setPageSize(pageSize);
            respResult = imMessageService.listChatFiles(messageVo);
        } else {
            respResult.setCode(IMRespEnum.SYS_PARAM_ERROR.getCode());
            respResult.setMsg(IMRespEnum.SYS_PARAM_ERROR.getMsg());
        }
        return respResult;
    }



}
