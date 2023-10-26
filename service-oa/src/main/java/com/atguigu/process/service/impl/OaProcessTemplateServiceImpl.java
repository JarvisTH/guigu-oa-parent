package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTemplateMapper;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author Jarvis
 * @since 2023-09-10
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService processTypeService;

    @Autowired
    private OaProcessService processService;

    @Override
    public void publish(Long id) {
        // 修改发布状态
        ProcessTemplate template = baseMapper.selectById(id);
        template.setStatus(1);
        baseMapper.updateById(template);

        // 流程定义部署
        String path = template.getProcessDefinitionPath();
        if (StringUtils.isEmpty(path)) {
            processService.deployByZip(path);
        }
    }

    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParm) {
        // 1.分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParm, null);

        // 2.分页数据中获取列表list集合
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();

        // 3.遍历list集合获取审批类型id
        // 4.根据id查询对应名称
        for (ProcessTemplate template: processTemplateList) {
            Long processTypeId = template.getProcessTypeId();
            LambdaQueryWrapper<ProcessType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId, processTypeId);
            ProcessType processType = processTypeService.getOne(wrapper);
            if (processType == null) {
                continue;
            }
            // 5.封装返回
            template.setProcessTypeName(processType.getName());
        }

        return processTemplatePage;
    }
}
