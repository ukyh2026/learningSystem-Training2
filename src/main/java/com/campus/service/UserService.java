package com.campus.service;

import com.campus.dao.UserDao;
import com.campus.model.Role;
import com.campus.model.Student;
import com.campus.model.Teacher;
import com.campus.model.User;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public User register(String username, String password, Role role){
        // 1. 检查基本参数
        if (username == null || username.trim().isEmpty()) {
            System.out.println("注册失败：用户名不能为空");
            return null;
        }

        if (password == null || password.isEmpty()) {
            System.out.println("注册失败：密码不能为空");
            return null;
        }

        if (role == null) {
            System.out.println("注册失败：用户角色不能为空");
            return null;
        }

        // 2. 检查用户名是否已经存在
        User existingUser =
                userDao.getUserByUsername(username);

        if (existingUser != null) {
            System.out.println("注册失败：用户名已存在");
            return null;
        }

        // 3. 用户名可用，生成新的用户 ID
        int userID = userDao.generateUserId();

        // 4. 创建用户对象
        User user;

        if (role == Role.STUDENT) {
            user = new Student(userID, username, password);
        } else {
            user = new Teacher(userID, username, password);
        }

        // 4. 保存用户
        userDao.addUser(user);
        System.out.println(
                "注册成功，用户ID：" + userID
        );

        return user;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回 User 对象；
     *         登录失败返回 null
     */
    public User login(String username,String password){
        //1、检查基本参数
        if(username == null || username.trim().isEmpty()) {
            System.out.println("登录失败，用户名不能为空");
            return null;
        }
        if(password == null || password.trim().isEmpty()) {
            System.out.println("登录失败，密码不能为空");
            return null;
        }
        //2、根据用户名查找用户
        User user = userDao.getUserByUsername(username);

        if(user == null){  //3、用户不存在
            System.out.println("登录失败：用户不存在");
            return null;
        }

        //4、如果用户存在，继续检查密码，密码不等，登录失败
        if(!user.getPassword().equals(password)){
            System.out.println("登录失败：密码错误");
            return null;
        }

        //登录成功
        System.out.println(
                "登录成功：" + user.getUsername()
        );

        return user;
    }
    public boolean checkUserPermission(User user, Role requiredRole) {
        if (user == null) {
            System.out.println("【权限拦截】校验失败：当前处于未登录状态！");
            return false;
        }

        if (user.getRole() != requiredRole) {
            System.out.println("【权限拦截】操作拒绝：用户 [" + user.getUsername() +
                    "] 的角色是 [" + user.getRole().getDescription() +
                    "]，无权执行此操作（需要 [" + requiredRole.getDescription() + "] 权限）！");
            return false;
        }

        return true;
    }


}
