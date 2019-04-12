package com.open.capacity.es.controller;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.commons.PageResult;
import com.open.capacity.es.dao.EsLogDao;
import com.open.capacity.es.entity.LogDocument;
import com.open.capacity.log.monitor.PointUtil;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 * @author zlt
 */
@RestController
public class EsLogController {
	
	
	private static Logger log = LoggerFactory.getLogger(EsLogController.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String ES_PARAM_MESSAGE = "message";
	private static final String ES_PARAM_LOG_LEVEL = "logLevel";
	private static final String ES_PARAM_APP_NAME = "appName";
	private static final String ES_PARAM_CLASSNAME = "classname";

	@Autowired
	public EsLogDao logDao;

	@ApiOperation(value = "系统日志查询列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
	})
	@GetMapping(value="/sysLog")
	public PageResult<LogDocument> getPage(@RequestParam Map<String, Object> params) throws JsonProcessingException {
		
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		String searchKey = (String)params.get("searchKey");
		String searchValue = (String)params.get("searchValue");
		if (StrUtil.isNotEmpty(searchKey)) {
			if (ES_PARAM_MESSAGE.equals(searchKey)) {
				// 分词查询 日志信息
				builder.must(QueryBuilders.matchPhraseQuery(ES_PARAM_MESSAGE, searchValue));
			}
			if (ES_PARAM_LOG_LEVEL.equals(searchKey)) {
				// 模糊查询
				builder.must(QueryBuilders.matchPhraseQuery(ES_PARAM_LOG_LEVEL, searchValue.toUpperCase()));
			}
			if (ES_PARAM_APP_NAME.equals(searchKey)) {
				// 模糊查询
				builder.must(QueryBuilders.fuzzyQuery(ES_PARAM_APP_NAME, searchValue));
			}
			if (ES_PARAM_CLASSNAME.equals(searchKey)) {
				// 模糊查询
				builder.must(QueryBuilders.fuzzyQuery(ES_PARAM_CLASSNAME, searchValue));
			}
		}
		// 分页参数
		Pageable pageable = PageRequest.of(MapUtils.getInteger(params, "page")-1, MapUtils.getInteger(params, "limit")
				//, Sort.Direction.DESC,"timestamp"
				);
		SearchQuery query = new NativeSearchQueryBuilder().withQuery(builder).withPageable(pageable).build();
		Page<LogDocument> result = logDao.search(query);
		return PageResult.<LogDocument>builder().data(result.getContent()).code(0).count(result.getTotalElements()).build();
	}
}
