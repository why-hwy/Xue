package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> quaryTreeNode(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }


    /**
     * 保存教师计划，若传入信息中有id值则为更新，否则为插入信息
     *
     * @param dto 教师计划信息
     */
    @Override
    @Transactional
    public void saveTeachplan(SaveTeachplanDto dto) {

        Long id = dto.getId();

        //更新数据
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(dto, teachplan);
            teachplanMapper.updateById(teachplan);
        }
        //添加数据
        else {
            int count = getTeachplanCount(dto.getCourseId(), dto.getParentid());

            Teachplan teachplan = new Teachplan();
            teachplan.setOrderby(count + 1);

            BeanUtils.copyProperties(dto, teachplan);
            teachplanMapper.insert(teachplan);

            System.out.println(teachplan.toString());

            if (teachplan.getGrade() == 1) {
                Teachplan temp = new Teachplan();
                temp.setPname("新小节名称 [点击修改]");
                temp.setParentid(teachplan.getId());
                temp.setGrade(2);
                temp.setCourseId(dto.getCourseId());
                temp.setStatus(1);
                temp.setOrderby(1);
                teachplanMapper.insert(temp);
            }
        }
    }

    /**
     * 根据传入的参数判断课程信息应该上移还是下移。
     *
     * @param dir 判断上下移动的参数，1表示上移，0表示下移
     * @param id  需要移动的计划id
     */
    @Override
    @Transactional
    public void move(int dir, Long id) {
        // dir为1表示上移，为0表示下移
        if (dir == 1) {
            List<Teachplan> list = teachplanMapper.moveup(id);
            if (list.size() == 1) {
                // 如果返回的List只有一个元素，则已经无法再上移，直接返回
                return;
            }
            boolean swap = swap(list);
            if (!swap) {
                XueChengPlusException.cast("交换失败");
            }
            ;
        } else if (dir == 0) {
            List<Teachplan> list = teachplanMapper.movedown(id);
            if (list.size() == 1) {
                // 如果返回的List只有一个元素，则已经无法再下移，直接返回
                return;
            }
            boolean swap = swap(list);
            if (!swap) {
                XueChengPlusException.cast("交换失败");
            }
            ;
        }
    }


    /**
     * 删除指定的课程计划节点，若该节点是根节点，则删除其所有子节点；若该节点是非根节点，
     * 则先将该节点及其子节点向上移动一位，再删除该节点。若该节点是其父节点的第一顺位节点且无其他节点，
     * 则删除该节点的父节点。
     *
     * @param id 待删除节点的ID
     */
    @Override
    @Transactional
    public void remove(Long id) {
        // 获取待删除节点及其所有子节点
        List<Teachplan> nodesToDelete = teachplanMapper.movedown(id);
        int nodesToDeleteSize = nodesToDelete.size();
        Long parentId = nodesToDelete.get(0).getParentid();
        List<Teachplan> updatedNodes = new ArrayList<>();

        // 对待删除节点及其子节点的顺序进行更新
        if (nodesToDeleteSize > 1) {
            nodesToDelete.stream().forEach(node -> {
                Integer order = node.getOrderby() - 1;
                node.setOrderby(order);
                updatedNodes.add(node);
            });
        }

        // 如果待删除节点不是根节点，并且该节点是其父节点的第一顺位节点且无其他节点，删除该节点的父节点
        if (parentId != 0 && nodesToDelete.get(0).getOrderby() == 1 && nodesToDeleteSize == 1) {
            teachplanMapper.deleteById(parentId);
        }

        // 如果待删除节点是根节点，删除其所有子节点
        if (parentId == 0) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, id);
            teachplanMapper.delete(queryWrapper);
        }

        // 删除待删除节点及其所有子节点
        teachplanMapper.deleteById(nodesToDelete.get(0));

        // 更新待删除节点之后的所有节点的顺序
        if (nodesToDeleteSize > 1) {
            for (Teachplan node : updatedNodes) {
                if (node.getOrderby() == 0) {
                    continue;
                }
                teachplanMapper.updateById(node);
            }
        }
    }

    @Override
    public void association(BindTeachplanMediaDto dto) {
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getMediaId, dto.getMediaId());
        queryWrapper.eq(TeachplanMedia::getTeachplanId, dto.getTeachplanId());
        teachplanMediaMapper.delete(queryWrapper);
        Teachplan teachplan = teachplanMapper.selectById(dto.getTeachplanId());

        TeachplanMedia teachplanMedia = new TeachplanMedia();
        BeanUtils.copyProperties(dto, teachplanMedia);
        teachplanMedia.setMediaFilename(dto.getFileName());
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setCreateDate(LocalDateTime.now());

        teachplanMediaMapper.insert(teachplanMedia);
    }



    /**
     * 获取指定课程的指定父节点下的子节点数量。
     *
     * @param courseId 课程ID
     * @param parentId 父节点ID
     * @return 子节点数量
     */
    private int getTeachplanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }


    /**
     * 交换传入的集合中前两个元素的位置，并将交换后的顺序写入数据库。
     *
     * @param list 需要交换顺序的集合
     * @return 写入数据库的元素个数，即2表示正常交换，0表示写入失败
     */
    private boolean swap(List<Teachplan> list) {
        //定义写入数据的元素
        Teachplan teachplan1 = new Teachplan();
        Teachplan teachplan2 = new Teachplan();

        //交换第一个元素
        teachplan1.setId(list.get(0).getId());
        teachplan1.setOrderby(list.get(1).getOrderby());

        //交换第二个元素
        teachplan2.setId(list.get(1).getId());
        teachplan2.setOrderby(list.get(0).getOrderby());

        int i = teachplanMapper.updateById(teachplan1);
        int j = teachplanMapper.updateById(teachplan2);

        return i + j == 2;
    }
}