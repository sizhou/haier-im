package com.haier.im.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haier.im.po.IMMsgFile;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Data
@Accessors(chain = true)
public class IMMsgFileVo extends IMMsgFile {

    //发送消息方昵称
    private String senderNickName;

    @JsonIgnore
    private String senderImId;//发送者imid，用于匹配名字

    private Integer messageType;//消息类型（文本，图片，语音等。。。）

    private Integer sendType;//群聊，单聊

    private Date sendTime;//消息发送时间



    public IMMsgFileVo parsePoTo(IMMsgFileVo result, IMMsgFile po) {
        try {
            BeanUtils.copyProperties(result, po);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }


}
