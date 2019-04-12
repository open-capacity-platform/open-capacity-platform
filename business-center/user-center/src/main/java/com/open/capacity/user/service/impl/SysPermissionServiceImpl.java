package com.open.capacity.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.model.system.SysPermission;
import com.open.capacity.user.dao.SysPermissionDao;
import com.open.capacity.user.dao.SysRolePermissionDao;
import com.open.capacity.user.service.SysPermissionService;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionDao sysPermissionDao;
	@Autowired
	private SysRolePermissionDao rolePermissionDao;

	@Override
	public Set<SysPermission> findByRoleIds(Set<Long> roleIds) {
		return rolePermissionDao.findPermissionsByRoleIds(roleIds);
	}

	@Transactional
	@Override
	public void save(SysPermission sysPermission) {
		SysPermission permission = sysPermissionDao.findByPermission(sysPermission.getPermission());
		if (permission != null) {
			throw new IllegalArgumentException("权限标识已存在");
		}
		sysPermission.setCreateTime(new Date());
		sysPermission.setUpdateTime(sysPermission.getCreateTime());

		sysPermissionDao.insert(sysPermission);
		log.info("保存权限标识：{}", sysPermission);
	}

	@Transactional
	@Override
	public void update(SysPermission sysPermission) {
		sysPermission.setUpdateTime(new Date());
		sysPermissionDao.updateByOps(sysPermission);
		log.info("修改权限标识：{}", sysPermission);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		SysPermission permission = sysPermissionDao.findById(id);
		if (permission == null) {
			throw new IllegalArgumentException("权限标识不存在");
		}

		sysPermissionDao.deleteOps(id);
		rolePermissionDao.deleteRolePermission(null, id);
		log.info("删除权限标识：{}", permission);
	}

	@Override
	public PageResult<SysPermission> findPermissions(Map<String, Object> params) {
		//设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
		if (MapUtils.getInteger(params, "page")!=null && MapUtils.getInteger(params, "limit")!=null)
			PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
		List<SysPermission> list  = sysPermissionDao.findList(params);
		PageInfo<SysPermission> pageInfo = new PageInfo(list);

		return PageResult.<SysPermission>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build()  ;

//		int total = sysPermissionDao.count(params);
//		List<SysPermission> list = Collections.emptyList();
//
//		if (total > 0) {
//			PageUtil.pageParamConver(params, false);
//			list = sysPermissionDao.findList(params);
//
//		}
//		return PageResult.<SysPermission>builder().data(list).code(0).count((long)total).build()  ;
	}

	@Override
	public void setAuthToRole(Long roleId, Set<Long> authIds) {
		rolePermissionDao.deleteRolePermission(roleId,null);

		if (!CollectionUtils.isEmpty(authIds)) {
			authIds.forEach(authId -> {
				rolePermissionDao.saveRolePermission(roleId, authId);
			});
		}

	}
}
