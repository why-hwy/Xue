package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-04-06
 */
public interface TeachplanService extends IService<Teachplan> {
    List<TeachplanDto> quaryTreeNode(Long courseId);

    void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    void move(int dir, Long id);

    void remove(Long id);

    void association(BindTeachplanMediaDto dto);
}
