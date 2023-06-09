package com.example.controller;

import com.example.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class TableController {

    @GetMapping("/editable_table")
    public String editable_table(){
        return "table/editable_table";
    }
     @GetMapping("/basic_table")
    public String basic_table(){
        return "table/basic_table";
    }
     @GetMapping("/responsive_table")
    public String responsive_table(){
        return "table/responsive_table";
    }
     @GetMapping("/pricing_table")
    public String pricing_table(){
        return "table/pricing_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model){


        return "table/dynamic_table";
    }


}
