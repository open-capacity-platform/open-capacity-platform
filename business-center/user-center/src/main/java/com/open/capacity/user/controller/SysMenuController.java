package com.open.capacity.user.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.log.monitor.PointUtil;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.model.system.SysMenu;
import com.open.capacity.model.system.SysRole;
import com.open.capacity.user.service.SysMenuService;
import com.open.capacity.utils.SysUserUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "菜单模块api")
@RequestMapping("/menus")
public class SysMenuController {

	@Autowired
	private SysMenuService menuService;
	private static Logger log = LoggerFactory.getLogger(SysMenuController.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	// <!-- -->
	/**
	 * 删除菜单
	 * 
	 * @param id
	 */
	@PreAuthorize("hasAuthority('menu:delete/menus/{id}')")
	@ApiOperation(value = "删除菜单")
	@DeleteMapping("/{id}")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result delete(@PathVariable Long id) {

		try {
			menuService.delete(id);
			return Result.succeed("操作成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			return Result.failed("操作失败");
		}

	}

	@PreAuthorize("hasAuthority('menu:get/menus/{roleId}/menus')")
	@ApiOperation(value = "根据roleId获取对应的菜单")
	@GetMapping("/{roleId}/menus")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public List<Map<String, Object>> findMenusByRoleId(@PathVariable Long roleId) {

		Set<Long> roleIds = new HashSet<Long>() {
			{
				add(roleId);
			}
		};
		List<SysMenu> roleMenus = menuService.findByRoles(roleIds); // 获取该角色对应的菜单
		List<SysMenu> allMenus = menuService.findAll(); // 全部的菜单列表
		List<Map<String, Object>> authTrees = new ArrayList<>();

		Map<Long, SysMenu> roleMenusMap = roleMenus.stream()
				.collect(Collectors.toMap(SysMenu::getId, SysMenu -> SysMenu));

		for (SysMenu sysMenu : allMenus) {
			Map<String, Object> authTree = new HashMap<>();
			authTree.put("id", sysMenu.getId());
			authTree.put("name", sysMenu.getName());
			authTree.put("pId", sysMenu.getParentId());
			authTree.put("open", true);
			authTree.put("checked", false);
			if (roleMenusMap.get(sysMenu.getId()) != null) {
				authTree.put("checked", true);
			}
			authTrees.add(authTree);
		}
		return authTrees;
	}

	/**
	 * 给角色分配菜单
	 */
	@PreAuthorize("hasAuthority('menu:post/menus/granted')")
	@ApiOperation(value = "角色分配菜单")
	@PostMapping("/granted")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result setMenuToRole(@RequestBody SysMenu sysMenu) {

		menuService.setMenuToRole(sysMenu.getRoleId(), sysMenu.getMenuIds());

		return Result.succeed("操作成功");

	}

	@PreAuthorize("hasAuthority('menu:get/menus/findAlls')")
	@ApiOperation(value = "查询所有菜单")
	@GetMapping("/findAlls")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public PageResult<SysMenu> findAlls() {

		List<SysMenu> list = menuService.findAll();

		return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
	}

	@ApiOperation(value = "获取菜单以及顶级菜单")
	@GetMapping("/findOnes")
	@PreAuthorize("hasAuthority('menu:get/menus/findOnes')")
	public PageResult<SysMenu> findOnes() {
		List<SysMenu> list = menuService.findOnes();
		return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
	}

	/**
	 * 添加菜单 或者 更新
	 * 
	 * @param menu
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('menu:post/menus','menu:put/menus')")
	@ApiOperation(value = "新增菜单")
	@PostMapping("saveOrUpdate")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result saveOrUpdate(@RequestBody SysMenu menu) {

		try {
			if (menu.getId() != null) {
				menuService.update(menu);
			} else {
				menuService.save(menu);
			}
			return Result.succeed("操作成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			return Result.failed("操作失败");
		}

	}

	/**
	 * 当前登录用户的菜单
	 * 
	 * @return
	 */
	@PreAuthorize("hasAuthority('menu:get/menus/current')")
	@GetMapping("/current")
	@ApiOperation(value = "查询当前用户菜单")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public List<SysMenu> findMyMenu() {

		LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
		Set<SysRole> roles = loginAppUser.getSysRoles();
		if (CollectionUtils.isEmpty(roles)) {
			return Collections.emptyList();
		}

		List<SysMenu> menus = menuService
				.findByRoles(roles.parallelStream().map(SysRole::getId).collect(Collectors.toSet()));

		List<SysMenu> sysMenus = TreeBuilder(menus);

		return sysMenus;
	}

	/**
	 * 两层循环实现建树
	 * 
	 * @param sysMenus
	 * @return
	 */
	public static List<SysMenu> TreeBuilder(List<SysMenu> sysMenus) {

		List<SysMenu> menus = new ArrayList<SysMenu>();
		for (SysMenu sysMenu : sysMenus) {
			if (ObjectUtils.equals(-1L, sysMenu.getParentId())) {
				menus.add(sysMenu);
			}
			for (SysMenu menu : sysMenus) {
				if (menu.getParentId().equals(sysMenu.getId())) {
					if (sysMenu.getSubMenus() == null) {
						sysMenu.setSubMenus(new ArrayList<>());
					}
					sysMenu.getSubMenus().add(menu);
				}
			}
		}
		return menus;
	}

}
