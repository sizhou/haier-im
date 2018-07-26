package com.haier.im.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haier.im.base.DateUtil;
import com.haier.im.base.IMConstant;
import com.haier.im.base.OSSClientUtil;
import com.haier.im.dao.IMMsgFileMapper;
import com.haier.im.dao.IMMsgSendMapper;
import com.haier.im.easemob.api.ChatMessageAPI;
import com.haier.im.easemob.api.FileAPI;
import com.haier.im.easemob.common.OrgInfo;
import com.haier.im.easemob.common.TokenUtil;
import com.haier.im.po.IMMsgFile;
import com.haier.im.po.IMMsgSend;
import com.haier.im.task.bean.MessageBody;
import com.haier.im.task.bean.MessageDO;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Component
public class MessageTask {
    @Resource
    private FileAPI fileAPI;
    @Resource
    private ChatMessageAPI chatMessageAPI;
    @Resource
    private IMMsgSendMapper imMessageMapper;
    @Resource
    private IMMsgFileMapper imMsgFileMapper;

    /**
     * 定时整点获取消息文件
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0 0-23 * * *")
    public void getMessage() throws IOException {
//        //获取当前时间的前一个小时
//        Object msgs = chatMessageAPI.exportChatMessages(DateUtil.toPointDtmPart(DateUtil.getTimeByHour(-2)));
        Object msgs = chatMessageAPI.exportChatMessages(DateUtil.toPointDtmPart(DateUtil.getTimeByHour(-2)));

        if (null != msgs) {
            JSONObject json = JSON.parseObject(msgs.toString());
            JSONArray arr = json.getJSONArray("data");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String url = obj.getString("url");
                if (StringUtils.isNotBlank(url)) {
                    //下载聊天记录
                    System.out.println(url);
                    InputStream is = null;

                    URL url1 = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                    con.setRequestProperty("Accept-Encoding", "gzip,deflate");
                    con.connect();
                    is = con.getInputStream();

                    GZIPInputStream gzin = new GZIPInputStream(is);
                    BufferedReader bin = new BufferedReader(new InputStreamReader(gzin, "UTF-8"));

                    String s = null;
                    String result = "";
                    while ((s = bin.readLine()) != null) {
                        result += s;
                    }

                    System.out.println("easemFile ===="+result);
                    //根据返回的结果解析数据
                    if (StringUtils.isNotBlank(result)) {
                        String jsonStr = "[" + result.replace("}{", "}, {") + "]";
                        JSONArray array = JSONArray.parseArray(jsonStr);
                        for (int j = 0; j < array.size(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            MessageDO messageDO = object.toJavaObject(MessageDO.class);

                            System.out.println(messageDO.toString());
                            if (messageDO.getChat_type().equals("chat") || messageDO.getChat_type().equals("groupchat")) {
                                //群聊消息体
                                IMMsgSend imMessage = new IMMsgSend();
                                MessageBody messageBody = messageDO.getPayload().getBodies().get(0);
                                imMessage.setRecverId(messageDO.getPayload().getTo());
                                imMessage.setMessageId(messageDO.getMsg_id());
                                imMessage.setMessageDetail(messageBody.getMsg());
                                Integer msgType = messageBody.getType().equals("txt") ? IMConstant.MESSAGE_TYPE_TXT :
                                        messageBody.getType().equals("img") ? IMConstant.MESSAGE_TYPE_IMG :
                                                messageBody.getType().equals("loc") ? IMConstant.MESSAGE_TYPE_LOC :
                                                        messageBody.getType().equals("audio") ? IMConstant.MESSAGE_TYPE_AUDIO :
                                                                messageBody.getType().equals("video") ? IMConstant.MESSAGE_TYPE_VIDEO :
                                                                        messageBody.getType().equals("file") ? IMConstant.MESSAGE_TYPE_FILE : 0;
                                if (msgType == IMConstant.MESSAGE_TYPE_LOC) {
                                    imMessage.setAddr(messageBody.getAddr());
                                    imMessage.setLat(messageBody.getLat());
                                    imMessage.setLng(messageBody.getLng());
                                }
                                imMessage.setMessageType(msgType);
                                imMessage.setSenderId(messageDO.getPayload().getFrom());
                                imMessage.setRecverId(messageDO.getPayload().getTo());
                                if (messageDO.getChat_type().equals("chat")){
                                    imMessage.setSendType(2);
                                }else if(messageDO.getChat_type().equals("groupchat")){
                                    imMessage.setSendType(1);
                                }
                                imMessage.setSendTime(DateUtil.getIntTime2Date(Long.parseLong(messageDO.getTimestamp())));
                                imMessage.setCreator(messageDO.getPayload().getFrom());
                                imMessage.setCreateTime(new Date());
                                imMessage.setIsDelete(false);
                                imMessage.setUpdateTime(new Date());
                                imMessage.setDirection(messageDO.getDirection());
                                imMessage.setExt(JSON.toJSONString(messageDO.getPayload().getExt()));
                                System.out.println("insert ===="+imMessage.toString());
                                int count = imMessageMapper.insertRecord(imMessage);

                                if (count > 0) {
                                    //保存消息文件
                                    if (msgType != IMConstant.MESSAGE_TYPE_TXT && msgType != IMConstant.MESSAGE_TYPE_LOC) {
                                        IMMsgFile imMsgFile = new IMMsgFile();
                                        imMsgFile.setCreateTime(imMessage.getSendTime());
                                        imMsgFile.setEaseUrl(messageBody.getUrl());
                                        imMsgFile.setCreator(imMessage.getCreator());
                                        imMsgFile.setFileName(messageBody.getFilename());
                                        imMsgFile.setIsDelete(false);
                                        imMsgFile.setMessageId(imMessage.getMessageId());
                                        imMsgFile.setSecret(messageBody.getSecret());
                                        imMsgFile.setUpdateTime(new Date());
                                        imMsgFile.setFileLength(messageBody.getFile_length());
                                        imMsgFile.setLength(messageBody.getLength());
                                        imMsgFile.setSize(messageBody.getSize());
                                        imMsgFile.setThumbEaseUrl(messageBody.getThumb());
                                        imMsgFile.setThumbSecret(messageBody.getThumb_secret());

                                        //保存消息文件到OSS
//                                        Object fileObj = fileAPI.downloadFile(imMsgFile.getFileUUID(), imMsgFile.getSecret(), false);

                                        RestTemplate restTemplate = new RestTemplate();

                                        HttpHeaders requestHeaders = new HttpHeaders();
                                        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
                                        requestHeaders.add("Authorization", TokenUtil.getAccessToken());
                                        requestHeaders.add("share-secret", imMsgFile.getSecret());
//                                        requestHeaders.add("thumbnail", String.valueOf(false));
                                        HttpEntity<String> entity = new HttpEntity<String>(requestHeaders);

                                        ResponseEntity<byte[]> response = restTemplate.exchange("http://a1.easemob.com/" + OrgInfo.ORG_NAME + "/" + OrgInfo.APP_NAME + "/chatfiles/" + imMsgFile.getFileUUID(), HttpMethod.GET, entity, byte[].class);
                                        byte[] imageBytes = response.getBody();

                                        if (null != imageBytes) {
                                            String filePath = OSSClientUtil.uploadMsgFile2Oss(imageBytes, false);
                                            imMsgFile.setOssUrl(filePath);
                                            //如果是视频文件，下载视频文件缩略图
                                            if (imMessage.getMessageType() == IMConstant.MESSAGE_TYPE_VIDEO && StringUtils.isNotBlank(imMsgFile.getThumbEaseUrl()) && StringUtils.isBlank(imMsgFile.getThumbOssUrl())) {
                                                //保存消息文件
                                                Object thobj = fileAPI.downloadFile(imMsgFile.getThumbUUID(), imMsgFile.getThumbSecret(), true);
                                                if (null != thobj) {
                                                    String thurl = OSSClientUtil.uploadMsgFile2Oss((File) thobj, false);
                                                    imMsgFile.setThumbOssUrl(thurl);
                                                }
                                                // TODO: 2018/5/10 添加日志
//                                                System.out.println(imMsgFile.toString());
                                            } else if (imMessage.getMessageType() == IMConstant.MESSAGE_TYPE_IMG && StringUtils.isBlank(imMsgFile.getThumbOssUrl())) {
                                                //如果是图片，下载图片缩略图
                                                Object thobj = fileAPI.downloadFile(imMsgFile.getFileUUID(), imMsgFile.getSecret(), true);
                                                if (null != thobj) {
                                                    String thurl = OSSClientUtil.uploadMsgFile2Oss((File) thobj, false);
                                                    imMsgFile.setThumbOssUrl(thurl);
                                                }
                                                // TODO: 2018/5/10 添加日志
//                                                System.out.println(imMsgFile.toString());
                                            }
                                            count = imMsgFileMapper.insertRecord(imMsgFile);
                                            if (count > 0) {
                                                // TODO: 2018/5/10 添加日志
//                                                System.out.println("UUID:" + imMsgFile.getFileUUID() + "；存储OSS成功！" + imMsgFile.toString());
                                            }

                                        } else {
                                            // TODO: 2018/5/10 添加日志
//                                            System.out.println("UUID:" + imMsgFile.getFileUUID() + "；存储OSS失败！");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (null != is) {
                        is.close();
                    }
                    if (null != gzin) {
                        gzin.close();
                    }
                }
            }
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")
    public void saveFile() {
        IMMsgFile imMsgFile = new IMMsgFile();
        Date startDate = DateUtil.getBeforeDate(new Date(), 1);
        Date endDate = DateUtil.getBeforeDate(new Date(), 2);
        List<IMMsgFile> imMsgFileList = imMsgFileMapper.findMsgFilesByTime(startDate,endDate);
        for (int i = 0; i < imMsgFileList.size(); i++) {
            IMMsgFile file = imMsgFileList.get(i);
            if (StringUtils.isBlank(file.getOssUrl())) {
                //保存消息文件
                Object obj = fileAPI.downloadFile(file.getFileUUID(), file.getSecret(), false);
                if (null != obj) {
                    String url = OSSClientUtil.uploadMsgFile2Oss((File) obj, false);
                    file.setOssUrl(url);
                    if (StringUtils.isNotBlank(file.getThumbEaseUrl()) && StringUtils.isBlank(file.getThumbOssUrl())) {
                        //保存消息文件
                        Object thobj = fileAPI.downloadFile(file.getThumbUUID(), file.getThumbSecret(), true);
                        if (null != thobj) {
                            String thurl = OSSClientUtil.uploadMsgFile2Oss((File) thobj, false);
                            file.setThumbOssUrl(thurl);
                        }
                        // TODO: 2018/5/10 添加日志
//                        System.out.println(file.toString());
                    }
                    int result = imMsgFileMapper.updateFileByMessageId(file);
                    if (result > 0) {
                        // TODO: 2018/5/10 添加日志
//                        System.out.println("UUID:" + file.getFileUUID() + "；存储OSS成功！" + file.toString());
                    }

                } else {
                    // TODO: 2018/5/10 添加日志
//                    System.out.println("UUID:" + file.getFileUUID() + "；存储OSS失败！");
                }
            }

        }
    }



}
