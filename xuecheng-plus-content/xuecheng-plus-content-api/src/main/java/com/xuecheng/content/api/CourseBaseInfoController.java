package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/11 15:44
 */
@Api(value = "课程信息管理接口", tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    CourseBaseService courseBaseService;

    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto courseParamsDto) {

        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryCourseBaseList
                (pageParams, courseParamsDto);

        return courseBasePageResult;
    }

    @ApiOperation("新增课程接口")
    @PostMapping("/course")
    public CourseBaseInfoDto courseBaseInfoDto(@RequestBody @Validated AddCourseDto addCourseDto) {
        Long companyId = 99999999L;
        return courseBaseService.createCourseBase(companyId, addCourseDto);
    }

    @ApiOperation("课程获取接口（通过id）")
    @GetMapping("/course/{id}")
    public EditCourseDto getEditCourseDto(@PathVariable Long id) {
        return courseBaseService.getEditCourseDto(id);
    }

    @ApiOperation("新增课程接口")
    @PutMapping("/course")
    public CourseBaseInfoDto updataCourseBase(@RequestBody @Validated EditCourseDto editCourseDto) {
        Long companyId = 99999999L;
        return courseBaseService.updataEditCourseDto(companyId, editCourseDto);
    }

    @ApiOperation("删除课程接口")
    @DeleteMapping("/course/{id}")
    public void remove(@PathVariable Long id) {
        courseBaseService.remove(id);
    }

}
