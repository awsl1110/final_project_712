package _712.final_project_712.controller;

import _712.final_project_712.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
@Tag(name = "文件上传接口", description = "处理文件上传相关的接口")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传用户头像，返回访问URL")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, 
                                             @RequestParam("userId") Long userId) {
        try {
            String fileUrl = fileService.saveAvatar(file, userId);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("���像上传失败：" + e.getMessage());
        }
    }
} 