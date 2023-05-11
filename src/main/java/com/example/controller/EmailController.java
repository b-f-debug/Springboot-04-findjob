package com.example.controller;

import com.example.bean.Draft;
import com.example.bean.FileImage;
import com.example.bean.ImageTest;
import com.example.service.EmailService;
import com.example.service.FileImageService;
import com.example.service.UserService;
import com.example.utils.DateUtil;
import com.example.utils.ImageSteganography;
import com.example.utils.SafeUtil;
import com.example.utils.ScheduleJobUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import com.example.utils.StringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
public class EmailController {

    @Resource
    private UserService userService;
    @Resource
    private EmailService emailService;
    @Resource
    private FileImageService fileImageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/email_write")
    public String mail_compose(){
        return "email/mail_compose";
    }
    @GetMapping("/time_email")
    public String mail_draft(){
        return "email/mail_draft";
    }
    @GetMapping("/check_save")
    public String check_save( HttpSession session,Model model){
        String m_from= (String)session.getAttribute("loginUser");
        Set<String> keys = redisTemplate.keys(m_from+"*");
        Map<String,Draft> map = new HashMap<>();
        for(String key:keys) {
            log.info("保存信息有=========》"+key);
            Draft draft=new Draft();
            draft =(Draft) redisTemplate.opsForValue().get(key);
            map.put(key,draft);
            log.info("放入map的信息有=========》"+map.get(key));

        }
        model.addAttribute("map", map);
        log.info("放入model信息有===========》"+model);
        return "email/check_save";
    }
    @GetMapping("/check_detail_save")
    public String check_detail_save(@RequestParam(name = "id") int id,Model model,HttpSession session){
        String m_from=(String)session.getAttribute("loginUser");
        log.info("保存信息详情===========》"+id);
        Draft draft = (Draft)redisTemplate.opsForValue().get(m_from+id);
        model.addAttribute("detail",draft);
        return "email/detail_save";
    }
    @GetMapping("/delete_detail_save")
    public String delete_detail_save(@RequestParam int id, HttpSession session){
        String m_from = (String)session.getAttribute("loginUser");
        redisTemplate.delete(m_from+id);
        return "redirect:/check_save ";
    }

    @PostMapping("/draft")
    public String draft(@RequestParam("btn-value")String btn, @RequestParam("m_to")String m_to
            , @RequestParam("m_subject")String m_subject
            , @RequestParam("m_content")String m_content
            ,@RequestParam("time")String time
            , HttpSession session
            , Model model) throws ParseException, SchedulerException {
        log.info("按钮值=============》"+btn);
        Integer isexit = userService.ifExit(m_to);
        if(isexit==null) {
            model.addAttribute("error_draft","收件人不存在");
            return "email/mail_draft";
        }
        String m_from = (String)session.getAttribute("loginUser");
        if(btn.equals("save")){
            int min=0;
            int max=65534;
            int id=(int)min+(int)(Math.random()*(max-min));
            log.info("进入到保存功能模块");
            String d_id=String.valueOf(id);
            Draft draft=new Draft(id,m_from,m_to,m_subject,m_content);
            //保存页面
            redisTemplate.opsForValue().set(m_from+d_id,draft);
            model.addAttribute("msg","草稿保存成功");
        }
        else{
            //定时任务
            if(StringUtils.isEmpty(m_subject)){
                model.addAttribute("draft","主题不能为空");
            }
            if(StringUtils.isEmpty(m_content)){
                model.addAttribute("draft","内容不能为空");
            }
            //定时发送任务
            log.info("传入的时间为==========》"+time);
            String m = time.replaceAll("T"," ");
            Date date = DateUtil.convert(time);
            Date date1 = new Date();
            //Date_self date2 = new Date_self(date);
            log.info(("转换后的时间===============》"+date));
            log.info("java中date类型===============》" +date1);
            //log.info("自定义中date类型===============》" +date2);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                    .startAt(date)
                    .build();
            JobDetail jobDetail = (JobDetail) JobBuilder.newJob(ScheduleJobUtil.class)
                    .withIdentity("job1","group1")
                    .usingJobData("m_to",m_to)
                    .usingJobData("m_from",m_from)
                    .usingJobData("m_subject",m_subject)
                    .usingJobData("m_content",m_content)
                    .build();
            Scheduler scheduler= StdSchedulerFactory.getDefaultScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
            model.addAttribute("msg","定时任务设置成功");
        }

        return "main";
    }


