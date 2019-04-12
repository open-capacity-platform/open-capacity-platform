package com.open.capacity.oss.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.open.capacity.oss.model.FileInfo;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * oss上传存储db
*/
@Mapper
public interface FileDao {

	@Select("select * from file_info t where t.id = #{id}")
	FileInfo getById(String id);

	@Insert("insert into file_info(id, name, isImg, contentType, size, path, url, source, createTime) "
			+ "values(#{id}, #{name}, #{isImg}, #{contentType}, #{size}, #{path}, #{url}, #{source}, #{createTime})")
	int save(FileInfo fileInfo);

	@Delete("delete from file_info where id = #{id}")
	int delete(String id);

	int count(Map<String, Object> params);

	List<FileInfo> findList(Map<String, Object> params);
}
