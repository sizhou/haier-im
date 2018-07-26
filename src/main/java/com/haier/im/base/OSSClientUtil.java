package com.haier.im.base;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class OSSClientUtil {

    private static OSSClient ossClient;

    public static final Logger logger = LoggerFactory.getLogger(OSSClientUtil.class);

    static {
        ossClient = new OSSClient(OSSConstant.static_endpoint, OSSConstant.static_accessKeyId, OSSConstant.static_accessKeySecret);
    }

    /**
     * OSS上传文件
     *
     * @param file  文件
     * @param isPub 是否公共读
     * @param key   文件原有key
     * @return
     */
    public static String uploadImg2Oss(MultipartFile file, Boolean isPub, String key) {
        if (file.getSize() > 1024 * 1024 * 10) {
            logger.error("上传图片大小不能超过10M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (StringUtils.isNotBlank(key) && key.substring(0, key.lastIndexOf(".") > 0 ? key.lastIndexOf(".") : 1).length() == 19) {
            //替换KEY
            String version = key.substring(key.indexOf(".V") + 2, key.lastIndexOf("."));
            String result = "" + (Integer.parseInt(version) + 1);
            int size = 4 - result.length();
            for (int j = 0; j < size; j++) {
                result = "0" + result;
            }
            key = key.substring(0, key.indexOf(".V") + 2) + result + substring;
        } else {
            Random random = new Random();
            key = random.nextInt(10000) + System.currentTimeMillis() + ".V0001" + substring;
        }
        try {
            InputStream inputStream = file.getInputStream();
            uploadFile2OSS(inputStream, key, isPub);
            return key;
        } catch (Exception e) {
            logger.error("图片上传失败");
        }
        return null;
    }

    /**
     * 环信消息体上传文件到OSS
     *
     * @param file
     * @param isPub
     * @return
     */
    public static String uploadMsgFile2Oss(File file, Boolean isPub) {
        Random random = new Random();
        String key = random.nextInt(10000) + System.currentTimeMillis() + ".V0001";
        try {
            InputStream inputStream = new FileInputStream(file);
            uploadFile2OSS(inputStream, key, isPub);
            return key;
        } catch (Exception e) {
            logger.error("图片上传失败");
        }
        return null;
    }

    public static String uploadMsgFile2Oss(byte[] content, Boolean isPub) {
        Random random = new Random();
        String key = random.nextInt(10000) + System.currentTimeMillis() + ".V0001";
        try {
            uploadFile2OSS(new ByteArrayInputStream(content), key, isPub);
            return key;
        } catch (Exception e) {
            logger.error("图片上传失败");
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public static String uploadFile2OSS(InputStream instream, String fileName, Boolean isPub) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            PutObjectResult putResult;
            //上传公共读文件
            if (isPub) {
                putResult = ossClient.putObject(OSSConstant.static_pubBucketName, fileName, instream, objectMetadata);
            } else {
//                ossClient.setObjectAcl(priBucketName,fileName, CannedAccessControlList.Private);
                //上传私有文件
                putResult = ossClient.putObject(OSSConstant.static_priBucketName, fileName, instream, objectMetadata);
            }
            ret = putResult.getETag();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得私有url链接
     *
     * @param key
     * @return
     */
    public static String getPriUrl(String key) {
        // 设置URL过期时间为1小时  3600l* 1000*24*
        Date expiration = new Date(new Date().getTime() + 3600l * 1000);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(OSSConstant.static_priBucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 获取公共读URL
     *
     * @param key
     * @return
     */
    public static String getPubUrl(String key) {
        String publicUrl = "";
        if (key.contains(OSSConstant.static_visitUrl)) {
            publicUrl = key;
        } else {
            publicUrl = OSSConstant.static_visitUrl + key;
        }
        return publicUrl;
    }


//    private static void displayTextInputStream(InputStream input) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        while (true) {
//            String line = reader.readLine();
//            if (line == null) break;
//
//            System.out.println("\t" + line);
//        }
//        System.out.println();
//
//        reader.close();
//    }

    public static void downloadPubFileToLocal(String fileKey) {
        try {
            System.out.println("========start download pri file===");
            File localFile = new File("D:\\im_download\\" + fileKey);
            ossClient = new OSSClient(OSSConstant.static_endpoint, OSSConstant.static_accessKeyId, OSSConstant.static_accessKeySecret);
            ObjectMetadata object = ossClient.getObject(new GetObjectRequest(OSSConstant.static_pubBucketName, fileKey), localFile);
            System.out.println("=== end ===Content-Type: " + object.getContentType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

    }


    public static void downloadPriFileToLocal(String fileKey) {
        try {
            System.out.println("========start download pri file===");
            File localFile = new File("D:\\im_download\\" + fileKey);
            ossClient = new OSSClient(OSSConstant.static_endpoint, OSSConstant.static_accessKeyId, OSSConstant.static_accessKeySecret);
            ObjectMetadata object = ossClient.getObject(new GetObjectRequest(OSSConstant.static_priBucketName, fileKey), localFile);
            System.out.println("=== end ===Content-Type: " + object.getContentType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

    }


}
