package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.MyTestException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登陆管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexConroller {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin-token");
//        return Result.ok(map);

        // 1.获取输入的用户名称和秘密
        // 2.根据用户名称查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = userService.getOne(wrapper);

        // 3.用户信息是否存在
        if (sysUser == null) {
            throw new MyTestException(201, "用户不存在");
        }

        // 4.判断密码、用户状态
        String password = sysUser.getPassword();
        if (!password.equals(MD5.encrypt(loginVo.getPassword()))) {
            throw new MyTestException(201, "用户密码错误");
        }

        if (sysUser.getStatus() == 0) {
            throw new MyTestException(201, "用户被禁用，联系管理员");
        }

        // 5.使用jwt生产token并返回
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        // 1.从请求头获取token
        String token = request.getHeader("token");

        // 2.从token字符串中获取用户名称/id
        Long userId = JwtHelper.getUserId(token);

        // 3.根据用户id查询数据库，获取用户信息
        SysUser sysUser = userService.getById(userId);

        // 4.返回用户可以操作的菜单、按钮(动态构建路由结构并显示）
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("buttons", permsList);
        map.put("routers", routerList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
