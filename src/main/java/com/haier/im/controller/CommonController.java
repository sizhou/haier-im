package com.haier.im.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.haier.im.base.IMRespEnum;
import com.haier.im.base.RespResult;
import com.haier.im.base.Validate;
import com.haier.im.service.IMCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;

@Api(tags = "公共和初始化")
@RestController
@RequestMapping(value = {"/im/common"})
public class CommonController {

    @Autowired
    private IMCommonService imCommonService;


    @Value("${qrCode.width}")
    private int qrCodeWidth;

    @Value("${qrCode.height}")
    private int qrCodeHeight;

    @Value("${qrCode.format}")
    private String qrCodeFormat;



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

//    @ApiOperation(value = "先下载文件到本地测试", httpMethod = "GET", notes = "下载文件，isPub（true：公共读；false：私有），fKey")
//    @ApiResponse(code = 200, message = "success", response = RespResult.class)
//    @ResponseBody
//    @RequestMapping(value = "/file/download", method = RequestMethod.GET, produces = "application/json")
//    public RespResult getFilePath(@ApiParam(name = "isPub", value = "是否公共读", required = true, defaultValue = "true") @RequestParam("isPub") Boolean isPub,
//                                  @ApiParam(name = "fKey", value = "文件原有KEY，更新时传入") @RequestParam("fKey") String fKey) {
//        RespResult imBaseResp = new RespResult();
//        if (!Validate.noNilLessEqualMaxLength(fKey, 32)) {
//            imBaseResp.setCode(IMRespEnum.FILE_KEY_FAIL.getCode());
//            imBaseResp.setMsg(IMRespEnum.FILE_KEY_FAIL.getMsg());
//        } else {
//            imBaseResp = imCommonService.downloadFileToLocal(isPub, fKey);
//            imBaseResp.setCode(IMRespEnum.SUCCESS.getCode());
//            imBaseResp.setMsg(IMRespEnum.SUCCESS.getMsg());
//        }
//        return imBaseResp;
//    }

    //共有配置规则
    private Hashtable configQrCodeHash() {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        return hints;
    }



    @RequestMapping(value = "/getQrCode", method = {RequestMethod.GET })
    public void getQrCode(HttpServletResponse resp, String content) throws IOException {
        if (content != null && !"".equals(content)) {
            ServletOutputStream stream = null;
            try {
                stream = resp.getOutputStream();
                Hashtable hints = this.configQrCodeHash();
                BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight, hints);
                MatrixToImageWriter.writeToStream(bitMatrix, qrCodeFormat, stream);
            } catch (WriterException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }else{
            return;
        }
    }



}
