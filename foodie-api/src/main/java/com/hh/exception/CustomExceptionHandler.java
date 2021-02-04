package com.hh.exception;

import com.hh.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author HuangHao
 * @date 2021/1/21 11:52
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    // 上传文件超过500kb时，捕获异常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handlerMaxUploadFile(MaxUploadSizeExceededException exception) {
        return Result.errorMsg("文件上传大小不能超过500Kb，请压缩图片或降低图片质量后再上传。");
    }

}
