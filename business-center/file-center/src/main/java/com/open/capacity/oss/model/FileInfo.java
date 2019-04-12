package com.open.capacity.oss.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * file实体类
*/
@Data
public class FileInfo implements Serializable {

	private static final long serialVersionUID = -1438078028040922174L;
//  md5字段
	private String id;
//  原始文件名
	private String name;
//	是否图片
	private Boolean isImg;
//	上传文件类型
	private String contentType;
//	文件大小
	private long size;
//  冗余字段
	private String path;
//	oss访问路径 oss需要设置公共读
	private String url;
//	FileType字段
	private String source;
	private Date createTime;
}
