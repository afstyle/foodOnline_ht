package com.hh.controller.center;

import com.hh.bo.UserBO;
import com.hh.bo.center.CenterUserBO;
import com.hh.controller.BaseController;
import com.hh.pojo.Users;
import com.hh.resource.FileUpload;
import com.hh.service.center.CenterUserService;
import com.hh.utils.CookieUtils;
import com.hh.utils.DateUtil;
import com.hh.utils.JsonUtils;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口", tags = {"用户信息接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;


    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public Result update(@RequestParam String userId,
                         @RequestBody @Valid CenterUserBO centerUserBO,
                         BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) {

        // 判断BindingResult是否有错误信息
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return Result.errorMap(errors);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), -1, true);

        // TODO 后续要改，增加令牌token，整合进redis，分布式会话

        return Result.ok();
    }

    @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public Result uploadFace(@RequestParam String userId, MultipartFile file,
                             HttpServletRequest request, HttpServletResponse response) {
        // 头像保存地址
//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();

        // 在路径上位每个用户增加userid，用于区分不同的用户
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (file == null) {
            return Result.errorMsg("文件为空");
        }

        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
            FileOutputStream fileOutputStream = null;
            try {
                String[] split = fileName.split("\\.");
                String suffix = split[split.length - 1];

                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    return Result.errorMsg("图片格式不正确");
                }

                // face-{userid}.png
                String newFileName = "face-" + userId + "-" + System.currentTimeMillis() +"." + suffix;

                // 最终地址
                String finalPath = fileSpace + uploadPathPrefix + File.separator + newFileName;

                uploadPathPrefix += ("/" + newFileName);

                File outFile = new File(finalPath);
                if (outFile.getParentFile() != null) {
                    // 创建文件夹
                    outFile.getParentFile().mkdirs();
                }

                // 文件输出保存到目录
                fileOutputStream = new FileOutputStream(outFile);
                InputStream inputStream = file.getInputStream();
                IOUtils.copy(inputStream, fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String imageServerUrl = fileUpload.getImageServerUrl();

        // 加时间戳解决浏览器缓存
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix.replaceAll("\\\\", "/") + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新头像地址到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), -1, true);

        // TODO 后续要改，增加令牌token，整合进redis，分布式会话

        return Result.ok();
    }


    /**
     * 敏感信息设空
     * @param users 原user
     */
    private void setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
    }

    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrorList = result.getFieldErrors();
        for (FieldError error : fieldErrorList) {
            //  发生验证错误所对应的属性
            String errorField = error.getField();
            //  验证错误信息
            String errorMsg = error.getDefaultMessage();
            map.put(errorField, errorMsg);
        }

        return map;
    }


}
