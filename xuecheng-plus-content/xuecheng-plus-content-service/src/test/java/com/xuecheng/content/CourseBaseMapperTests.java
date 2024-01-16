package com.xuecheng.content;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseMapperTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    public void testCourseBaseMapper() {
        CourseBase courseBase = courseBaseMapper.selectById(39L);
        Assertions.assertNotNull(courseBase);

        //自定义了查询对象
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");

        //mybatis查询封装
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名字进行模糊查询
        queryWrapper.like(StringUtils.isNoneEmpty(courseParamsDto.getCourseName())
                , CourseBase::getName, courseParamsDto.getCourseName());
        //查询状态
        queryWrapper.eq(StringUtils.isNoneEmpty(courseBase.getAuditStatus())
                , CourseBase::getAuditStatus, courseBase.getAuditStatus());

        //Page-->>设置参数分页查询
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);


        Page<CourseBase> page = new Page<>(pageParams.getPageNo(),pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        //获取数据列表和数据行数
        List<CourseBase> records = pageResult.getRecords();
        long total = pageResult.getTotal();


        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(
                records, total, pageParams.getPageNo(),pageParams.getPageSize());
        System.out.println(courseBasePageResult);
    }


}
