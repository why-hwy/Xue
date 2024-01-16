package com.xuecheng.content;


import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TeachplanServiceTests {

    @Autowired
    TeachplanService teachplanService;

//    @Test
//    void testTeachplanService() {
//        Long courseId = 22L;
//
//        List<TeachplanDto> teachplanDtos = teachplanService.quaryTreeNode(courseId);
//
//        System.out.println(teachplanDtos);
//    }
//
//    @Test
//    void testMove() {
//        teachplanService.move(1, 317L);
//        System.out.println("ok");
//    }
//
//    @Test
//    void testReMove() {
//        teachplanService.remove(320L);
//    }
}
