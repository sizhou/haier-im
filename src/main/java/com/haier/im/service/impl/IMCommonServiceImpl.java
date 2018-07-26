package com.haier.im.service.impl;

import com.haier.im.base.IMRespEnum;
import com.haier.im.base.OSSClientUtil;
import com.haier.im.base.RespResult;
import com.haier.im.service.IMCommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IMCommonServiceImpl implements IMCommonService {


    @Override
    public RespResult uploadFile(MultipartFile file, Boolean isPub, String fKey) {
        RespResult imBaseResp = new RespResult();
        //上传头像到OSS
        String portraitKey = null;
        portraitKey = OSSClientUtil.uploadImg2Oss(file, isPub, fKey);
        if (StringUtils.isNotBlank(portraitKey)) {
            imBaseResp.setCode(IMRespEnum.SUCCESS.getCode());
            imBaseResp.setMsg(IMRespEnum.SUCCESS.getMsg());
            if (isPub) {
                imBaseResp.setData(OSSClientUtil.getPubUrl(portraitKey));
            } else {
                imBaseResp.setData(OSSClientUtil.getPriUrl(portraitKey));
            }
        } else {
            imBaseResp.setCode(IMRespEnum.SYS_STAT_FAIL.getCode());
            imBaseResp.setMsg(IMRespEnum.SYS_STAT_FAIL.getMsg());
        }
        return imBaseResp;
    }


    @Override
    public RespResult downloadFileToLocal(Boolean isPub, String fKey) {
        RespResult respResult = new RespResult();
        if (isPub){
            OSSClientUtil.downloadPubFileToLocal(fKey);
        }else if (!isPub){
            OSSClientUtil.downloadPriFileToLocal(fKey);
        }
        respResult.setCode(IMRespEnum.SUCCESS.getCode());
        respResult.setMsg(IMRespEnum.SUCCESS.getMsg());
        return respResult;
    }
}
