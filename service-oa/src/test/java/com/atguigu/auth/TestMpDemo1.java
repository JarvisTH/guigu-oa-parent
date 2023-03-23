package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@SuppressWarnings(value = "all")
public class TestMpDemo1 {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void getAll() {
        List<SysRole> roleList = sysRoleMapper.selectList(null);
        System.out.println(roleList);
    }

    @Test
    public void addRecord() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");

        System.out.println(sysRoleMapper.insert(sysRole));
        System.out.println(sysRole.toString() + " " + sysRole.getId());
    }

    @Test
    public void modifyRecord() {
        SysRole sysRole = sysRoleMapper.selectById(11L);
        sysRole.setRoleName("modify");
        int update = sysRoleMapper.updateById(sysRole);
        System.out.println(update);
    }

    @Test
    public void deleteRecord() {
        System.out.println(sysRoleMapper.deleteById(10L));
    }

    @Test
    public void batchDelete() {
        List<SysRole> roleList = sysRoleMapper.selectList(null);
        List<Long> list = roleList.stream().map(sysRole -> {
            return sysRole.getId();
        }).collect(Collectors.toList());
        sysRoleMapper.deleteBatchIds(list);
    }

    @Test
    public void selectCondition1() {
        // 1. 创建querryWrapper对象，调用方法封装条件
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", "角色管理员");

        // 2. 调用mp方法实现查询
        System.out.println(sysRoleMapper.selectList(wrapper));
    }

    @Test
    public void selectCondition2() {
        // 1. 创建querryWrapper对象，调用方法封装条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName, "角色管理员");

        // 2. 调用mp方法实现查询
        System.out.println(sysRoleMapper.selectList(wrapper));
    }
}
