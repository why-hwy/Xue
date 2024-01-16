package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.po.CourseTeacher;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-04-06
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    CourseTeacher addAndUpdate(CourseTeacher courseTeacher);

    void remove(Long courseId, Long id);

}
