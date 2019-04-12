package com.open.capacity.user.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.open.capacity.model.system.SysMenu;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月13日 上午22:57:51
 * 角色菜单
 */
@Mapper
public interface SysRoleMenuDao {

	@Insert("insert into sys_role_menu(roleId, menuId) values(#{roleId}, #{menuId})")
	int save(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	int delete(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

	@Select("select t.menuId from sys_role_menu t where t.roleId = #{roleId}")
	Set<Long> findMenuIdsByRoleId(Long roleId);

	List<SysMenu> findMenusByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
