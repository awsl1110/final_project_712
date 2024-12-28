package _712.final_project_712.service.impl;

import _712.final_project_712.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${headImgPath}")
    private String uploadPath;

    @Override
    public String saveAvatar(MultipartFile file, Long userId) throws Exception {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只能上传图片文件");
        }

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";

        // 生成新的文件名
        String newFileName = UUID.randomUUID() + extension;

        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 创建用户专属目录
        String userDir = uploadPath + File.separator + userId;
        File userUploadDir = new File(userDir);
        if (!userUploadDir.exists()) {
            userUploadDir.mkdirs();
        }

        // 保存文件
        Path path = Paths.get(userDir + File.separator + newFileName);
        Files.write(path, file.getBytes());

        // 返回文件访问路径
        return "/api/file/avatar/" + userId + "/" + newFileName;
    }
} 