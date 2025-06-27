package edu.ahut.volunteersystembackend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import edu.ahut.volunteersystembackend.config.AliyunOssProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AliyunOssService {

    private final AliyunOssProperties ossProperties;

    public AliyunOssService(AliyunOssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        try (InputStream inputStream = file.getInputStream()) {
            OSS ossClient = new OSSClientBuilder().build(
                    ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret());

            String objectName = ossProperties.getFolder() + fileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossProperties.getBucketName(), objectName, inputStream);
            ossClient.putObject(putObjectRequest);
            ossClient.shutdown();

            return "https://" + ossProperties.getBucketName() + "."
                    + ossProperties.getEndpoint() + "/" + objectName;

        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage(), e);
        }
    }
}