package com.xuecheng.content;

import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseTeacherServiceTests {

    @Autowired
    CourseTeacherService courseTeacherService;

    @Test
    void testCourseTeacherService(){
        CourseTeacher courseTeacher = new CourseTeacher();
        courseTeacher.setCourseId(90L);
        courseTeacher.setTeacherName("why");
        courseTeacher.setPosition("p7");
        courseTeacher.setIntroduction("教师简介");

        CourseTeacher add = courseTeacherService.addAndUpdate(courseTeacher);
    }

}
