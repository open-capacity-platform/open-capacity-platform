package com.open.capacity.oauth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.oauth.dto.ClientDto;
import com.open.capacity.oauth.model.SysService;
import com.open.capacity.oauth.service.ISysServiceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 16:20]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@RestController
@Api(tags = "服务模块api")
@RequestMapping("/services")
public class SysServiceController {

    @Autowired
    private ISysServiceService iSysServiceService;

    /**
     * 查询所有服务
     * @return
     */
    @PreAuthorize("hasAuthority('service:get/service/findAlls')")
    @ApiOperation(value = "查询所有服务")
    @GetMapping("/findAlls")
    public PageResult<SysService> findAlls() {
        List<SysService> list = iSysServiceService.findAll();

        return PageResult.<SysService>builder().data(list).code(0).count((long)list.size()).build() ;
    }

    /**
     * 获取服务以及顶级服务
     * @return
     */
    @ApiOperation(value = "获取服务以及顶级服务")
    @GetMapping("/findOnes")
    @PreAuthorize("hasAuthority('service:get/service/findOnes')")
    public PageResult<SysService> findOnes(){
        List<SysService> list = iSysServiceService.findOnes();
        return PageResult.<SysService>builder().data(list).code(0).count((long)list.size()).build() ;
    }

    /**
     * 删除服务
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('service:delete/service/{id}')")
    @ApiOperation(value = "删除服务")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        try {
            iSysServiceService.delete(id);

        }catch (Exception ex){
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
        return Result.succeed("操作成功");
    }

    @PreAuthorize("hasAnyAuthority('service:post/saveOrUpdate')")
    @ApiOperation(value = "新增服务")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SysService service) {
        try{
            if (service.getId() != null){
                iSysServiceService.update(service);
            }else {
                iSysServiceService.save(service);
            }
            return Result.succeed("操作成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "根据clientId获取对应的服务")
    @GetMapping("/{clientId}/services")
    public List<Map<String, Object>> findServicesByclientId(@PathVariable Long clientId) {
        Set<Long> clientIds = new HashSet<Long>() {{ add(clientId); }};
        List<SysService> clientService = iSysServiceService.findByClient(clientIds);
        List<SysService> allService = iSysServiceService.findAll();
        List<Map<String, Object>> authTrees = new ArrayList<>();

        Map<Long,SysService> clientServiceMap = clientService.stream().collect(Collectors.toMap(SysService::getId,SysService->SysService));

        for (SysService sysService: allService) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id",sysService.getId());
            authTree.put("name",sysService.getName());
            authTree.put("pId",sysService.getParentId());
            authTree.put("open",true);
            authTree.put("checked", false);
            if (clientServiceMap.get(sysService.getId())!=null){
                authTree.put("checked", true);
            }
            authTrees.add(authTree);
        }

        return  authTrees;
    }

    @PostMapping("/granted")
    public Result setMenuToClient(@RequestBody ClientDto clientDto) {
        iSysServiceService.setMenuToClient(clientDto.getId(), clientDto.getServiceIds());

        return Result.succeed("操作成功");
    }















}
