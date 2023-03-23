package com.atguigu.auth;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@SuppressWarnings(value = "all")
public class TestMpDemo2 {
    @Autowired
    private SysRoleService roleService;

    @Test
    public void getAll() {
        List<SysRole> list = roleService.list();
        System.out.println(list);
    }


}
