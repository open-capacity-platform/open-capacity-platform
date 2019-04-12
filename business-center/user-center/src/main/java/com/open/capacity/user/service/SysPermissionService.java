package com.open.capacity.user.service;

import java.util.Map;
import java.util.Set;

import com.open.capacity.commons.PageResult;
import com.open.capacity.model.system.SysPermission;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 */
public interface SysPermissionService {

	/**
	 * 根绝角色ids获取权限集合
	 * 
	 * @param roleIds
	 * @return
	 */
	Set<SysPermission> findByRoleIds(Set<Long> roleIds);

	/**
	 * 保存权限
	 * @param sysPermission
	 */
	void save(SysPermission sysPermission);

	/**
	 * 修改权限
	 * @param sysPermission
	 */
	void update(SysPermission sysPermission);

	/**
	 * 删除权限
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 权限列表
	 * @param params
	 * @return
	 */
	PageResult<SysPermission> findPermissions(Map<String, Object> params);

	/**
	 * 授权
	 * @param roleId
	 * @param authIds
	 */
	void setAuthToRole(Long roleId, Set<Long> authIds);

}
