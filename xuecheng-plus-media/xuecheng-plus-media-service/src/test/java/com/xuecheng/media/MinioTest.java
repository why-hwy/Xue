package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


//@SpringBootTest
public class MinioTest {

    public MinioClient minioClient
            =
            MinioClient.builder()
                    .endpoint("http://192.168.163.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void testUpload() throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        UploadObjectArgs testbucket = UploadObjectArgs.builder()
                .bucket("video")
                .filename("D:\\Project\\xuecheng\\data\\video\\Factorio.rar")
//                .object("1.rar")
                .build();
        System.out.println();

        minioClient.uploadObject(testbucket);
    }

    //
    @Test
    public void testDelete() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        RemoveObjectArgs testbucket = RemoveObjectArgs.builder()
                .bucket("video")
                .object("a/e/ae645cba6c4fc52d24d61e17b65e7e0d/ae645cba6c4fc52d24d61e17b65e7e0d.mp4")
                .build();

        minioClient.removeObject(testbucket);
    }

    @Test
    public void testGet() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        GetObjectArgs testbucket = GetObjectArgs.builder()
                .bucket("video")
                .object("D:\\Project\\xuecheng\\data\\video\\Factorio.rar")
                .build();
        FilterInputStream inputStream = minioClient.getObject(testbucket);

//        System.out.println(DigestUtils.md5Hex(inputStream));

        File file = new File("D:\\Project\\xuecheng\\data\\video\\Factorio.rar");
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 4];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }


//        String sourceMD5 = DigestUtils.md5Hex(inputStream);
        String installMD5 = DigestUtils.md5Hex(new FileInputStream(new File("D:\\Project\\why.mp4")));

//        System.out.println(sourceMD5);
        System.out.println(installMD5);

        // 关闭文件输出流和输入流
        outputStream.close();
        inputStream.close();


//        //临时文件
//        File minioFile = null;
//        FileOutputStream output = null;
//
//        InputStream stream = minioClient.getObject(GetObjectArgs.builder()
//                .bucket("video")
//                .object("a/e/ae645cba6c4fc52d24d61e17b65e7e0d/ae645cba6c4fc52d24d61e17b65e7e0d.mp4")
////                .object("d/4/d41d8cd98f00b204e9800998ecf8427e/d41d8cd98f00b204e9800998ecf8427e.mp4")
//                .build());
//        //创建临时文件
//        minioFile = File.createTempFile("minio", ".merge");
//        output = new FileOutputStream(minioFile);
//        IOUtils.copy(stream, output);
//        FileInputStream iiii = new FileInputStream(minioFile);
//
//        System.out.println(DigestUtils.md5Hex(iiii));
//
//        FileInputStream stream1 = new FileInputStream(new File("C:\\Users\\www\\Videos\\Desktop\\1.mp4"));
//        System.out.println(DigestUtils.md5Hex(stream1));

    }

    @Test
    void donnot() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        // 从 MinIO 服务器获取输入流
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("video")
                .object("d/4/d41d8cd98f00b204e9800998ecf8427e/d41d8cd98f00b204e9800998ecf8427e.mp4")
                .build();

        FilterInputStream inputStream = minioClient.getObject(args);


        // 计算输入流的 MD5 值
        String md5 = DigestUtils.md5Hex(inputStream);


        // 将输入流写入本地文件D:\Project\why.mp4
        File outputFile = new File("D:\\Project\\why.mp4");
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        // 关闭输入流和输出流
        inputStream.close();
        outputStream.close();

        // 从本地文件读取数据并计算 MD5 值
        InputStream input = new FileInputStream(outputFile);
        String md5FromFile = DigestUtils.md5Hex(input);


        InputStream input1 = new FileInputStream("D:\\Project\\2.mp4");
        String md5FromFile1 = DigestUtils.md5Hex(input);


        File outputFile1 = new File("D:\\Project\\22222.mp4");
        FileOutputStream outputStream1 = new FileOutputStream(outputFile1);
        while ((bytesRead = input1.read(buffer)) != -1) {
            outputStream1.write(buffer, 0, bytesRead);
        }

        input1.close();
        outputStream1.close();

        // 比较两个 MD5 值
        System.out.println("minio初始流: " + md5);
        System.out.println("写入文件后流: " + md5FromFile);
        System.out.println("原始的文件流:" + md5FromFile1);
//        System.out.println("MD5s match: " + md5.equals(md5FromFile));
    }
}
