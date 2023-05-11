package com.example.controller;

import com.example.bean.User;

import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
public class LoginController {

    @Resource
    private UserService userService;


    @PostMapping("/login")
    public String login(@RequestParam("u_email") String u_email,@RequestParam("u_password") String u_password, HttpSession session, Model model) throws ParseException {
        if (StringUtils.isEmpty(u_email)||StringUtils.isEmpty(u_password)) {
            model.addAttribute("msg","邮箱或密码不能为空");
            return "login";
        }
        User user = userService.login(u_email,u_password);
        if(user!=null) {
            int num = userService.update(u_email);
            if(num==1) log.info("用户更新成功");
            model.addAttribute("msg","登录成功");
            session.setAttribute("loginUser", user.getU_email());
            return "redirect:/main.html";
        }else{
            model.addAttribute("msg","登录失败");
            return "login";
        }
    }

    @GetMapping("/registration")
    public Object registor(HttpServletRequest request, HttpSession session, Model model){
        request.getSession();
        session.setAttribute("loginUser","注册");
        return "registration";
    }

    @PostMapping("/register")
    public String addUser(@RequestParam(value = "u_email")String u_email, @RequestParam(value = "u_password" )String u_password,HttpSession session, Model model)  {

        String regisName=(String) session.getAttribute("loginUser");

        User user = userService.userExit(u_email);
        log.info(user.getU_email()+"\t"+user.getU_id()+"\t");
        if(user!=null){
            model.addAttribute("msg","用户已被注册");
        }else {
            int flag = userService.userInsert(u_email, u_password);
            if (flag == 1) {
                session.setAttribute("loginUser", u_email);
                model.addAttribute("msg", "注册成功");

            } else {
                model.addAttribute("msg", "注册失败");
            }
        }
    return "login";

    }
}
