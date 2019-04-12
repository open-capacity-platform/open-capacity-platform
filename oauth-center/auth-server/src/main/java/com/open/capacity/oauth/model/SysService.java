package com.open.capacity.oauth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 12:07]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Data
public class SysService implements Serializable {

    private static final long serialVersionUID = 749360940290141180L;

    private Long id;
    private Long parentId;
    private String name;
    private String css;
    private String url;
    private String path;
    private Integer sort;
    private Date createTime;
    private Date updateTime;

    private Integer isMenu;

}