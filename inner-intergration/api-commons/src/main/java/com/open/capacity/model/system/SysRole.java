package com.open.capacity.model.system;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 *  角色
 */
@Data
public class SysRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4497149010220586111L;
	@JsonSerialize(using=ToStringSerializer.class)
	private Long id;
	private String code;
	private String name;
	private Date createTime;
	private Date updateTime;
	private Long userId;
}
