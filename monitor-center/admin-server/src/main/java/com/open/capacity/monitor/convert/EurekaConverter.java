//package com.open.capacity.monitor.convert;
//
//import static org.springframework.util.StringUtils.isEmpty;
//
//import java.net.URI;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.EurekaServiceInstance;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.netflix.appinfo.InstanceInfo;
//
//import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
//import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
//import de.codecentric.boot.admin.server.domain.values.Registration;
//
//
///**
// * @author 作者 owen E-mail: 624191343@qq.com
// * @version 创建时间：2017年11月24日 下午5:34:47 类说明
// * 偷梁换柱 将druid数据库连接池 注册到监控中心
// */
//public class EurekaConverter implements ServiceInstanceConverter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServiceInstanceConverter.class);
//    private static final String KEY_MANAGEMENT_PORT = "management.port";
//    private static final String KEY_MANAGEMENT_PATH = "management.context-path";
//    private static final String KEY_HEALTH_PATH = "health.path";
//
//    /**
//     * Default context-path to be appended to the url of the discovered service for the
//     * managment-url.
//     */
//    private String managementContextPath = "/actuator";
//    /**
//     * Default path of the health-endpoint to be used for the health-url of the discovered service.
//     */
//    private String healthEndpointPath = "health";
//
//    @Override
//    public Registration convert(ServiceInstance instance) {
//        LOGGER.debug("Converting service '{}' running at '{}' with metadata {}", instance.getServiceId(),
//            instance.getUri(), instance.getMetadata());
//
//        Registration.Builder builder = Registration.create(instance.getServiceId(), getHealthUrl1(instance).toString());
//
//        URI managementUrl = getManagementUrl(instance);
//        if (managementUrl != null) {
//            builder.managementUrl(managementUrl.toString());
//        }
//
//        URI serviceUrl = getServiceUrl(instance);
//        if (serviceUrl != null) {
//        	
//        	if("EUREKA-SERVER".equals(instance.getServiceId())) {
//        		builder.serviceUrl( serviceUrl.toString() );
//        	} else if ("ADMIN-SERVER".equals(instance.getServiceId())){
//        		builder.serviceUrl( serviceUrl.toString() );
//        	}else if ("AUTH-GATEWAY".equals(instance.getServiceId())){
//        		builder.serviceUrl( serviceUrl.toString().replaceFirst("/+$", "") + "/druid" );
//        	}else{
//        		builder.serviceUrl( serviceUrl.toString().replaceFirst("/+$", "") + "/druid" );
//        	}
//        	
//            
//        }
//
//        Map<String, String> metadata = getMetadata(instance);
//        if (metadata != null) {
//            builder.metadata(metadata);
//        }
//
//        return builder.build();
//    }
//
//    protected URI getHealthUrl1(ServiceInstance instance) {
//        String healthPath = instance.getMetadata().get(KEY_HEALTH_PATH);
//        if (isEmpty(healthPath)) {
//            healthPath = healthEndpointPath;
//        }
//
//        return UriComponentsBuilder.fromUri(getManagementUrl(instance)).path("/").path(healthPath).build().toUri();
//    }
//    
//    protected URI getHealthUrl(ServiceInstance instance) {
//        Assert.isInstanceOf(EurekaServiceInstance.class, instance,
//            "serviceInstance must be of type EurekaServiceInstance");
//
//        InstanceInfo instanceInfo = ((EurekaServiceInstance) instance).getInstanceInfo();
//        String healthUrl = instanceInfo.getSecureHealthCheckUrl();
//        if (StringUtils.isEmpty(healthUrl)) {
//            healthUrl = instanceInfo.getHealthCheckUrl();
//        }
//        return URI.create(healthUrl);
//    }
//
//    
//    
//    protected URI getManagementUrl(ServiceInstance instance) {
//        String managamentPath = instance.getMetadata().get(KEY_MANAGEMENT_PATH);
//        if (isEmpty(managamentPath)) {
//            managamentPath = managementContextPath;
//        }
//
//        URI serviceUrl = getServiceUrl(instance);
//        String managamentPort = instance.getMetadata().get(KEY_MANAGEMENT_PORT);
//        if (isEmpty(managamentPort)) {
//            managamentPort = String.valueOf(serviceUrl.getPort());
//        }
//
//        return UriComponentsBuilder.fromUri(serviceUrl)
//                                   .port(managamentPort)
//                                   .path("/")
//                                   .path(managamentPath)
//                                   .build()
//                                   .toUri();
//    }
//
//    protected URI getServiceUrl(ServiceInstance instance) {
//        return UriComponentsBuilder.fromUri(instance.getUri()).path("/").build().toUri();
//    }
//
//    protected Map<String, String> getMetadata(ServiceInstance instance) {
//        return instance.getMetadata();
//    }
//
//
//    public void setManagementContextPath(String managementContextPath) {
//        this.managementContextPath = managementContextPath;
//    }
//
//    public String getManagementContextPath() {
//        return managementContextPath;
//    }
//
//    public void setHealthEndpointPath(String healthEndpointPath) {
//        this.healthEndpointPath = healthEndpointPath;
//    }
//
//    public String getHealthEndpointPath() {
//        return healthEndpointPath;
//    }
//}
//
