package com.open.capacity.oauth.service;

import java.util.List;
import java.util.Set;

import com.open.capacity.oauth.model.SysService;

public interface ISysServiceService {

	/**
	 * 添加服务
	 * @param service
	 */
	void save(SysService service);

	/**
	 * 更新服务
	 * @param service
	 */
	void update(SysService service);

	/**
	 * 删除服务
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 客户端分配服务
	 * @param clientId
	 * @param serviceIds
	 */
	void setMenuToClient(Long clientId, Set<Long> serviceIds);

	/**
	 * 客户端服务列表
	 * @param clientIds
	 * @return
	 */
	List<SysService> findByClient(Set<Long> clientIds);

	/**
	 * 服务列表
	 * @return
	 */
	List<SysService> findAll();

	/**
	 * ID获取服务
	 * @param id
	 * @return
	 */
	SysService findById(Long id);

	/**
	 * 角色ID获取服务
	 * @param clientId
	 * @return
	 */
	Set<Long> findServiceIdsByClientId(Long clientId);

	/**
	 * 一级服务
	 * @return
	 */
	List<SysService> findOnes();


}
