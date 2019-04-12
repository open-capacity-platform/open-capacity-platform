package com.open.capacity.easypoi.user;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-08-21 23:00]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Data
public class SysUserExcel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5886012896705137070L;

    private Long id;

    @Excel(name = "用户姓名", height = 20, width = 30, isImportField = "true_st")
    private String username;

    private String password;

    @Excel(name = "用户昵称", height = 20, width = 30, isImportField = "true_st")
    private String nickname;

//    @Excel(name = "用户头像", type = 2 ,width = 40 , height = 20,imageType = 2)
    private String headImgUrl;

    @Excel(name = "手机号码", height = 20, width = 30, isImportField = "true_st")
    private String phone;

    @Excel(name = "学生性别", replace = { "男_0", "女_1" }, suffix = "生", isImportField = "true_st")
    private Integer sex;

    private Boolean enabled;

    private String type;

    @Excel(name = "创建时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st", width = 20)
    private Date createTime;

    @Excel(name = "修改时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true_st", width = 20)
    private Date updateTime;

}
