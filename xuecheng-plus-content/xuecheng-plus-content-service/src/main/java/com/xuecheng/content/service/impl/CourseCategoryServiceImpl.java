package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory>
        implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    /*
        public List<CourseCategoryTreeDto> queryTreeNode(String id) {

            //查询到的全部数据
            List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNode(id);

            //将数据转换成map，方便判断
            Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream()
                    .filter(item -> !id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value,
                            (key1, key2) -> key2));


            //定义返回的类型
            List<CourseCategoryTreeDto> courseCategoryTreeList = new ArrayList<>();

            courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId())).forEach(item -> {
                //节点的父节点为原始根节点则直接加入
                        if (item.getParentid().equals(id)) {
                            courseCategoryTreeList.add(item);
                        }
                        //通过map找到节点父节点不为根节点的父节点
                        CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
                        if (courseCategoryTreeDto != null) {
                            //判断该父节点是否存在子节点
                            if (courseCategoryTreeDto.getChildrenTreeNode() == null) {
                                courseCategoryTreeDto.setChildrenTreeNode(new ArrayList<CourseCategoryTreeDto>());
                            }
                            //将该节点加入子节点中
                            courseCategoryTreeDto.getChildrenTreeNode().add(item);
                        }
                    }
            );
            return courseCategoryTreeList;
        }
    */


    /**
     * 将数据库中读取到的分类信息表按照父子节点的形式返回
     *
     * @param id 需要查询的父节点的id
     * @return 返回课程分类信息的父子节点形式的list集合
     */
    @Override
    public List<CourseCategoryTreeDto> queryTreeNode(String id) {
        //调用mapper递归查询出分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNode(id);

        //找到每个节点的子节点，最终封装成List<CourseCategoryTreeDto>
        //先将list转成map，key就是结点的id，value就是CourseCategoryTreeDto对象，目的就是为了方便从map获取结点,filter(item->!id.equals(item.getId()))把根结点排除
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream().
                filter(item -> !id.equals(item.getId())).
                collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        //定义一个list作为最终返回的list
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();
        //从头遍历 List<CourseCategoryTreeDto> ，一边遍历一边找子节点放在父节点的childrenTreeNodes
        courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId())).forEach(item -> {
            if (item.getParentid().equals(id)) {
                courseCategoryList.add(item);
            }
            //找到节点的父节点
            CourseCategoryTreeDto courseCategoryParent = mapTemp.get(item.getParentid());
            if(courseCategoryParent!=null){
                if(courseCategoryParent.getChildrenTreeNodes()==null){
                    //如果该父节点的ChildrenTreeNodes属性为空要new一个集合，因为要向该集合中放它的子节点
                    courseCategoryParent.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                //到每个节点的子节点放在父节点的childrenTreeNodes属性中
                courseCategoryParent.getChildrenTreeNodes().add(item);
            }

        });

        return courseCategoryList;
    }
}


