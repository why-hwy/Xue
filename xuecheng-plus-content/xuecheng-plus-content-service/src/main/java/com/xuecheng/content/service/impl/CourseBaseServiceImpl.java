package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.*;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.*;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    /**
     * 分页查询课程基础信息列表
     *
     * @param pageParams      分页参数对象，包括页码和每页记录数
     * @param courseParamsDto 查询条件对象，包括课程名称、审核状态、发布状态等
     * @return 返回课程基础信息列表的分页结果集合
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();

        //根据名称模糊查询,在sql中拼接 course_base.name like '%值%'
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());

        //根据课程审核状态查询 course_base.audit_status = ?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());

        //根据课程状态查询
        queryWrapper.eq(StringUtils.isNoneEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());

        //创建分页参数对象，包括当前页码和每页记录数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        //进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        //获取查询结果列表和总记录数
        List<CourseBase> items = pageResult.getRecords();
        long total = pageResult.getTotal();

        //构建返回的分页结果集合
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items, total, pageParams.getPageNo(), pageParams.getPageSize());

        return courseBasePageResult;
    }

    /**
     * 创建课程基本信息和课程营销信息
     *
     * @param companyId 公司id
     * @param dto       新增课程信息
     * @return CourseBaseInfoDto 课程基本信息dto
     * @throws XueChengPlusException 如果新增课程信息失败或课程名称、分类、等级、教育模式、适应人群、收费规则为空，抛出异常
     */
    @Override
    @Transactional
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) throws XueChengPlusException {

        // 参数校验
        if (StringUtils.isBlank(dto.getName())) {
            XueChengPlusException.cast("课程名称为空");
        }
        if (StringUtils.isBlank(dto.getMt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            XueChengPlusException.cast("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            XueChengPlusException.cast("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            XueChengPlusException.cast("适应人群为空");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            XueChengPlusException.cast("收费规则为空");
        }

        // 在courseBase表中添加课程基本信息
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        // st字段目前无值，需要设置默认值
        courseBase.setSt(courseBase.getMt() + "-1");
        int insertCount = courseBaseMapper.insert(courseBase);
        if (insertCount <= 0) {
            XueChengPlusException.cast("添加课程信息失败");
        }

        // 在courseMarket表中添加课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        courseMarket.setId(courseBase.getId());
        saveCourseMark(courseMarket);

        // 查询并返回新创建的课程信息
        return getCourseBaseInfo(courseBase.getId());
    }

    /**
     * 根据课程ID获取编辑课程页面需要展示的数据
     *
     * @param id 课程ID
     * @return EditCourseDto对象，包含课程基本信息和营销信息
     * @throws XueChengPlusException 课程分类或课程营销信息不存在时抛出异常
     */
    @Override
    public EditCourseDto getEditCourseDto(Long id) {
        //返回的对象
        EditCourseDto editCourseDto = new EditCourseDto();

        //查询课程和营销信息
        CourseBase courseBase = courseBaseMapper.selectById(id);
        CourseMarket courseMarket = courseMarketMapper.selectById(id);

        //如果课程信息或营销信息不存在，抛出异常
        if (courseBase == null) {
            XueChengPlusException.cast("该课程分类不存在");
        }
        if (courseMarket == null) {
            XueChengPlusException.cast("该课程营销信息不存在");
        }

        //拷贝信息到EditCourseDto对象
        BeanUtils.copyProperties(courseBase, editCourseDto);
        BeanUtils.copyProperties(courseMarket, editCourseDto);

        return editCourseDto;
    }

    /**
     * 根据传入的参数更新课程信息
     *
     * @param companyId 公司ID
     * @param dto       待更新的课程信息
     * @return 更新后的课程信息
     * @throws XueChengPlusException 当传入的参数不合法或更新失败时，抛出该异常
     */
    @Override
    @Transactional
    public CourseBaseInfoDto updataEditCourseDto(Long companyId, EditCourseDto dto) throws XueChengPlusException {

        // 参数合法性校验
        if (StringUtils.isBlank(dto.getName())) {
            XueChengPlusException.cast("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            XueChengPlusException.cast("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            XueChengPlusException.cast("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            XueChengPlusException.cast("适应人群为空");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            XueChengPlusException.cast("收费规则为空");
        }

        // 在courseBase表中更新数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(dto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setChangeDate(LocalDateTime.now());

        int i = courseBaseMapper.updateById(courseBase);
        if (i == 0) {
            XueChengPlusException.cast("修改课程信息失败");
        }

        // 在courseMarket表中更新数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(dto, courseMarket);
        saveCourseMark(courseMarket);

        // 返回更新后的课程信息
        return getCourseBaseInfo(dto.getId());
    }

    /**
     * 删除课程及相关信息
     *
     * @param id 课程ID
     */
    @Override
    @Transactional
    public void remove(Long id) {
        // 删除课程基本信息和课程营销信息
        courseBaseMapper.deleteById(id);
        courseMarketMapper.deleteById(id);

        // 删除课程计划和媒资文件关联
        LambdaQueryWrapper<Teachplan> teachplanQuery = new LambdaQueryWrapper<>();
        teachplanQuery.eq(Teachplan::getCourseId, id);
        teachplanMapper.delete(teachplanQuery);

        LambdaQueryWrapper<TeachplanMedia> teachplanMediaQuery = new LambdaQueryWrapper<>();
        teachplanMediaQuery.eq(TeachplanMedia::getCourseId, id);
        teachplanMediaMapper.delete(teachplanMediaQuery);

        // 删除课程教师关联
        LambdaQueryWrapper<CourseTeacher> courseTeacherQuery = new LambdaQueryWrapper<>();
        courseTeacherQuery.eq(CourseTeacher::getCourseId, id);
        courseTeacherMapper.delete(courseTeacherQuery);
    }

    /**
     * 返回到前端的数据类型，base表和mark表
     *
     * @param id 返回的数据id
     * @return 若无此id返回空，若有，将两张表的数据整合返回
     */
    @Override
    public CourseBaseInfoDto getCourseBaseInfo(Long id) {
        //查询Base表是否为空
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (courseBase == null) {
            return null;
        }
        //查询Mark表
        CourseMarket courseMarket = courseMarketMapper.selectById(id);

        //拷贝数据整合到返回数据类型的dto中
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }

        return courseBaseInfoDto;
    }

    /**
     * 将营销数据存入表中，若改营销数据存在则为更新，不存在则为插入数据
     *
     * @param courseMarket 需要写入数据库的营销表
     */
    private void saveCourseMark(CourseMarket courseMarket) {
        String charge = courseMarket.getCharge();
        if (StringUtils.isEmpty(charge)) {
            XueChengPlusException.cast("收费规则为空");
        }
        if (charge.equals("201001")) {
            if (courseMarket.getPrice() == null || courseMarket.getPrice() <= 0)
                XueChengPlusException.cast("课程收费规则错误");
        }
        //判断数据是否为空
        Long id = courseMarket.getId();
        CourseMarket courseMarket1 = courseMarketMapper.selectById(id);
        if (courseMarket1 == null) {
            //插入数据
            courseMarketMapper.insert(courseMarket);
        } else {
            //更新数据
            BeanUtils.copyProperties(courseMarket, courseMarket1);
            courseMarket.setId(id);
            courseMarketMapper.updateById(courseMarket);
        }
    }
}
