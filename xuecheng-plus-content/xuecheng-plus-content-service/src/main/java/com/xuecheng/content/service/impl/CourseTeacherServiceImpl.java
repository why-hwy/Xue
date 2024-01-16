package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    /**
     * 将教师信息写入数据库中，若该数据存在则为更新
     *
     * @param courseTeacher 所需要写入数据的教师信息
     * @return 返回写入数据库的教师信息
     */
    @Override
    public CourseTeacher addAndUpdate(CourseTeacher courseTeacher) {

        if (courseTeacher.getId() == null) {
            courseTeacher.setCreateDate(LocalDateTime.now());
            courseTeacherMapper.insert(courseTeacher);
        } else {
            courseTeacherMapper.updateById(courseTeacher);
        }
        return courseTeacher;
    }

    /**
     * 删除教师信息
     *
     * @param courseId 教师所属的课程id
     * @param id       教师的id
     */
    public void remove(Long courseId, Long id) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        queryWrapper.eq(CourseTeacher::getId, id);

        courseTeacherMapper.delete(queryWrapper);

    }

}
