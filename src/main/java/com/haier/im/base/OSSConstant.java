package com.haier.im.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Component
public class OSSConstant {

    public static String static_endpoint;
    public static String static_accessKeyId;
    public static String static_accessKeySecret;
    public static String static_priBucketName;
    public static String static_pubBucketName;
    public static String static_visitUrl;


    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.access.keyId}")
    private String accessKeyId;

    @Value("${oss.access.keySecret}")
    private String accessKeySecret;

    @Value("${oss.pub.bucket.name}")
    private String pubBucketName;


    @Value("${oss.pri.bucket.name}")
    private String priBucketName;


    @Value("${oss.pub.visit.url}")
    private String visitUrl;

    @PostConstruct
    public void init() {
        static_endpoint = endpoint;
        static_accessKeyId = accessKeyId;
        static_accessKeySecret = accessKeySecret;
        static_pubBucketName = pubBucketName;
        static_priBucketName = priBucketName;
        static_visitUrl = visitUrl;
    }


}
