package com.haier.im.task.bean;

import lombok.Data;

@Data
public class SOSDO {
    private String phone;

    private String imei;

    private String sn;

    private String acceptno;

    private String lati;

    private String longi;

    private String address;

    private String time;

    private Integer isGPS;

    private Integer failInf;
}
