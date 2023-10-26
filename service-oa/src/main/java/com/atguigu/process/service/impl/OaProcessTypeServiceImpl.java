package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTypeMapper;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author Jarvis
 * @since 2023-09-10
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Override
    public List<ProcessType> findProcessType() {
        // 1.查询所有审批分类
        List<ProcessType> processTypeList = baseMapper.selectList(null);

        // 2.遍历分类，得到每个分类id对应的审批模板列表
        for (ProcessType processType : processTypeList) {
            Long id = processType.getId();
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, id);
            List<ProcessTemplate> list = processTemplateService.list(wrapper);

            // 3.将对应模板封装到对应的审批对象里
            processType.setProcessTemplateList(list);
        }

        return processTypeList;
    }
}
