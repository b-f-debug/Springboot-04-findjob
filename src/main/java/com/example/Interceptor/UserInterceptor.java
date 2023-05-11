package com.example.Interceptor;

import com.example.bean.User;
import com.example.service.UserService;
import com.example.utils.SpringUtil;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Base64;


public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        // 用户请求
        if(request.getHeader("jwt") != null) {
            String u_name;
            try {
                u_name = new String(Base64.getDecoder().decode(request.getHeader("jwt").getBytes()));
                // 判断jwt是否正确
                UserService userService = (UserService) SpringUtil.getBean("userService");
                User user = userService.queryByName(u_name);
                if(user != null ) {
                    //user.setU_time(new Date());
                    //userService.update(user);
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("jwt错误，可能是伪造的");
            }
        }
        // 可能是管理员请求
        HttpSession session = request.getSession();
        if(session.getAttribute("loginUser") != null) {
            System.out.println(session.getAttribute("loginUser"));
            flag = true;
        }
        if(!flag) {
            System.out.println("请求被拦截");
            JSONObject res = new JSONObject();
            res.put("status","error");
            res.put("error","您没有登录");
            response.getWriter().write(res.toString());
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
