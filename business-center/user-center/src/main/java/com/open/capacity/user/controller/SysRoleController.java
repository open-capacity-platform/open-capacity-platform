package com.open.capacity.user.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.log.monitor.PointUtil;
import com.open.capacity.model.system.SysRole;
import com.open.capacity.user.service.SysRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
* 角色管理
 */
@RestController
@Api(tags = "角色模块api")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	private static Logger log = LoggerFactory.getLogger(SysRoleController.class);
	private ObjectMapper objectMapper = new ObjectMapper();


//	<!-- -->
	/**
	 * 后台管理查询角色
	 * @param params
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PreAuthorize("hasAuthority('role:get/roles')")
	@ApiOperation(value = "后台管理查询角色")
	@GetMapping("/roles")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public PageResult<SysRole> findRoles(@RequestParam Map<String, Object> params) throws JsonProcessingException {
		return sysRoleService.findRoles(params);
	}

	/**
	 * 角色新增或者更新
	 * @param sysRole
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PreAuthorize("hasAnyAuthority('role:post/roles','role:put/roles')")
	@PostMapping("/roles/saveOrUpdate")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result saveOrUpdate(@RequestBody SysRole sysRole) throws JsonProcessingException {
		return sysRoleService.saveOrUpdate(sysRole);
	}

	/**
	 * 后台管理删除角色
	 * delete /role/1
	 * @param id
	 */
	@PreAuthorize("hasAuthority('role:delete/roles/{id}')")
	@ApiOperation(value = "后台管理删除角色")
	@DeleteMapping("/roles/{id}")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result deleteRole(@PathVariable Long id) {
		try {
			if (id == 1L){
				return Result.failed("管理员不可以删除");
			}
			sysRoleService.deleteRole(id);
			return Result.succeed("操作成功");
		}catch (Exception e){
			e.printStackTrace();
			return Result.failed("操作失败");
		}
	}

}
