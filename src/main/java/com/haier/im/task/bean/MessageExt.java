package com.haier.im.task.bean;

import lombok.Data;

@Data
public class MessageExt {
    private SosExt data;

    private String name;

    private String stime;

    private String time;

    private Integer type;

    private String title;

    private String desc;


}
