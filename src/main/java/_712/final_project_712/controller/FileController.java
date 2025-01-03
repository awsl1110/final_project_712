package _712.final_project_712.controller;

import _712.final_project_712.model.Avatar;
import _712.final_project_712.model.Result;
import _712.final_project_712.service.FileService;
import com.mybatisflex.core.query.QueryChain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import static _712.final_project_712.model.table.AvatarTableDef.AVATAR;

@Slf4j
@RestController
@RequestMapping("/api/file")
@Tag(name = "文件上传接口", description = "处理文件上传相关的接口")
public class FileController {

    @Value("${headImgPath}")
    private String uploadPath;

    @Autowired
    private FileService fileService;

    @PostMapping("/avatar")
    @Operation(
        summary = "上传头像", 
        description = "上传用户头像，需要Token认证",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    public Result<String> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error(401, "未登录或token无效");
            }
            
            if (file == null || file.isEmpty()) {
                return Result.error(400, "请选择要上传的文件");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
                return Result.error(400, "只支持图片文件上传");
            }
            
            String fileUrl = fileService.saveAvatar(file, userId);
            return Result.success(fileUrl);
        } catch (IllegalArgumentException e) {
            log.warn("文件上传参数错误", e);
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("头像上传失败", e);
            return Result.error(500, "头像上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/avatar")
    @Operation(
        summary = "获取当前用户头像", 
        description = "通过token获取当前登录用户的头像",
        security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    )
    public void getCurrentUserAvatar(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            
            Avatar avatar = QueryChain.of(Avatar.class)
                    .where(AVATAR.USER_ID.eq(userId))
                    .orderBy(AVATAR.CREATE_TIME.desc())
                    .limit(1)
                    .one();
            
            if (avatar == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "您还没有上传过头像");
                return;
            }
            
            // 构建头像文件路径
            String avatarPath = uploadPath + File.separator + userId + File.separator + avatar.getFileName();
            File avatarFile = new File(avatarPath);
            
            if (!avatarFile.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "头像文件不存在");
                return;
            }
            
            // 设置响应头
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            response.setHeader("Cache-Control", "max-age=86400"); // 缓存一天
            
            // 写入图片数据
            Files.copy(avatarFile.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
            
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取头像失败：" + e.getMessage());
            } catch (Exception ignored) {
            }
        }
    }
} 