    @PostMapping("/send")
    public String sendEmail(@RequestPart("lsbImage")MultipartFile lsbImage
            ,@RequestParam("btn-value")String btn
            , @RequestParam("m_to")String m_to
            , @RequestParam("m_subject")String m_subject
            , @RequestParam("m_content")String m_content
            , HttpSession session
            , Model model) throws IOException, URISyntaxException {
        log.info("按钮值=============》"+btn);

        if(!lsbImage.isEmpty()){
            if(btn.equals("send")) {
                //判断是否发件人存在
                log.info("进入到send功能模块");
                Integer user = userService.ifExit(m_to);
                if (user!=null) {
                    //上传文件部分
                   // String oldName = lsbImage.getOriginalFilename();
                    String extension ="."+ FilenameUtils.getExtension(lsbImage.getOriginalFilename());
                    String newName = new SimpleDateFormat("MMddHHmm").format(new Date())+extension;
                    //+UUID.randomUUID().toString().replace("-","")生成唯一标识符
                    long size = lsbImage.getSize();
                    String type = lsbImage.getContentType();

                    //处理根据日期生成目录
                    String realPath = ResourceUtils.getURL("classpath:").getPath()+"/static/files";
                    String dateDirPath = realPath+"/"+new SimpleDateFormat("MM-dd").format(new Date());
                    File dateDir = new File(dateDirPath);
                    if(!dateDir.exists()){
                        dateDir.mkdirs();
                    }
                    log.info("new文件名===========》"+newName);
                    log.info("目录名===========》"+dateDirPath);
                    //处理文件上传
                    lsbImage.transferTo(new File(dateDir,newName));

                    //发送信息部分
                    String m_from = (String) session.getAttribute("loginUser");
                    log.info("发件人===========》" + m_from);
                    String email = SafeUtil.cleanString(m_content);

                    //信息隐藏
                    File file = new File(dateDir,newName);
                    File writeToImgResult = ImageSteganography.writeToImg(email, "lsb", file.getPath());

                    InputStream inputStream = new FileInputStream(writeToImgResult);
                    ImageTest imageTest = new ImageTest(StringUtil.inputTobyte(inputStream));


//log.info("图片地址=============="+writeToImgResult);
//int i = emailService.sendInfo(writeToImgResult);
//if(i==1)log.info("图片数据库成功");

//                    //获取信息
                   String data = ImageSteganography.readFromImg(inputStream);
                   log.info("解密后的数据==============>"+data);


                    int num = emailService.emailInsert(m_from, m_to, m_subject, email, true);
                    if (num == 1) {
                        //邮件发送成功
                        int id = emailService.getMaxId(m_from);
                        log.info("获取最大id============>"+id);
                        //放入数据库
                        FileImage fileImage=new FileImage();
                        fileImage.setFileName(newName).setPath("/files/dateFormat").setId(id);
                        fileImageService.save(fileImage);
                        model.addAttribute("msg","邮件发送成功");
                        return "main";
                    } else {
                        log.info("===============邮件发送失败");
                        model.addAttribute("error", "邮件发送失败");
                        return "email/mail_compose";
                    }
                } else {
                    log.info("===============收件人地址不存在");
                    model.addAttribute("error", "收件人地址不存在");
                    return "email/mail_compose";
                }
            }

        }else {
            model.addAttribute("error","照片不能为空");
        }

        return "email/mail_compose";
    }

}
//cd /usr/local/bin ; redis-server /etc/redis.conf ; ps -ef |grep redis