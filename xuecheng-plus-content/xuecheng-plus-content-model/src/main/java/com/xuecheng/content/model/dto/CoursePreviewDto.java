package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import lombok.Data;

import java.util.List;

@Data
public class CoursePreviewDto {

    private CourseBaseInfoDto course;

    private List<TeachplanDto> teachplans;


}
