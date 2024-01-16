package com.xuecheng.content.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "教师信息接口", tags = "教师信息接口")
@RestController
public class CourseTeacherController {

    @Autowired
    CourseTeacherService courseTeacherService;

    @ApiOperation("查询教师课程id接口")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> getCourseId(@PathVariable Long courseId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherService.list(queryWrapper);
    }

    @ApiOperation("添加教师信息接口")
    @PostMapping("/courseTeacher")
    public CourseTeacher save(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.addAndUpdate(courseTeacher);
    }

    @ApiOperation("修改教师信息接口")
    @PutMapping("/courseTeacher")
    public CourseTeacher update(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.addAndUpdate(courseTeacher);
    }

    @ApiOperation("删除教师信息接口")
    @DeleteMapping("/courseTeacher/course/{courseId}/{id}")
    public void remove(@PathVariable Long courseId, @PathVariable Long id) {
        courseTeacherService.remove(courseId, id);
    }

}
