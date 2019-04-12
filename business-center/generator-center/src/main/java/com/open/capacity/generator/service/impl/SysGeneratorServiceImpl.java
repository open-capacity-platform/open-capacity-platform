package com.open.capacity.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.generator.dao.SysGeneratorDao;
import com.open.capacity.generator.service.SysGeneratorService;
import com.open.capacity.generator.utils.GenUtils;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-09-05 12:20]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Service
public class SysGeneratorServiceImpl implements SysGeneratorService {

    @Autowired
    private SysGeneratorDao sysGeneratorDao;


    @Override
    public PageResult queryList(Map<String, Object> map) {
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(map, "page"),MapUtils.getInteger(map, "limit"),true);

        List list = sysGeneratorDao.queryList(map);
        PageInfo pageInfo = new PageInfo<>(list);
        return PageResult.builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return 0;
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(String tableName : tableNames){
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
