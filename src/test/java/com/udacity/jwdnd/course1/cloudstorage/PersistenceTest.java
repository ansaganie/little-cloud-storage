package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTest {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void fileInsertTest() throws IOException {
        User user = new User(null, "Ansagan", "islam", "salt", "ansagan", "adfasfbaf");

        int userId = userMapper.insert(user);

        Path path = Paths.get("/home/ansagan/IdeaProjects/littleCloudStorage/src/test/java/com/udacity/jwdnd/course1/cloudstorage/PersistenceTest.java");
        byte[] bytes = Files.readAllBytes(path);
        File file = new File(null, path.getFileName().toString(), "", (long) bytes.length, userId, bytes,  new Date(System.currentTimeMillis()));

        int fileId = fileMapper.insert(file);

        File file1 = fileMapper.getFileById(fileId);
        System.out.println(file1);
        System.out.println();
        System.out.println();
        System.out.println(file1);
        assertEquals(bytes.length, file1.getFileSize());
        assertEquals(file.getContentType(), file1.getContentType());
        assertEquals(Arrays.toString(file.getFileData()), Arrays.toString(file1.getFileData()));
        assertEquals(file.getUploadTime(), file1.getUploadTime());
        assertEquals(file, file1);
    }
}
