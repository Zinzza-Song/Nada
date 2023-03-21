package com.fmi.nada.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockListDto {

    private String blockMemberNickname;
    private String blockMemberReason;
    private Long blockMemberIdx;

}
