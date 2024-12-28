package _712.final_project_712.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 保存用户头像
     * @param file 头像文件
     * @param userId 用户ID
     * @return 头像访问URL
     */
    String saveAvatar(MultipartFile file, Long userId) throws Exception;
} 