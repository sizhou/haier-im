package com.haier.im.controller;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.base.Validate;
import com.haier.im.service.IMCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公共和初始化")
@RestController
@RequestMapping(value = {"/im/common"})
public class CommonController {

    @Autowired
    private IMCommonService imCommonService;


//    @ApiOperation(value = "上传文件", httpMethod = "POST", notes = "上传文件，isPub（true：公共读；false：私有），fKey（不为空时，做文件更新操作）；使用场景：1、上传头像；2、上传上门服务详情图片；3、上传默认头像（医生，患者，家属。。。）等")
//    @ApiResponse(code = 200, message = "success", response = RespResult.class)
//    @ResponseBody
//    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
//    public RespResult uploadFile(@ApiParam(name = "isPub", value = "是否公共读", required = true, defaultValue = "true") @RequestParam("isPub") Boolean isPub,
//                                 @ApiParam(name = "fKey", value = "文件原有KEY，更新时传入") @RequestParam(value = "fKey", required = false) String fKey,
//                                 @ApiParam(name = "file", value = "文件") @RequestParam("file") MultipartFile file) {
//        RespResult imBaseResp = new RespResult();
//        if (Objects.nonNull(fKey) && !Validate.noNilLessEqualMaxLength(fKey, 32)) {
//            imBaseResp.setCode(IMRespEnum.FILE_KEY_FAIL.getCode());
//            imBaseResp.setMsg(IMRespEnum.FILE_KEY_FAIL.getMsg());
//        } else {
//            imBaseResp = imCommonService.uploadFile(file, isPub, fKey);
//        }
//        return imBaseResp;
//    }
//
//    @ApiOperation(value = "获取上传文件路径", httpMethod = "GET", notes = "上传文件，isPub（true：公共读；false：私有），fKey")
//    @ApiResponse(code = 200, message = "success", response = RespResult.class)
//    @ResponseBody
//    @RequestMapping(value = "/file", method = RequestMethod.GET, produces = "application/json")
//    public RespResult getFilePath(@ApiParam(name = "isPub", value = "是否公共读", required = true, defaultValue = "true") @RequestParam("isPub") Boolean isPub,
//                                  @ApiParam(name = "fKey", value = "文件原有KEY，更新时传入") @RequestParam("fKey") String fKey) {
//        RespResult imBaseResp = new RespResult();
//        if (!Validate.noNilLessEqualMaxLength(fKey, 32)) {
//            imBaseResp.setCode(IMRespEnum.FILE_KEY_FAIL.getCode());
//            imBaseResp.setMsg(IMRespEnum.FILE_KEY_FAIL.getMsg());
//        } else {
//            if (isPub) {
//                imBaseResp.setData(OSSClientUtil.getPubUrl(fKey));
//            } else {
//                imBaseResp.setData(OSSClientUtil.getPriUrl(fKey));
//            }
//            imBaseResp.setCode(IMRespEnum.SUCCESS.getCode());
//            imBaseResp.setMsg(IMRespEnum.SUCCESS.getMsg());
//        }
//        return imBaseResp;
//    }

    @ApiOperation(value = "先下载文件到本地测试", httpMethod = "GET", notes = "下载文件，isPub（true：公共读；false：私有），fKey")
    @ApiResponse(code = 200, message = "success", response = RespResult.class)
    @ResponseBody
    @RequestMapping(value = "/file/download", method = RequestMethod.GET, produces = "application/json")
    public RespResult getFilePath(@ApiParam(name = "isPub", value = "是否公共读", required = true, defaultValue = "true") @RequestParam("isPub") Boolean isPub,
                                  @ApiParam(name = "fKey", value = "文件原有KEY，更新时传入") @RequestParam("fKey") String fKey) {
        RespResult imBaseResp = new RespResult();
        if (!Validate.noNilLessEqualMaxLength(fKey, 32)) {
            imBaseResp.setCode(IMRespEnum.FILE_KEY_FAIL.getCode());
            imBaseResp.setMsg(IMRespEnum.FILE_KEY_FAIL.getMsg());
        } else {
            imBaseResp = imCommonService.downloadFileToLocal(isPub, fKey);
            imBaseResp.setCode(IMRespEnum.SUCCESS.getCode());
            imBaseResp.setMsg(IMRespEnum.SUCCESS.getMsg());
        }
        return imBaseResp;
    }


}
