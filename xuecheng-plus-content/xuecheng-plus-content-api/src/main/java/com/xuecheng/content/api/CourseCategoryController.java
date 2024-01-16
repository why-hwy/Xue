package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "课程分类信息接口", tags = "课程分类信息接口")
@RestController
public class CourseCategoryController {

    @Autowired
    CourseCategoryService courseCategoryService;

    @ApiOperation("分类信息查询接口")
    @GetMapping("course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryNode() {
        return courseCategoryService.queryTreeNode("1");
    }
}