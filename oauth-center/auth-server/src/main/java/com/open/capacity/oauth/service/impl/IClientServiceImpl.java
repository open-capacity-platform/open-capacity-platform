package com.open.capacity.oauth.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.oauth.dao.ClientDao;
import com.open.capacity.oauth.dao.SysClientServiceDao;
import com.open.capacity.oauth.dto.ClientDto;
import com.open.capacity.oauth.model.Client;
import com.open.capacity.oauth.service.IClientService;

@Service
public class IClientServiceImpl implements IClientService {

    private static final Logger log = LoggerFactory.getLogger(IClientServiceImpl.class);

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    private static final String CACHE_CLIENT_KEY = "oauth_client_details";

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private SysClientServiceDao sysClientServiceDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void saveClient(ClientDto clientDto) {
        Client client = clientDto;
        List<Long> permissionIds = clientDto.getPermissionIds();

        if (client.getId() != null) {// 修改
            updateClient(client, permissionIds);
        } else {// 新增
            saveClient(client, permissionIds);
        }
    }

    @Override
    public Result saveOrUpdate(ClientDto clientDto) {
        clientDto.setClientSecret(passwordEncoder.encode(clientDto.getClientSecretStr()));

        if (clientDto.getId() != null) {// 修改
            clientDao.update(clientDto);
        } else {// 新增
            Client r = clientDao.getClient(clientDto.getClientId());
            if (r != null) {
                return Result.failed(clientDto.getClientId()+"已存在");
            }
            clientDao.save(clientDto);
        }
        return Result.succeed("操作成功");
    }

    private void saveClient(Client client, List<Long> permissionIds) {
        Client r = clientDao.getClient(client.getClientId());
        if (r != null) {
            throw new IllegalArgumentException(client.getClientId() + "已存在");
        }

        clientDao.save(client);
        if (!CollectionUtils.isEmpty(permissionIds)) {
            clientDao.saveClientPermission(client.getId(), permissionIds);
        }
        log.debug("新增应用{}", client.getClientId());
    }

    private void updateClient(Client client, List<Long> permissionIds) {
//		Client r = clientDao.getClient(client.getClientId());
//		if (r != null && r.getId() != client.getId()) {
//			throw new IllegalArgumentException(client.getClientId() + "已存在");
//		}

        clientDao.update(client);


        clientDao.deleteClientPermission(client.getId());
        if (!CollectionUtils.isEmpty(permissionIds)) {
            clientDao.saveClientPermission(client.getId(), permissionIds);
        }

        String clientId = clientDao.getById(client.getId()).getClientId();

        BaseClientDetails clientDetails = null;

        // 先从redis获取
        try {
            String value = (String) redisTemplate.boundHashOps(CACHE_CLIENT_KEY).get(clientId);
            clientDetails = JSONObject.parseObject(value, BaseClientDetails.class);
            clientDetails.setClientSecret(client.getClientSecret());
            redisTemplate.boundHashOps(CACHE_CLIENT_KEY).put(clientId, JSONObject.toJSONString(clientDetails));


        } catch (Exception e) {

        }


        log.debug("修改应用{}", client.getClientId());
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        clientDao.delete(id);

        sysClientServiceDao.delete(id,null);

        log.debug("删除应用id:{}", id);
    }

	@Override
	public PageResult<Client> listRoles(Map<String, Object> params) {

        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);
        List<Client> list = clientDao.findList(params);
        PageInfo<Client> pageInfo = new PageInfo<>(list);
        return PageResult.<Client>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build()  ;

//		// TODO Auto-generated method stub
//		int total = clientDao.count(params);
//		List<Client> list = Collections.emptyList();
//
//		if (total > 0) {
//			PageUtil.pageParamConver(params, false);
//			list = clientDao.findList(params);
//
//		}
//		return PageResult.<Client>builder().data(list).code(0).count((long)total).build()  ;
	}
	public  Client getById(Long id) {
		return clientDao.getById(id);
	}

	@Override
	public List<Client> findList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return clientDao.findList(params);
	}

	@Override
	public List<Client> listByUserId(Long userId) {
		// TODO Auto-generated method stub
		return clientDao.listByUserId(userId);
	}

}
