package com.open.capacity.sms.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class SmsCode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7353129036120185186L;
	//短信流水号
	private String key;
}
