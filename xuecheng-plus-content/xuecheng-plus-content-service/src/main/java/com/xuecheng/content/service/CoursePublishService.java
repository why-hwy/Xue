package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;

/**
 * <p>
 * 课程发布 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-04-06
 */
public interface CoursePublishService extends IService<CoursePublish> {

    CoursePreviewDto getCoursePreviewInfo(Long coursId);
}
