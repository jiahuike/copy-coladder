package com.example.coladder.controller;

import com.example.coladder.pojo.Result;
import com.example.coladder.pojo.User;
import com.example.coladder.service.UserService;
import com.example.coladder.untils.JwtUntil;
import com.example.coladder.untils.ThreadLocalUntil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/findById")
    public User findById(Integer id){
        return userService.findById(id);
    }

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        User user = userService.findByUsername(username);
        System.out.println(username);
        if (user == null){
            userService.register(username,password);
            return Result.success();
        }else {
            return Result.error("用户名占用");
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$")String password){
        User user = userService.findByUsername(username);
        if (user == null){
            return Result.error("用户名或密码错误");
        }

        if (user.getPassword().equals(password)){
            Map<String,Object> map = new HashMap<>();
            map.put("id",user.getId());
            map.put("username",user.getUsername());
            String token = JwtUntil.genToken(map);

            //token放进redis中
            stringRedisTemplate.opsForValue().set(token,token,1, TimeUnit.DAYS);
            return Result.success("登录成功",token);
        }else {
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/logout")
    public Result logout(@Pattern(regexp = "^\\S{5,16}$")String username,@RequestHeader("Authorization") String token){
        ThreadLocalUntil.remove();
        stringRedisTemplate.delete(token);
        return Result.success("登出成功",username);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        Map<String,Object> map = ThreadLocalUntil.get();
        String username = (String) map.get("username");

        User user = userService.findByUsername(username);
        return Result.success("查询成功",user);
    }

    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user){
        userService.updateUserInfo(user);
        return Result.success("更新成功","操作成功");
    }

    @PatchMapping("updateAvatar")
    public Result updateAvatar(@RequestParam String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePassword(@RequestBody Map<String,String> params, @RequestHeader("Authorization") String token){
        //参数校验
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        String confirmPassword = params.get("confirmPassword");

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
            return Result.error("缺少参数");
        }

        Map<String,Object> map = ThreadLocalUntil.get();
        String username = (String) map.get("username");
        User user = userService.findByUsername(username);
        if (!user.getPassword().equals(oldPassword)){
            return Result.error("原密码错误");
        }
        if (!newPassword.equals(confirmPassword)){
            return Result.error("两次密码不一致");
        }

        userService.updatePassword(newPassword);
        //删除redis中的对应token
        stringRedisTemplate.opsForValue().getOperations().delete(token);
        return Result.success();
    }
}
