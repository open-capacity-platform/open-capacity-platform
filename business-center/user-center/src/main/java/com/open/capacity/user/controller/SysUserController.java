package com.open.capacity.user.controller;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.easypoi.user.SysUserExcel;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.model.system.SysRole;
import com.open.capacity.model.system.SysUser;
import com.open.capacity.user.service.SysUserService;
import com.open.capacity.utils.SysUserUtil;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 *用户
 */
@Slf4j
@RestController
@Api(tags = "用户模块api")
public class SysUserController {

    @Autowired
    private SysUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 当前登录用户 LoginAppUser
     *
     * @return
     * @throws JsonProcessingException 
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/users/current")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public LoginAppUser getLoginAppUser()   {
		LoginAppUser loginUser = null ;
		try {
			loginUser = SysUserUtil.getLoginAppUser() ;
		} catch (Exception e) {
		}
		
        return loginUser ;
    }
    
    @GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public LoginAppUser findByUsername(String username) {
        return appUserService.findByUsername(username);
    }



    @PreAuthorize("hasAuthority('user:get/users/{id}')")
    @GetMapping("/users/{id}")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public SysUser findUserById(@PathVariable Long id) {
        return appUserService.findById(id);
    }

    /**
     * 管理后台，给用户重置密码
     *
     * @param id
     * @param newPassword
     */
    @PreAuthorize("hasAnyAuthority('user:put/users/password','user:post/users/{id}/resetPassword')")
    @PutMapping(value = "/users/{id}/password", params = {"newPassword"})
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public void resetPassword(@PathVariable Long id, String newPassword) {
        appUserService.updatePassword(id, null, newPassword);
    }

    /**
     * 管理后台修改用户
     *
     * @param sysUser
     * @throws JsonProcessingException 
     */
    @PreAuthorize("hasAuthority('user:put/users/me')")
    @PutMapping("/users")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public void updateSysUser(@RequestBody SysUser sysUser) throws JsonProcessingException {
        appUserService.updateSysUser(sysUser);
    }

    /**
     * 管理后台给用户分配角色
     *
     * @param id
     * @param roleIds
     * @throws JsonProcessingException 
     */
    @PreAuthorize("hasAuthority('user:post/users/{id}/roles')")
    @PostMapping("/users/{id}/roles")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public void setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) throws JsonProcessingException {
        appUserService.setRoleToUser(id, roleIds);
    }

    /**
     * 获取用户的角色
     *
     * @param
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:get/users/{id}/roles')")
    @GetMapping("/users/{id}/roles")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public Set<SysRole> findRolesByUserId(@PathVariable Long id) {
        return appUserService.findRolesByUserId(id);
    }


//    <!-- -->
    /**
     * 用户查询
     * http://192.168.3.2:7000/users?access_token=3b45d059-601b-4c63-85f9-9d77128ee94d&start=0&length=10
     * @param params
     * @return
     * @throws JsonProcessingException 
     */
    @PreAuthorize("hasAuthority('user:get/users')")
    @ApiOperation(value = "用户查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping("/users")
    @LogAnnotation(module="user-center",recordRequestParam=false)
//  searchKey=username, searchValue=as
    public PageResult<SysUser> findUsers(@RequestParam Map<String, Object> params) throws JsonProcessingException {
        return appUserService.findUsers(params);
    }

    /**
     * 修改自己的个人信息
     *
     * @param sysUser
     * @return
     * @throws JsonProcessingException 
     */
    @PutMapping("/users/me")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    @PreAuthorize("hasAnyAuthority('user:put/users/me','user:post/users/saveOrUpdate')")
    public Result updateMe(@RequestBody SysUser sysUser) throws JsonProcessingException {
//        SysUser user = SysUserUtil.getLoginAppUser();
//        sysUser.setId(user.getId());
        SysUser user = appUserService.updateSysUser(sysUser);

        return Result.succeed(user,"操作成功");
    }

    /**
     * 修改密码
     *
     * @param sysUser
     * @throws JsonProcessingException 
     */
    @PutMapping(value = "/users/password")
    @PreAuthorize("hasAuthority('user:put/users/password')")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public Result updatePassword(@RequestBody SysUser sysUser) throws JsonProcessingException {
        if (StringUtils.isBlank(sysUser.getOldPassword())) {
            throw new IllegalArgumentException("旧密码不能为空");
        }
        if (StringUtils.isBlank(sysUser.getNewPassword())) {
            throw new IllegalArgumentException("新密码不能为空");
        }

        if (sysUser.getId() == 1L){
            return Result.failed("超级管理员不给予修改");
        }

        return appUserService.updatePassword(sysUser.getId(), sysUser.getOldPassword(), sysUser.getNewPassword());
    }

    /**
     *  修改用户状态
     * @param params
     * @return
     * @author gitgeek
     */
    @ApiOperation(value = "修改用户状态")
    @GetMapping("/users/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "enabled",value = "是否启用", required = true, dataType = "Boolean")
    })
    @LogAnnotation(module="user-center",recordRequestParam=false)
    @PreAuthorize("hasAnyAuthority('user:get/users/updateEnabled' ,'user:put/users/me')")
    public Result updateEnabled(@RequestParam Map<String, Object> params){
        Long id = MapUtils.getLong(params, "id");
        if (id == 1L){
            return Result.failed("超级管理员不给予修改");
        }
        return appUserService.updateEnabled(params);
    }

    /**
     * 管理后台，给用户重置密码
     * @param id
     * @author gitgeek
     */
    @PreAuthorize("hasAuthority('user:post/users/{id}/resetPassword' )")
    @PostMapping(value = "/users/{id}/resetPassword")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public Result resetPassword(@PathVariable Long id) {
        if (id == 1L){
            return Result.failed("超级管理员不给予修改");
        }
        appUserService.updatePassword(id, null, "123456");
        return Result.succeed(null,"重置成功");
    }


    /**
     * 新增or更新
     * @param sysUser
     * @return
     */
    @PostMapping("/users/saveOrUpdate")
    @PreAuthorize("hasAnyAuthority('user:post/users/saveOrUpdate')")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public Result saveOrUpdate(@RequestBody SysUser sysUser) {
        return  appUserService.saveOrUpdate(sysUser);
    }

    /**
     * 导出数据
     * @return
     */
    @PostMapping("/users/exportUser")
    @PreAuthorize("hasAuthority('user:post/users/exportUser')")
    public void exportUser(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        List<SysUserExcel> result = appUserService.findAllUsers(params);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
        OutputStream ouputStream = null;
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户导出","用户"),
                SysUserExcel.class, result );
        try {
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        } finally {
            try {
                ouputStream.flush();
                ouputStream.close();
            } catch (Exception e) {
                throw new RuntimeException("系统异常");
            }
        }
    }








}
