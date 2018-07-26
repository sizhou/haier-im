package com.haier.im.service;

import com.haier.im.base.RespResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统可以公用的
 */
public interface IMCommonService {

    /**
     * 上传文件
     * @param file
     * @param isPub
     * @param fKey
     * @return
     */
    RespResult uploadFile(MultipartFile file, Boolean isPub, String fKey);

    /**
     * 下载文件到本地（用于测试）
     * @param isPub
     * @param fKey
     * @return
     */
    RespResult downloadFileToLocal(Boolean isPub, String fKey);

}
