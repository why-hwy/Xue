package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "课程计划编辑接口", tags = "课程计划编辑接口")
@RestController
public class TeachplanController {

    @Autowired
    TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId", name = "课程Id", required = true
            , dataType = "Long", paramType = "path")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.quaryTreeNode(courseId);
    }

    @ApiOperation("保存课程计划接口")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

    @ApiOperation("课程计划修改位置接口")
    @PostMapping("/teachplan/{move}/{id}")
    public void move(@PathVariable String move, @PathVariable Long id) {
        if (move.equals("movedown")) {
            teachplanService.move(0, id);
        }
        if (move.equals("moveup")) {
            teachplanService.move(1, id);
        }
    }

    @ApiOperation("课程计划删除接口")
    @DeleteMapping("/teachplan/{id}")
    public void remove(@PathVariable Long id){
        teachplanService.remove(id);
    }

    @ApiOperation(value = "课程计划喝媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto dto){

        teachplanService.association(dto);
    }

}
