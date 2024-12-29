package _712.final_project_712.service.impl;

import _712.final_project_712.mapper.AvatarMapper;
import _712.final_project_712.model.Avatar;
import _712.final_project_712.service.FileService;
import com.mybatisflex.core.query.QueryChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${headImgPath}")
    private String uploadPath;

    @Autowired
    private AvatarMapper avatarMapper;

    @Override
    @Transactional
    public String saveAvatar(MultipartFile file, Long userId) throws Exception {
        log.info("开始保存用户头像，userId: {}", userId);

        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
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

        // 查找并删除用户之前的头像
        try {
            Avatar oldAvatar = QueryChain.of(Avatar.class)
                    .where(Avatar::getUserId).eq(userId)
                    .orderBy(Avatar::getCreateTime).desc()
                    .limit(1)
                    .one();

            if (oldAvatar != null) {
                // 删除旧的头像文件
                String oldFilePath = userDir + File.separator + oldAvatar.getFileName();
                Files.deleteIfExists(Paths.get(oldFilePath));
                
                // 删除数据库记录
                avatarMapper.deleteById(oldAvatar.getId());
            }
        } catch (Exception e) {
            log.warn("删除旧头像失败", e);
        }

        // 保存新文件
        Path path = Paths.get(userDir + File.separator + newFileName);
        Files.write(path, file.getBytes());

        // 保存头像信息到数据库
        try {
            Avatar avatar = new Avatar();
            avatar.setUserId(userId);
            avatar.setFileName(newFileName);
            avatar.setCreateTime(LocalDateTime.now());
            
            log.info("准备保存头像信息到数据库: {}", avatar);
            int result = avatarMapper.insert(avatar);
            log.info("头像信息保存结果: {}", result);
            
            if (result <= 0) {
                throw new RuntimeException("保存头像信息到数据库失败");
            }
        } catch (Exception e) {
            // 如果数据库操作失败，删除已上传的文件
            try {
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                log.error("删除文件失败", ex);
            }
            log.error("保存头像信息到数据库出错", e);
            throw e;
        }

        String fileUrl = "/file/avatar/" + userId + "/" + newFileName;
        log.info("头像保存成功，访问URL: {}", fileUrl);
        return fileUrl;
    }
} 