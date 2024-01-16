package com.xuecheng.content;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.service.CourseBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseBaseInfoServiceTests {

    @Autowired
    CourseBaseService courseBaseService;

    @Test
    public void testCourseBaseInfoService() {

        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java");
        courseParamsDto.setAuditStatus("202004");

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);

        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryCourseBaseList(pageParams, courseParamsDto);
        System.out.println(courseBasePageResult);


    }

    @Test
    public void testCreateCourseBase() {
        AddCourseDto dto = new AddCourseDto();
        dto.setCharge("201000");
        dto.setDescription("w");
        dto.setGrade("204001");
        dto.setMt("1-1");
        dto.setSt("1-1-1");
        dto.setName("www");
        dto.setOriginalPrice(9f);
        dto.setPhone("188");
        dto.setPic("pic");
        dto.setPrice(999f);
        dto.setQq("qq");
        dto.setTags("tags");
        dto.setTeachmode("200003");
        dto.setUsers("w");
        dto.setValidDays(365);
        dto.setWechat("www");

        CourseBaseInfoDto courseBase = courseBaseService.createCourseBase(748748L, dto);

        System.out.println(courseBase);
    }

    @Test
    public void testGetAddCourseDto() {
        Long id = 18L;
        AddCourseDto addCourseDto = courseBaseService.getEditCourseDto(id);

        System.out.println(addCourseDto);
    }

    @Test
    public void testUpdataEditCourseDto() {
        EditCourseDto editCourseDto = new EditCourseDto();
        editCourseDto.setId(165L);
        editCourseDto.setName("why2561651");
        editCourseDto.setUsers("use,asd.sss,aaa");
        editCourseDto.setTags("tag");
        editCourseDto.setMt("1-1");
        editCourseDto.setSt("1-1-1");
        editCourseDto.setGrade("204003");
        editCourseDto.setTeachmode("200002");
        editCourseDto.setDescription("aaaaaaaaaaa");
        editCourseDto.setPic("");
        editCourseDto.setCharge("201001");
        editCourseDto.setPrice(99F);

        Long companyId = 99999999L;

        CourseBaseInfoDto courseBaseInfoDto = courseBaseService.updataEditCourseDto(companyId, editCourseDto);
        System.out.println(courseBaseInfoDto);
    }
}
