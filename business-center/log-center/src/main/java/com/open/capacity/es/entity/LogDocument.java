package com.open.capacity.es.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

import java.util.Date;

/**
 * 日志对象
 *
 * @author zlt
 */
@Data
@Document(indexName = "filebeat-sys-log", type = "doc")
public class LogDocument {
    @Id
    private String id;
    private Date timestamp;
    private String message;
    private String threadName;
    private String serverPort;
    private String serverIp;
    private String logLevel;
    private String appName;
    private String classname;
}