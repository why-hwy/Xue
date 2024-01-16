package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {

    @Autowired
    CourseBaseService courseBaseService;

    @Autowired
    TeachplanService teachplanService;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long coursId) {


        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();

        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseInfo(coursId);
        List<TeachplanDto> teachplanDtos = teachplanService.quaryTreeNode(coursId);

        coursePreviewDto.setCourse(courseBaseInfo);
        coursePreviewDto.setTeachplans(teachplanDtos);

        return coursePreviewDto;
    }
}
