package com.open.capacity.oauth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.open.capacity.oauth.dao.SysClientServiceDao;
import com.open.capacity.oauth.dao.SysServiceDao;
import com.open.capacity.oauth.model.SysService;
import com.open.capacity.oauth.service.ISysServiceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 15:18]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */

@Slf4j
@Service
@Transactional
public class ISysServiceServiceImpl implements ISysServiceService {


    @Autowired
    private SysServiceDao sysServiceDao;

    @Autowired
    private SysClientServiceDao sysClientServiceDao;


    /**
     * 添加服务
     *
     * @param service
     */
    @Override
    public void save(SysService service) {
        service.setCreateTime(new Date());
        service.setUpdateTime(new Date());

        sysServiceDao.save(service);
        log.info("添加服务：{}", service);
    }

    /**
     * 更新服务
     *
     * @param service
     */
    @Override
    public void update(SysService service) {
        service.setUpdateTime(new Date());

        sysServiceDao.update(service);

        log.info("更新服务：{}", service);
    }

    /**
     * 删除服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        SysService sysService = sysServiceDao.findById(id);

        sysServiceDao.deleteByParentId(sysService.getId());
        sysServiceDao.delete(id);
        log.info("删除服务:{}",sysService);
    }

    /**
     * 客户端分配服务
     *
     * @param clientId
     * @param serviceIds
     */
    @Override
    public void setMenuToClient(Long clientId, Set<Long> serviceIds) {
        sysClientServiceDao.delete(clientId,null);

        if (!CollectionUtils.isEmpty(serviceIds)){
            serviceIds.forEach(serviceId -> {
                sysClientServiceDao.save(clientId,serviceId);
            });

        }

    }

    /**
     * 客户端服务列表
     *
     * @param clientIds
     * @return
     */
    @Override
    public List<SysService> findByClient(Set<Long> clientIds) {
        return sysClientServiceDao.findServicesBySlientIds(clientIds);
    }

    /**
     * 服务列表
     *
     * @return
     */
    @Override
    public List<SysService> findAll() {
        return sysServiceDao.findAll();
    }

    /**
     * ID获取服务
     *
     * @param id
     * @return
     */
    @Override
    public SysService findById(Long id) {
        return sysServiceDao.findById(id);
    }

    /**
     * 角色ID获取服务
     *
     * @param clientId
     * @return
     */
    @Override
    public Set<Long> findServiceIdsByClientId(Long clientId) {
        return sysClientServiceDao.findServiceIdsByClientId(clientId);
    }

    /**
     * 一级服务
     *
     * @return
     */
    @Override
    public List<SysService> findOnes() {
        return sysServiceDao.findOnes();
    }

}
