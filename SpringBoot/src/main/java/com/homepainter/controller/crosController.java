package com.homepainter.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@RestController
public class crosController {
    /**
     * 下载文件接口
     * @param response
     * @param dir
     * @throws IOException
     */
    @GetMapping("/download/{dir}/{filename}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public void downloadFile(HttpServletResponse response,@PathVariable String dir ,@PathVariable String filename) throws IOException {


        String filepath = "/www/wwwroot/obj/" + dir +"/" + filename;
        File file = new File(filepath);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename.replace(" ", "_"));
        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();
    }

}
