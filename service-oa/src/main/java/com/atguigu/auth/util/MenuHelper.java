package com.atguigu.auth.util;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        // 1.创建list存储数据
        List<SysMenu> trees = new ArrayList<>();

        // 2.遍历所有菜单数据
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(getChildren(sysMenu, sysMenuList));
            }
        }

        return trees;
    }


    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> list) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu item : list) {
            if (item.getParentId().longValue() == sysMenu.getId().longValue()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<SysMenu>());
                }
                sysMenu.getChildren().add(getChildren(item, list));
            }
        }
        return sysMenu;
    }
}
