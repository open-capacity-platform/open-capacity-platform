package com.open.capacity.es.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.open.capacity.es.entity.LogDocument;

/**
 * @author zlt
 */
@Repository
public interface EsLogDao extends ElasticsearchRepository<LogDocument, String> {

}