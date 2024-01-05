package com.oddfar.campus.service.controller.tourist;

import com.oddfar.campus.model.campus.FileInfo;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.service.FileInfoService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhiyuan
 */
@RestController
@Api(tags = "图片或视频的路由")
public class FileController {


    private String addressBefore;

    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private SysUserService userService;

    @ApiOperation(value = "查看图片")
    @GetMapping(value = "/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPicture(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = StringUtils.substringAfter(request.getRequestURI(), "images/");
        FileInfo fileInfo = fileInfoService.getById(fileId);

        File file = new File(fileInfo.getPath());
        if (!file.exists()) {
            response.setStatus(404);
            return null;
        }

        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return bytes;
    }

    @ApiOperation(value = "查看视频")
    @GetMapping(value = "/videos/**")
    public void getVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = StringUtils.substringAfter(request.getRequestURI(), "videos/");
        FileInfo fileInfo = fileInfoService.getById(fileId);
        File file = new File(fileInfo.getPath());

        if (!file.exists()) {
            response.setStatus(404);
            return;
        }

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName().replace(" ", "_"));
        InputStream iStream = new FileInputStream(file);
        IOUtils.copy(iStream, response.getOutputStream());
        response.flushBuffer();

    }

    @ApiOperation(value = "以文件流方式返回")
    @GetMapping("/filePreview/**")
    public void filePreview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = StringUtils.substringAfter(request.getRequestURI(), "filePreview/");
        FileInfo fileInfo = fileInfoService.getById(fileId);
        if (fileInfo.getState() != 0) {
            //文件状态为0的只可管理员查看，用户可不看
            LoginUser loginUser = null;
            try {
                 loginUser = SecurityUtils.getLoginUser();
                SysUser user = loginUser.getUser();
                if (user.getUserType() == 0) {
                    //用户查看文件返回404
                    response.setStatus(404);
                    return;
                }
            } catch (Exception e) {
                response.setStatus(404);
                return;
            }

        }

        String fileName = fileInfo.getObjectName();
        try (FileInputStream inputStream = new FileInputStream(fileInfo.getPath() + File.separator + fileInfo.getObjectName());
             OutputStream outputStream = response.getOutputStream()) {
            byte[] data = new byte[1024];
            // 全文件类型（传什么文件返回什么文件流）
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Accept-Ranges", "bytes");
            int read;
            while ((read = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, read);
            }
            // 将缓存区数据进行输出
            outputStream.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

}
