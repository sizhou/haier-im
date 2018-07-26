package com.haier.im.easemob.api;

public interface FileAPI {
    /**
     * 下载文件 <br>
     * GET
     *
     * @param fileUUID
     *            文件唯一标识，从上传Response-entities-uuid中获取
     * @param shareSecret
     *            文件访问秘钥，从上传Response-entities-share-secret中获取
     * @param isThumbnail
     *            ，如果下载图片，是否为缩略图
     * @return
     */
    Object downloadFile(String fileUUID, String shareSecret, Boolean isThumbnail);
}
