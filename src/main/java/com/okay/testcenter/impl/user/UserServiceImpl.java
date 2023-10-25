package com.okay.testcenter.impl.user;

import com.okay.testcenter.common.convert.UserConvert;
import com.okay.testcenter.domain.bean.Role;
import com.okay.testcenter.domain.bean.User;

import com.okay.testcenter.domain.user.UserResponse;
import com.okay.testcenter.mapper.user.UserMapper;
import com.okay.testcenter.service.user.RoleService;
import com.okay.testcenter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Autowired
    RoleService roleService;


    //只给登录用
    @Override
    public UserResponse findByUserRes(User user) {
        User resUser = userMapper.selectByUserSingle(user);
        UserResponse userResponse = UserConvert.userToUserResponse(resUser);
        int roleId = userResponse.getRoleId();
        Role role = new Role();
        role.setId(roleId);
        role.setIsDelete("0");
        userResponse.setRoleResponse(roleService.getRoleByRoleSingle(role));
        return userResponse;
    }


    @Override
    public List<User> findUserByPage(User user) {
        List<User> users = userMapper.selectUserAndRoleByPage(user);
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        Boolean del = userMapper.updateByPrimaryKeySelective(user)>0?true:false;
        return del;
    }


    @Override
    public boolean insertUser(User user) {
        Boolean del = userMapper.insert(user)>0?true:false;
        return del;
    }

    @Override
    public User findUserSingle(User user) {
        User userRole=userMapper.selectUserRoleByUser(user);
        return userRole;
    }
}
