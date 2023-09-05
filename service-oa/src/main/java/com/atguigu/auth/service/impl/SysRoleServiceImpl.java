package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> exitUserRoleList = sysUserRoleService.list(wrapper);
        List<Long> exitRoleIdList = exitUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        List<SysRole> sysRoles = baseMapper.selectList(null);
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            if (exitRoleIdList.contains(sysRole.getId())) {
                assignRoleList.add(sysRole);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRolesList", sysRoles);
        return roleMap;
    }

    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        // 把用户之前的角色数据删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        // 分配角色
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
