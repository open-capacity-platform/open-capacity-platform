package com.open.capacity.oss.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.log.monitor.PointUtil;
import com.open.capacity.oss.config.OssServiceFactory;
import com.open.capacity.oss.model.FileInfo;
import com.open.capacity.oss.model.FileType;
import com.open.capacity.oss.service.FileService;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
*  文件上传 同步oss db双写 目前仅实现了阿里云,七牛云
*  参考src/main/view/upload.html
*/
@RestController
public class FileController {

	@Autowired
	private OssServiceFactory fileServiceFactory;
	private static Logger log = LoggerFactory.getLogger(FileController.class);
	private ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 文件上传
	 * 根据fileType选择上传方式
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	@PostMapping("/files-anon")
	public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception {
		
		String fileType = FileType.ALIYUN.toString();
		FileService fileService = fileServiceFactory.getFileService(fileType);
		return fileService.upload(file);
	}

	/**
	 * layui富文本文件自定义上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	@PostMapping("/files/layui")
	public Map<String, Object> uploadLayui(@RequestParam("file") MultipartFile file )
			throws Exception {
		
		FileInfo fileInfo = upload(file);

		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		Map<String, Object> data = new HashMap<>();
		data.put("src", fileInfo.getUrl());
		map.put("data", data);

		return map;
	}

	/**
	 * 文件删除
	 * @param id
	 */
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	@PreAuthorize("hasAuthority('file:del')") 
	@DeleteMapping("/files/{id}")
	public Result delete(@PathVariable String id) {

		try{
			FileInfo fileInfo = fileServiceFactory.getFileService(FileType.ALIYUN.toString()).getById(id);
			if (fileInfo != null) {
				FileService fileService = fileServiceFactory.getFileService(fileInfo.getSource());
				fileService.delete(fileInfo);
			}
			return Result.succeed("操作成功");
		}catch (Exception ex){
			return Result.failed("操作失败");
		}

	}
 
	/**
	 * 文件查询
	 * @param params
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PreAuthorize("hasAuthority('file:query')")
	@GetMapping("/files")
	public PageResult<FileInfo> findFiles(@RequestParam Map<String, Object> params) throws JsonProcessingException {
        
		return  fileServiceFactory.getFileService(FileType.ALIYUN.toString()).findList(params);

	}
}
