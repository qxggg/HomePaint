package com.homepainter.ForTest;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import static com.homepainter.ForTest.LuRuGoods.Get_Zi_Dir;

@SpringBootTest
public class GaiName {

    @Test
    public void start() throws IOException {
        String dir = "G:\\家具类别划分\\吊灯";
        String []res = Get_Zi_Dir(dir
        );
        String target = "G:\\test\\吊灯\\";
        for(int i=0;i<res.length;i++){
            System.out.println(res[i]);
            copyFolder(dir+"\\"+res[i], target + i);
            System.out.println(i);
        }
    }

    public static void copyFolder(String sourceFolder, String destinationFolder) throws IOException {



        try {
            // 创建源文件夹路径对象
            Path sourceFolderPath = Paths.get(sourceFolder);
            // 创建目标文件夹路径对象
            Path destinationFolderPath = Paths.get(destinationFolder);

            // 移动文件夹及其内容到目标文件夹
            Files.walk(sourceFolderPath)
                    .forEach(sourcePath -> {
                        Path destinationPath = destinationFolderPath.resolve(sourceFolderPath.relativize(sourcePath));
                        try {
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            // 删除源文件夹
            Files.delete(sourceFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public  void chongmingming() {
        // 获取用户输入的文件夹路径
        String folderPath = "F:\\地板贴图A\\H039-地板贴图02\\地板";

        // 创建File对象
        File folder = new File(folderPath);

        // 判断文件夹是否存在
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("文件夹不存在！");
            return;
        }

        // 获取文件夹下的所有文件
        File[] files = folder.listFiles();

        // 给文件重命名
        int count = 1;
        for (File file : files) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".")); // 获取文件扩展名
            String newFileName = count + fileExtension;
            File newFile = new File(folderPath + File.separator + newFileName);
            if (file.renameTo(newFile)) {
                System.out.println("文件 " + fileName + " 重命名为 " + newFileName + " 成功！");
            } else {
                System.out.println("文件 " + fileName + " 重命名失败！");
            }
            count++;
        }
    }

}
