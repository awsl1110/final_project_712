package _712.final_project_712.advice;

import _712.final_project_712.Exception.BusinessException;
import _712.final_project_712.Exception.FileException;
import _712.final_project_712.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public Result<?> handleFileException(FileException e) {
        log.error("文件处理异常：{}", e.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传超出大小限制：{}", e.getMessage());
        return Result.error(HttpStatus.BAD_REQUEST.value(), "上传文件过大，请确保文件大小在限制范围内");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数验证失败";
        log.error("参数验证异常：{}", message);
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        log.error("参数绑定异常：{}", message);
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统繁忙，请稍后重试");
    }
} 