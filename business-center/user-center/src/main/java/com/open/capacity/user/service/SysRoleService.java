package com.open.capacity.user.service;

import java.util.Map;
import java.util.Set;

import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.model.system.SysPermission;
import com.open.capacity.model.system.SysRole;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 */
public interface SysRoleService {

	/**
	 * 保存角色
	 * @param sysRole
	 */
	void save(SysRole sysRole);

	/**
	 * 修改角色
	 * @param sysRole
	 */
	void update(SysRole sysRole);

	/**
	 * 删除角色
	 * @param id
	 */
	void deleteRole(Long id);

	/**
	 * 分配权限
	 * @param id
	 * @param permissionIds
	 */
	void setPermissionToRole(Long id, Set<Long> permissionIds);

	/**
	 * ID获取角色
	 * @param id
	 * @return
	 */
	SysRole findById(Long id);

	/**
	 * 角色列表
	 * @param params
	 * @return
	 */
	PageResult<SysRole> findRoles(Map<String, Object> params);

	/**
	 * 角色权限列表
	 * @param roleId
	 * @return
	 */
	Set<SysPermission> findPermissionsByRoleId(Long roleId);

	/**
	 * 更新角色
	 * @param sysRole
	 */
	Result saveOrUpdate(SysRole sysRole);

}
