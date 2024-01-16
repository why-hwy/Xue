package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TeachplanMapperTests {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Test
    void testTeachplanMapper() {
        Long courseId = 22L;
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);

        System.out.println(teachplanDtos);
    }

    @Test
    void testMove(){
        List<Teachplan> moveup = teachplanMapper.moveup(317L);
        System.out.println(moveup);
    }

}
