package com.xuecheng.content.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "EditCourseDto", description = "修改课程基本信息（获取）")
public class EditCourseDto extends AddCourseDto {

    @ApiModelProperty(value = "课程id", required = true)
    private Long id;
}
