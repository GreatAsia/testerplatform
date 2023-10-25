package com.okay.testcenter.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.bean.Menu;
import com.okay.testcenter.domain.bean.Role;
import com.okay.testcenter.domain.bean.User;
import com.okay.testcenter.mapper.user.MenuMapper;
import com.okay.testcenter.mapper.user.RoleMapper;
import com.okay.testcenter.service.user.GroupService;
import com.okay.testcenter.service.user.RoleService;
import com.okay.testcenter.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @auth 谢扬扬
 * @date 2020/8/26 10:12
 */
@Api(description = "用户管理")
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    GroupService groupService;

    @Resource
    RoleMapper roleMapper;

    @Resource
    MenuMapper menuMapper;

    @GetMapping("/getUserByPage")
    public String getUserByPage(Model model, User user, Page page) {
        List<User> userList = userService.findUserByPage(user);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("users", userList);
        return "user/userList";
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ResponseBody
    @GetMapping("/getUser")
    public RetResult<Object> getUser(User user) {
        User userRes = userService.findUserSingle(user);
        return RetResponse.makeOKRsp(userRes);
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ResponseBody
    @PostMapping("/delUser")
    public RetResult<Object> deleteUser(User user, HttpSession session) {
        user.setIsDelete("1"); //设置为已删除
        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }

        user.setUpdateAt(new Date());
        user.setUpdateBy(userName);
        Boolean isdel = userService.updateUser(user);
        if (isdel) {
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("删除失败");
        }
    }

    @ApiOperation(value = "获取角色", notes = "获取角色")
    @ResponseBody
    @GetMapping("/getRole")
    public RetResult<Object> getRole(Role role) {
        List<Role> roles = roleService.getAllRole(role);
        return RetResponse.makeOKRsp(roles);
    }

    @ApiOperation(value = "更新用户角色", notes = "更新用户角色")
    @ResponseBody
    @PostMapping("/updateUserRole")
    public RetResult<Object> updateUserRole(User user, HttpSession session) {

        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }

        boolean success = false;
        if (user.getId() == 0) {
            user.setCreateBy(userName);
            success = userService.insertUser(user);
        } else {
            user.setUpdateAt(new Date());
            user.setUpdateBy(userName);
            success = userService.updateUser(user);
        }
        if (success) {
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("失败");
        }
    }


    //**role相关****


    @GetMapping("/getRoleByPage")
    public String getRoleByPage(Model model, Role role, Page page) {
        List<Role> roles = roleService.getAllRole(role);
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("roles", roles);
        return "user/roleList";
    }

    @ApiOperation(value = "获取一个角色", notes = "获取一个角色")
    @ResponseBody
    @GetMapping("/getRoleSingle")
    public RetResult<Object> getRoleSingle(Role role) {
        Role roleRes = roleService.getAllRoleSingle(role);
        return RetResponse.makeOKRsp(roleRes);
    }

    @ApiOperation(value = "更新一个角色", notes = "更新一个角色")
    @ResponseBody
    @PostMapping("/updateRole")
    public RetResult<Object> updateRole(Role role, HttpSession session) {


        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }

        int success = 0;
        if (role.getId() == null) {
            role.setCreateBy(userName);
            success = roleMapper.insertSelective(role);
        } else {
            role.setUpdateAt(new Date());
            role.setUpdateBy(userName);
            success = roleMapper.updateByPrimaryKeySelective(role);
        }
        if (success > 0) {
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("失败");
        }
    }

    @ApiOperation(value = "获取所有按钮", notes = "获取所有按钮")
    @ResponseBody
    @GetMapping("/getAllMenus")
    public RetResult<Object> getAllMenus() {
        List<Menu> menus = menuMapper.selectByMenu(new Menu());
        return RetResponse.makeOKRsp(menus);
    }

    @ApiOperation(value = "获取一个按钮通过Menu", notes = "获取一个按钮通过Menu")
    @ResponseBody
    @GetMapping("/getSingleMenuByMenu")
    public RetResult<Object> getAllMenuByMenu(Menu menu) {
        Menu menures = menuMapper.selectByMenuSingle(menu);
        return RetResponse.makeOKRsp(menures);
    }

    @ApiOperation(value = "获取所有按钮通过RoleId", notes = "获取所有按钮通过RoleId")
    @ResponseBody
    @GetMapping("/getAllMenusByRoleId")
    public RetResult<Object> getAllMenusByRoleId(int id) {
        List<Menu> menus = menuMapper.selectByMenuByRole(id);
        return RetResponse.makeOKRsp(menus);
    }


    @GetMapping("/getMenuByPage")
    public String getMenuByPage(Model model, Menu menu, Page page) {
        List<Menu> menus = menuMapper.selectByMenu(menu);
        PageInfo<Menu> pageInfo = new PageInfo<>(menus);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("menus", menus);
        return "user/menuList";
    }

    @ApiOperation(value = "更新或插入按钮", notes = "更新或插入按钮")
    @ResponseBody
    @PostMapping("/updateOrInsertMenu")
    public RetResult<Object> updateOrInsertMenu(Menu menu, HttpSession HttpSession) {
        int success = 0;

        String userName = null;
        if (SecurityUtils.getSubject().getPrincipal() != null && !SecurityUtils.getSubject().getPrincipal().equals("")) {
            String userMsg = SecurityUtils.getSubject().getPrincipal().toString();
            userName = userMsg.substring(0, userMsg.indexOf("-"));
        }
        if (userName == null) {
            return RetResponse.makeErrRsp("请检查当前用户信息");
        }

        if (menu.getId() != null) {
            menu.setUpdateAt(new Date());
            menu.setUpdateBy(userName);
            success = menuMapper.updateByPrimaryKeySelective(menu);
        } else {
            menu.setCreateBy(userName);
            success = menuMapper.insertSelective(menu);
        }
        if (success > 0) {
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("失败");
        }
    }

    @ApiOperation(value = "更新或插入组", notes = "更新或插入组")
    @ResponseBody
    @PostMapping("/updateOrInsertGroup")
    public RetResult<Object> getAllMenusByRoleId(@RequestBody JSONObject json, HttpSession httpSession) {
        int success = groupService.insertOrUpdateGroupArray(json, httpSession);
        if (success > 0) {
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("失败");
        }
    }


}
