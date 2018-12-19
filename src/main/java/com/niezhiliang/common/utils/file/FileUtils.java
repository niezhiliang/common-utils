package com.niezhiliang.common.utils.file;

import com.niezhiliang.common.utils.random.CodeUtils;
import com.niezhiliang.common.utils.random.UuidUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/27 上午10:40
 */
public class FileUtils {

    /**
     * 获取文件后缀
     * @param file
     * @return
     */
    public static String getFileSuffixName(MultipartFile file) {
        String fileName = getFileName(file);
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 返回文件名带后缀
     * @param file
     * @return
     */
    public static String getFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }


    /**
     * 返回文件名带后缀
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        return file.getName();
    }

    /**
     * 获取文件后缀
     * @param file
     * @return
     */
    public static String getFileSuffixName(File file) {
        String fileName = getFileName(file);
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 传入后缀名 生成不重复文件名(时间戳格式)
     * @param suffix
     * @return
     */
    public static String generateFileNameByTime(String suffix) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(System.currentTimeMillis())
                .append(CodeUtils.getNumCode4());
        if (suffix.indexOf(".") != -1) {
            fileName.append(suffix);
        } else {
            fileName.append(".")
                    .append(suffix);
        }
        return fileName.toString();
    }

    /**
     * 传入后缀名 生成不重复文件名(时间戳格式)
     * @param suffix
     * @return
     */
    public static String generateFileNameByUUID(String suffix) {
        StringBuffer fileName = new StringBuffer();
        fileName.append(UuidUtils.getUUIDNoSlash());
        if (suffix.indexOf(".") != -1) {
            fileName.append(suffix);
        } else {
            fileName.append(".")
                    .append(suffix);
        }
        return fileName.toString();
    }


    /**
     * 删除文件或清空文件夹并删除
     * @param path
     */
    public static void delete(String path) {
        File file = new File(path);
        if(file.isDirectory()){
            for(File f:file.listFiles())
                delete(f.getPath());
        }
        file.delete();
    }

    /**
     * 文件转base64
     * @param path
     * @return
     * @throws IOException
     */
    public static String filePathToBase64(String path) throws IOException {
        String base64 = null;
        Object in = null;

        try {
            File file = new File(path);
            byte[] bytes = loadFile(file);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } finally {
            if (in != null) {
                ((InputStream)in).close();
            }

        }

        return base64;
    }

    /**
     * 文件转base64
     * @param file
     * @return
     * @throws IOException
     */
    public static String FileToBase64(File file) throws IOException {
        String base64 = null;
        Object in = null;

        try {
            byte[] bytes = loadFile(file);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } finally {
            if (in != null) {
                ((InputStream)in).close();
            }

        }

        return base64;
    }

    /**
     * base64转文件
     * @param base64Content
     * @param savepath
     * @throws IOException
     */
    public static void Base64ToFile(String base64Content, String savepath) throws IOException {
        new File(savepath);
        FileOutputStream fos = new FileOutputStream(savepath);

        try {
            fos.write(Base64.getDecoder().decode(base64Content));
        } finally {
            if (fos != null) {
                fos.close();
            }

        }

    }

    /**
     * 文件转byte[]
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > 2147483647L) {
            ;
        }

        byte[] bytes = new byte[(int)length];
        int offset = 0;

        int numRead;
        for(boolean var6 = false; offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0; offset += numRead) {
            ;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        } else {
            is.close();
            return bytes;
        }
    }

    /**
     * byte[]转本地文件
     * @param buf
     * @param path
     * @throws IOException
     */
    private static void byte2File(byte[] buf, String path) throws IOException {

        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        //1.如果父目录不存在，则创建
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //2.写byte数组到文件
        bos.write(buf);
    }

    /**
     * 文件转inputStream
     * @param file
     * @return
     */
    public static InputStream File2ImputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /**
     * MultipartFile转file
     * @param multipartFile
     * @param file
     * @throws IOException
     */
    public static void MultipartFile2File(MultipartFile multipartFile,File file) throws IOException {
        multipartFile.transferTo(file);
    }

    /**
     * inputStream转byte[]
     * @param inputStream
     * @return
     */
    public static byte[] InputStream2Byte(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 文件下载
     * @param response
     * @throws IOException
     */
    public static void downLoadFile(HttpServletResponse response,File file) throws IOException {
        byte[] data = loadFile(file);
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + getFileName(file));
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
    }

    /**
     * 通过url下载文件
     * @param response
     * @param fileUrl
     */
    public static void downLoadFileByUrl(HttpServletResponse response,String fileUrl) throws Exception {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 获取自己数组
        byte[] data = InputStream2Byte(inputStream);
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
        if (inputStream != null) {
            inputStream.close();
        }
    }

}
