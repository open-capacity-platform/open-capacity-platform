package com.open.capacity.oss.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.oss.dao.FileDao;
import com.open.capacity.oss.model.FileInfo;
import com.open.capacity.oss.model.FileType;
import com.open.capacity.oss.service.FileService;
import com.open.capacity.oss.utils.FileUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * AbstractFileService 抽取类
 * 根据filetype 实例化具体oss对象
*/
@Slf4j
public abstract class AbstractFileService implements FileService {

	protected abstract FileDao getFileDao();

	@Override
	public FileInfo upload(MultipartFile file  ) throws Exception {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		FileInfo oldFileInfo = getFileDao().getById(fileInfo.getId());
		if (oldFileInfo != null) {
			return oldFileInfo;
		}

		if (!fileInfo.getName().contains(".")) {
			throw new IllegalArgumentException("缺少后缀名");
		}

		uploadFile(file, fileInfo);

		fileInfo.setSource(fileType().name());// 设置文件来源
		getFileDao().save(fileInfo);// 将文件信息保存到数据库

		log.info("上传文件：{}", fileInfo);

		return fileInfo;
	}

	/**
	 * 文件来源
	 * 
	 * @return
	 */
	protected abstract FileType fileType();

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param fileInfo
	 */
	protected abstract void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception;

	@Override
	public void delete(FileInfo fileInfo) {
		deleteFile(fileInfo);
		getFileDao().delete(fileInfo.getId());
		log.info("删除文件：{}", fileInfo);
	}

	/**
	 * 删除文件资源
	 * 
	 * @param fileInfo
	 * @return
	 */
	protected abstract boolean deleteFile(FileInfo fileInfo);
	
	@Override
	public FileInfo getById(String id){
		return getFileDao().getById(id);
	}
	
	public PageResult<FileInfo> findList(Map<String, Object> params){
		//设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);

        List<FileInfo> list = getFileDao().findList(params);
        PageInfo<FileInfo> pageInfo = new PageInfo<>(list);
		return PageResult.<FileInfo>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
	}
}
