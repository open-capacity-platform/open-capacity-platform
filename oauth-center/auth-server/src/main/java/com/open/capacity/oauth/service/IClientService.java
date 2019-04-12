package com.open.capacity.oauth.service;

import java.util.List;
import java.util.Map;

import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.oauth.dto.ClientDto;
import com.open.capacity.oauth.model.Client;

public interface IClientService {

	
	Client getById(Long id) ;
	 
    void saveClient(ClientDto clientDto);

    Result saveOrUpdate(ClientDto clientDto);

    void deleteClient(Long id);
    
    public PageResult<Client> listRoles(Map<String, Object> params);
    
    List<Client> findList(Map<String, Object> params) ;
    
    List<Client> listByUserId(Long userId) ;
    
}
