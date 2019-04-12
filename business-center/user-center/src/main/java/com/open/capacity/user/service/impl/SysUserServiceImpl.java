package com.open.capacity.user.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.easypoi.user.SysUserExcel;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.model.system.SysPermission;
import com.open.capacity.model.system.SysRole;
import com.open.capacity.model.system.SysUser;
import com.open.capacity.model.system.constants.UserType;
import com.open.capacity.user.dao.SysUserDao;
import com.open.capacity.user.dao.SysUserRoleDao;
import com.open.capacity.user.service.SysPermissionService;
import com.open.capacity.user.service.SysUserService;
import com.open.capacity.utils.PageUtil;
import com.open.capacity.utils.PhoneUtil;
import com.open.capacity.utils.SysUserUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysUserRoleDao userRoleDao;

	@Autowired(required = false)
	private TokenStore redisTokenStore;

	@Transactional
	@Override
	public void addSysUser(SysUser sysUser) {
		String username = sysUser.getUsername();
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("用户名不能为空");
		}

		if (PhoneUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
			throw new IllegalArgumentException("用户名要包含英文字符");
		}

		if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
			throw new IllegalArgumentException("用户名不能包含@");
		}

		if (username.contains("|")) {
			throw new IllegalArgumentException("用户名不能包含|字符");
		}

		if (StringUtils.isBlank(sysUser.getPassword())) {
			throw new IllegalArgumentException("密码不能为空");
		}

		if (StringUtils.isBlank(sysUser.getNickname())) {
			sysUser.setNickname(username);
		}

		if (StringUtils.isBlank(sysUser.getType())) {
			sysUser.setType(UserType.APP.name());
		}

		SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
		if (persistenceUser != null && persistenceUser.getUsername() != null) {
			throw new IllegalArgumentException("用户名已存在");

		}

		sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
		sysUser.setEnabled(Boolean.TRUE);
		sysUser.setCreateTime(new Date());
		sysUser.setUpdateTime(sysUser.getCreateTime());
		sysUserDao.save(sysUser);
		log.info("添加用户：{}", sysUser);
	}

	@Transactional
	@Override
	public SysUser updateSysUser(SysUser sysUser) {
		sysUser.setUpdateTime(new Date());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
			authentication = oAuth2Auth.getUserAuthentication();

			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Auth.getDetails();

			LoginAppUser user = SysUserUtil.getLoginAppUser();

			if (user != null) {

				if ( !ObjectUtils.notEqual(user.getId(),sysUser.getId()) ) {

					OAuth2AccessToken token = redisTokenStore.readAccessToken(details.getTokenValue());

					if (token != null) {

						if (!StringUtils.isBlank(sysUser.getHeadImgUrl())) {
							user.setHeadImgUrl(sysUser.getHeadImgUrl());
						}

						if (!StringUtils.isBlank(sysUser.getNewPassword())) {
							user.setPassword(sysUser.getNewPassword());
						}

						if (!StringUtils.isBlank(sysUser.getNewPassword())) {
							user.setPassword(sysUser.getNewPassword());
						}

						if (!StringUtils.isBlank(sysUser.getNickname())) {
							user.setNickname(sysUser.getNickname());
						}

						if (!StringUtils.isBlank(sysUser.getPhone())){
							user.setPhone(sysUser.getPhone());
						}

						if (sysUser.getSex() != null) {
							user.setSex(sysUser.getSex());
						}

						UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,
		                        null, user.getAuthorities());

						OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Auth.getOAuth2Request(), userAuthentication);
						oAuth2Authentication.setAuthenticated(true);
						redisTokenStore.storeAccessToken(token, oAuth2Authentication);

					}

				}

			}
		}

		sysUserDao.updateByOps(sysUser);
		log.info("修改用户：{}", sysUser);
		return sysUser;
	}

	@Transactional
	@Override
	public LoginAppUser findByUsername(String username) {
		SysUser sysUser = sysUserDao.findUserByUsername(username);
		if (sysUser != null) {
			LoginAppUser loginAppUser = new LoginAppUser();
			BeanUtils.copyProperties(sysUser, loginAppUser);

			Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(sysUser.getId());
			loginAppUser.setSysRoles(sysRoles);// 设置角色

			if (!CollectionUtils.isEmpty(sysRoles)) {
				Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
				Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
				if (!CollectionUtils.isEmpty(sysPermissions)) {
					Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
							.collect(Collectors.toSet());

					loginAppUser.setPermissions(permissions);// 设置权限集合
				}

			}

			return loginAppUser;
		}

		return null;
	}

	@Override
	public SysUser findById(Long id) {
		return sysUserDao.findById(id);
	}

	/**
	 * 给用户设置角色
	 */
	@Transactional
	@Override
	public void setRoleToUser(Long id, Set<Long> roleIds) {
		SysUser sysUser = sysUserDao.findById(id);
		if (sysUser == null) {

			throw new IllegalArgumentException("用户不存在");
		}

		userRoleDao.deleteUserRole(id, null);
		if (!CollectionUtils.isEmpty(roleIds)) {
			roleIds.forEach(roleId -> {
				userRoleDao.saveUserRoles(id, roleId);
			});
		}

		log.info("修改用户：{}的角色，{}", sysUser.getUsername(), roleIds);
	}

	@Transactional
	@Override
	public Result updatePassword(Long id, String oldPassword, String newPassword) {
		SysUser sysUser = sysUserDao.findById(id);
		if (StringUtils.isNoneBlank(oldPassword)) {
			if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
				return Result.failed("旧密码错误");
			}
		}

		SysUser user = new SysUser();
		user.setId(id);
		user.setPassword(passwordEncoder.encode(newPassword));

		updateSysUser(user);
		log.info("修改密码：{}", user);
		return Result.succeed("修改成功");
	}

	@Override
	public PageResult<SysUser> findUsers(Map<String, Object> params) {
		int total = sysUserDao.count(params);
		List<SysUser> list = Collections.emptyList();
		if (total > 0) {
			PageUtil.pageParamConver(params, true);
			list = sysUserDao.findList(params);

			List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

			List<SysRole> sysRoles = userRoleDao.findRolesByUserIds(userIds);

			list.forEach(u -> {
				u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getUserId()))
						.collect(Collectors.toList()));
			});
		}
		return PageResult.<SysUser>builder().data(list).code(0).count((long)total).build();
	}

	@Override
	public Set<SysRole> findRolesByUserId(Long userId) {
		return userRoleDao.findRolesByUserId(userId);
	}

	@Override
	public Result updateEnabled(Map<String, Object> params) {
		Long id = MapUtils.getLong(params, "id");
		Boolean enabled = MapUtils.getBoolean(params, "enabled");

		SysUser appUser = sysUserDao.findById(id);
		if (appUser == null) {
			return Result.failed("用户不存在");
			//throw new IllegalArgumentException("用户不存在");
		}
		appUser.setEnabled(enabled);
		appUser.setUpdateTime(new Date());

		int i = sysUserDao.updateByOps(appUser);
		log.info("修改用户：{}", appUser);

		return i > 0 ? Result.succeed(appUser, "更新成功") : Result.failed("更新失败");
	}

	@Transactional
	@Override
	public Result saveOrUpdate(SysUser sysUser) {
		String username = sysUser.getUsername();
		if (StringUtils.isBlank(username)) {
			//throw new IllegalArgumentException("用户名不能为空");
			return Result.failed("用户名不能为空");
		}

		if (PhoneUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
			//throw new IllegalArgumentException("用户名要包含英文字符");
			return Result.failed("用户名要包含英文字符");
		}

		if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
			//throw new IllegalArgumentException("用户名不能包含@");
			return Result.failed("用户名不能包含@");
		}

		if (username.contains("|")) {
			//throw new IllegalArgumentException("用户名不能包含|字符");
			return Result.failed("用户名不能包含|字符");
		}

		if (StringUtils.isBlank(sysUser.getNickname())) {
			sysUser.setNickname(username);
		}

		if (StringUtils.isBlank(sysUser.getType())) {
			sysUser.setType(UserType.APP.name());
		}

		sysUser.setPassword(passwordEncoder.encode("123456"));
		sysUser.setEnabled(Boolean.TRUE);
		sysUser.setCreateTime(new Date());

		int i = 0;

		if (sysUser.getId() == null) {
			SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
			if (persistenceUser != null && persistenceUser.getUsername() != null) {
				//throw new IllegalArgumentException("用户名已存在");
				return Result.failed("用户名已存在");
			}
			sysUser.setUpdateTime(sysUser.getCreateTime());
			i = sysUserDao.insert(sysUser);
		} else {
			sysUser.setUpdateTime(new Date());
			i = sysUserDao.updateByOps(sysUser);
		}

		userRoleDao.deleteUserRole(sysUser.getId(), null);
		List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
		if (!CollectionUtils.isEmpty(roleIds)) {
			roleIds.forEach(roleId -> {
				userRoleDao.saveUserRoles(sysUser.getId(), Long.parseLong(roleId.toString()));
			});
		}

		return i > 0 ? Result.succeed(sysUser, "操作成功") : Result.failed("操作失败");
	}

	@Override
	public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
		List<SysUserExcel> sysUserExcels = new ArrayList<>();
		List<SysUser> list = sysUserDao.findList(params);

		for (SysUser sysUser : list){
			SysUserExcel sysUserExcel = new SysUserExcel();
			BeanUtils.copyProperties(sysUser,sysUserExcel);
			sysUserExcels.add(sysUserExcel);
		}
		return sysUserExcels;
	}
}