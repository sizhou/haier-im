package com.haier.im.base;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class PinyinResp {

    private String initial;//首字母


    private List arrays;//数据



}
