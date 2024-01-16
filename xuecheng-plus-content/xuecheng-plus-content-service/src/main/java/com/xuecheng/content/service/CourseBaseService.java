package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-04-06
 */
public interface CourseBaseService extends IService<CourseBase> {

    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto);

    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    EditCourseDto getEditCourseDto(Long id);

    CourseBaseInfoDto updataEditCourseDto(Long companyId, EditCourseDto editCourseDto);

    void remove(Long id);

    CourseBaseInfoDto getCourseBaseInfo(Long id);
}
