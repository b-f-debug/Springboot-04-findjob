package com.example.controller;

import com.example.bean.Email;
import com.example.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@Slf4j
@Controller
public class CheckController {

    @Resource
    private EmailService emailService;

    //查看发送
    @GetMapping("/check_send")
    public String is_send(HttpSession session, Model model){
        String m_from = (String)session.getAttribute("loginUser");
        Collection<Email> emails = emailService.queryAllBySend(m_from);
        model.addAttribute("emails",emails);
        return "check/send";
    }

    //查看接收
    @GetMapping("/check_receive")
    public String is_receive(HttpSession session,Model model){
        String m_to=(String)session.getAttribute("loginUser");
        Collection<Email> emails = emailService.queryAllByReceive(m_to);
        model.addAttribute("emails",emails);
        return "check/receive";
    }
    @GetMapping("/check_detail_send")
    public String check_detail_send(@RequestParam(name = "m_id")int m_id,Model model){
        Email email = emailService.queryById(m_id);
        model.addAttribute("mail",email);
        return "check/detail_send";
    }

    //接收详情
    @GetMapping("/check_detail_receive")
    public String check_detail_receive(@RequestParam(name = "m_id")int m_id,Model model){
        log.info("进入接收详情请求"+m_id);
        Email email = emailService.queryById_R(m_id);
        log.info("详情===============>"+email.getM_from());
        model.addAttribute("mail",email);
        return "check/detail_receive";
    }
}
