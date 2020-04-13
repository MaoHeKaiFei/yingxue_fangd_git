package com.fd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CateVo {

    private String id;
    private String cateName;
    private Integer levels;
    private String parentId;
    private List<CateVo> categoryList;


}
