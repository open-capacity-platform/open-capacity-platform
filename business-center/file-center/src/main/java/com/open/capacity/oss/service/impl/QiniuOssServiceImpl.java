package com.open.capacity.oss.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.open.capacity.oss.dao.FileDao;
import com.open.capacity.oss.model.FileInfo;
import com.open.capacity.oss.model.FileType;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 
 * 七牛云oss存储文件
 */
@Service("qiniuOssServiceImpl")
public class QiniuOssServiceImpl extends AbstractFileService implements InitializingBean {

	@Autowired
	private FileDao fileDao;

	@Override
	protected FileDao getFileDao() {
		return fileDao;
	}

	@Override
	protected FileType fileType() {
		return FileType.QINIU;
	}

	@Autowired
	private UploadManager uploadManager;

	@Autowired
	private BucketManager bucketManager;

	@Autowired
	private Auth auth;

	@Value("${qiniu.oss.bucketName:xxxxx}")
	private String bucket;

	@Value("${qiniu.oss.endpoint:xxxxx}")
	private String endpoint;
	
	 
	
	private StringMap putPolicy;

	/**
	 * 获取上传凭证
	 * 
	 * @return
	 */
	private String getUploadToken() {
		return this.auth.uploadToken(bucket, null, 3600, putPolicy);
	}

	@Override
	protected void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
		try {
			// 调用put方法上传
			uploadManager.put(file.getBytes(),  fileInfo.getName() , auth.uploadToken(bucket));
			// 打印返回的信息
		} catch (Exception e) {
		}
		fileInfo.setUrl(endpoint+"/"+ fileInfo.getName());
		fileInfo.setPath(endpoint+"/"+ fileInfo.getName());
		

	}

	@Override
	protected boolean deleteFile(FileInfo fileInfo) {
		try {
			Response response = bucketManager.delete(this.bucket, fileInfo.getPath());
			int retry = 0;
			while (response.needRetry() && retry++ < 3) {
			    response = bucketManager.delete(bucket, fileInfo.getPath());
			}
		} catch (QiniuException e) {
			return false ;
		}
        return true;

	}

 

  

	@Override
	public void afterPropertiesSet() throws Exception {
		this.putPolicy = new StringMap();
		putPolicy.put("returnBody",
				"{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
	}

}
