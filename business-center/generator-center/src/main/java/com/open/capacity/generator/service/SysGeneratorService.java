package com.open.capacity.generator.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.open.capacity.commons.PageResult;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-09-05 12:03]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Service
public interface SysGeneratorService {

     PageResult queryList(Map<String, Object> map);

     int queryTotal(Map<String, Object> map);

     Map<String, String> queryTable(String tableName);

     List<Map<String, String>> queryColumns(String tableName);

     byte[] generatorCode(String[] tableNames);

}
