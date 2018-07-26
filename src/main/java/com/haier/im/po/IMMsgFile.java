package com.haier.im.po;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class IMMsgFile extends BasePo {

    private String messageId;

    private Integer fileLength;

    private String fileName;

    private String secret;

    private String size;

    private Integer length;

    private String thumbEaseUrl;

    private String thumbOssUrl;

    private String easeUrl;

    private String ossUrl;

    private String thumbSecret;



    public String getFileUUID() {
        if (StringUtils.isNotBlank(this.getEaseUrl())) {
            return this.getEaseUrl().substring(this.getEaseUrl().lastIndexOf("/") + 1, this.getEaseUrl().length());
        } else {
            return null;
        }
    }

    public String getThumbUUID() {
        if (StringUtils.isNotBlank(this.getThumbEaseUrl())) {
            return this.getThumbEaseUrl().substring(this.getThumbEaseUrl().lastIndexOf("/") + 1, this.getThumbEaseUrl().length());
        } else {
            return null;
        }
    }

}