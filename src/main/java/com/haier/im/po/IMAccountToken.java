package com.haier.im.po;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Accessors(chain = true)
@Data
public class IMAccountToken extends BasePo {

    @NotNull
    private Long userId;

    @NotNull
    private String token;

    private Date expireTime;

}
