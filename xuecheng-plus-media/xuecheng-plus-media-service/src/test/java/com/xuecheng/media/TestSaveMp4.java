package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestSaveMp4 {

    public MinioClient minioClient
            =
            MinioClient.builder()
                    .endpoint("http://192.168.163.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void testChunk() throws IOException {
        File sourceFile = new File("C:\\Users\\www\\Videos\\Desktop\\1.mp4");
        String chunkPath = "C:\\Users\\www\\Videos\\Desktop\\cpoy\\";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        long chunkSize = 1024 * 1024 * 5;
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);

        byte[] b = new byte[1024];
        RandomAccessFile rafRead = new RandomAccessFile(sourceFile, "r");

        for (int i = 0; i < chunkNum; i++) {
            File file = new File(chunkPath + i);
            if (file.exists()) {
                file.delete();
            }
            boolean newFile = file.createNewFile();
            if (newFile) {
                RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
                int len = -1;
                while ((len = rafRead.read(b)) != -1) {
                    rafWrite.write(b, 0, len);
                    if (file.length() >= chunkSize) {
                        break;
                    }
                }
                rafWrite.close();
                System.out.println("完成分块:" + i);
            }
        }
        rafRead.close();
    }

    @Test
    public void TestMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File(
                "C:\\Users\\www\\Videos\\Desktop\\cpoy");
        //原始文件
        File originalFile = new File(
                "C:\\Users\\www\\Videos\\Desktop\\1.mp4");
        //合并文件
        File mergeFile = new File(
                "C:\\Users\\www\\Videos\\Desktop\\ooo.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        mergeFile.createNewFile();
        RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
        rafWrite.seek(0);
        byte[] b = new byte[1024];
        File[] fileArray = chunkFolder.listFiles();

        List<File> fileList = Arrays.asList(fileArray);

        //重写排序方法排序文件
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
            }
        });


        for (File chunkFile : fileList) {
            RandomAccessFile rafRead = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = rafRead.read(b)) != -1) {
                rafWrite.write(b, 0, len);
            }
            rafRead.close();
        }
        rafWrite.close();
        testComplete(originalFile, mergeFile);
    }

    private void testComplete(File originalFile, File mergeFile) {
        try (
                FileInputStream fileInputStream = new FileInputStream(originalFile);
                FileInputStream mergeFileStream = new FileInputStream(mergeFile);

        ) {
            //取出原始文件的md5
            String originalMd5 = DigestUtils.md5Hex(fileInputStream);
            //取出合并文件的md5进行比较
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileStream);
            if (originalMd5.equals(mergeFileMd5)) {
                System.out.println("合并文件成功");
            } else {
                System.out.println("合并文件失败");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadChunk() throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        String chunkFolderPath = "C:\\Users\\www\\Videos\\Desktop\\cpoy\\";
        File chunkFolder = new File(chunkFolderPath);

        File[] files = chunkFolder.listFiles();
        int i = 0;
        for (File file : files) {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs
                    .builder()
                    .bucket("testbucket")
                    .object("chunk/" + i)
                    .filename(file.getAbsolutePath())
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传分块" + i++ + "成功");
        }
    }

    @Test
    public void testMerge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)
                .limit(3)
                .map(i -> ComposeSource.builder()
                        .bucket("testbucket")
                        .object("chunk/".concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());

        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket("testbucket").object("chunk1/merge01.mp4").sources(sources).build();
        minioClient.composeObject(composeObjectArgs);
    }

    @Test
    void fd() throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {

//        GetObjectArgs testbucket = GetObjectArgs.builder()
//                .bucket("video")
//                .object("d/4/d41d8cd98f00b204e9800998ecf8427e/d41d8cd98f00b204e9800998ecf8427e.mp4")
////                .object("test/01/1.mp4")
//                .build();
//        FilterInputStream input = minioClient.getObject(testbucket);
//
//        System.out.println(DigestUtils.md5Hex(input));
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\www\\Videos\\Desktop\\Desktop 2023.04.26 - 11.37.33.22.DVR.mp4"));
        System.out.println(DigestUtils.md5Hex(fileInputStream));
        fileInputStream.close();
    }

    @Test
    void dd() throws IOException {

    }
}


