package com.hisun.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>类名称：</p>
 * <p>类描述: </p>
 * <p>公司：湖南海数互联信息技术有限公司</p>
 *
 * @创建人：lyk
 * @创建时间：2015年5月5日
 * @创建人联系方式：
 */
public class FileUtil {

    private static final Logger logger = Logger.getLogger(FileUtil.class);

    public static void saveFile(MultipartFile file, String path, String fileName) throws IOException {
        if (file == null || file.isEmpty()) {
            return;
        }
        File fileObj = new File(path);
        if (!fileObj.exists()) {
            boolean bool = fileObj.mkdirs();
            if (!bool) {
                throw new IOException("文件夹创建失败");
            }
        }
        fileObj = new File(path + "/" + fileName);
        if (!fileObj.exists()) {
            boolean bool = fileObj.createNewFile();
            if (!bool) {
                throw new IOException("文件创建失败:" + fileObj.getAbsolutePath());
            }
        }
        FileUtils.copyInputStreamToFile(file.getInputStream(), fileObj);
    }

    /**
     * 获取文件扩展名,没有扩展名返回空
     *
     * @param fileName
     * @return
     * @Description
     */
    public static String getExtend(String fileName) {
        String[] s = fileName.split("\\.");
        if (s.length > 0) {
            return s[s.length - 1];
        } else {
            return "";
        }
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        return readInputStream(inputStream, "utf-8", inputStream.available());
    }

    public static String readInputStream(InputStream inputStream, long sumLeng) throws IOException {
        return readInputStream(inputStream, "utf-8", sumLeng);
    }

    public static String readInputStream(InputStream inputStream, String encoding, long sumLeng) {
        StringBuilder content = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, encoding);
            int i;
            while ((i = isr.read()) != -1) {
                content.append(String.valueOf((char) i));
            }
        } catch (IOException e) {
            logger.error(e, e);
        }
        return content.toString();
    }

    /**
     * 解压zip或rar文件
     *
     * @param filePath 需要解压的文件路径
     * @param outPath  解压文件后的路径
     * @throws FileNotFoundException
     */
    public static void decompression(String filePath, String outPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(filePath);
        ZipInputStream zin = new ZipInputStream(inputStream);
        try {

            ZipEntry entry;
            // 创建文件夹
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(outPath, entry.getName());
                    if (!directory.exists()) {
                        if (!directory.mkdirs()) {
                            throw new Exception("没有权限建立文件夹");
                        }
                    }
                    zin.closeEntry();
                } else {
                    File myFile = new File(outPath, entry.getName());
                    if (!myFile.exists()) {
                        (new File(myFile.getParent())).mkdirs();
                    }
                    FileOutputStream fout = new FileOutputStream(myFile);
                    DataOutputStream dout = new DataOutputStream(fout);
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = zin.read(b)) != -1) {
                        dout.write(b, 0, len);
                    }
                    dout.close();
                    fout.close();
                    inputStream.close();
                    zin.closeEntry();
                }
            }

        } catch (IOException e) {
            logger.error(e, e);
        } finally {
            zin.close();
            inputStream.close();
        }
    }

    public static String getNewFileName(String oldName) {
        if (oldName == null) {
            return null;
        }
        String path = oldName;
        int index = path.lastIndexOf('.');
        StringBuilder newPath = new StringBuilder();
        if (index == -1) {
            newPath.append(path).append("_").append(System.nanoTime());
        } else {
            newPath.append(path.substring(0, index)).append("_").append(System.nanoTime()).append(path.substring(index, path.length()));
        }
        return newPath.toString();

    }
}
