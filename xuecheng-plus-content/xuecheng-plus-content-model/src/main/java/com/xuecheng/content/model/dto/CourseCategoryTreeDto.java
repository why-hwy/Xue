package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable {

    List<CourseCategoryTreeDto> childrenTreeNodes;

}