简称ocp是基于layui+springcloud的企业级微服务框架(用户权限管理，配置中心管理，应用管理，....),其核心的设计目标是分离前后端，快速开发部署，学习简单，功能强大，提供快速接入核心接口能力，其目标是帮助企业搭建一套类似百度能力开放平台的框架；

# **项目演示地址**

http://59.110.164.254:8066/login.html 用户名/密码：admin/admin

# **项目代码地址**

https://gitee.com/owenwangwen/open-capacity-platform

# 技术介绍
![image.png](https://upload-images.jianshu.io/upload_images/17103873-bec64504e9d9fdc4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


- 基于layui前后端分离的企业级微服务架构
- 兼容spring cloud netflix & spring cloud alibaba
- 优化Spring Security内部实现，实现API调用的统一出口和权限认证授权中心
- 提供完善的企业微服务流量监控，日志监控能力 
- 提供完善的压力测试方案
- 提供完善的灰度发布方案
- 提供完善的微服务部署方案，支持shell部署和docker两种方式

# 开发文档
[试读](https://www.kancloud.cn/owenwangwen/open-capacity-platform/content)
[正式文档](https://www.kancloud.cn/owenwangwen/open-capacity-platform) 

# 项目组织结构分析
![image.png](https://upload-images.jianshu.io/upload_images/17103873-b99932262f291be0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## open-capacity-platform能力开放平台管理

|     |       |  
| --- | --- |
|   ![image.png](https://upload-images.jianshu.io/upload_images/17103873-4ceab5033e74dc9d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| ![image.png](https://upload-images.jianshu.io/upload_images/17103873-505360c24cf15142.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| 
|   ![image.png](https://upload-images.jianshu.io/upload_images/17103873-8067231922929ada.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| ![image.png](https://upload-images.jianshu.io/upload_images/17103873-d73fe21dfb8b335d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| 
|   ![image.png](https://upload-images.jianshu.io/upload_images/17103873-eb5a27a4686b217d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| ![image.png](https://upload-images.jianshu.io/upload_images/17103873-1adbbecea04b5f87.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| 
|   ![image.png](https://upload-images.jianshu.io/upload_images/17103873-59443176a95d8106.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| ![image.png](https://upload-images.jianshu.io/upload_images/17103873-26e14ee3b791a8f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)| 
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-bb9395233ad74a30.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-486d47d158d43b7d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|


# 灰度发布 #

ocp灰度发布功能(参考dev分支) a.先启动 register-center 注册中心的 eureka-server 注册服务
b.在启动 api-gateway 网关服务 c.再启动 oauth-center 认证中心 oauth-server 认证服务 d.在启动 business-center 业务中心的 对应服务 user-center d.启动gray-center的discovery-console
e.启动gray-center的discovery-console-desktop

灰度管理UI
用户名:admin
密码 :admin
|     |       |  
| --- | --- |
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-26f2ae27bcec346d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-b4bd5362e461fef6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-c663072988cd5367.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-8a6586765e30d339.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|


# 链路跟踪 #
|     |       |  
| --- | --- |
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-dd7c4fa10a637eac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-d7ad1d6ee22a0265.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-1fd5f5ad339f81d8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-e3b293a71b100b9e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

# 系统监控 #

|     |       |  
| --- | --- |
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-dde6f10a37ae6e5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-a8c2ea37b57c5119.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

# api-gateway vs new-api-gateway #
内网测试网关/api-user/users/current
zuul-->api-gateway
|     |       |  
| --- | --- |
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-9cb52130a5462bbb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-f2dd07520e90be2e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-4d7e3a3e1273d360.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-8980afe848c9ca5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

spring cloud gateway -->new-api-gateway

|     |       |  
| --- | --- |
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-c24c7d20405be7fc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-87633cdadbf9e88e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
|![image.png](https://upload-images.jianshu.io/upload_images/17103873-b32568183b5d44c9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![image.png](https://upload-images.jianshu.io/upload_images/17103873-3565fcb99ea65f6e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
