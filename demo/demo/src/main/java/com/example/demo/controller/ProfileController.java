package com.example.demo.controller;

import com.example.demo.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String getProfile(Model model) {
        Student sinhVien = new Student(
                "Nguyễn Quốc Khánh","B24DCCN311","D24CQCN03-B","Công nghệ thông tin","D24");
        model.addAttribute("sinhVien", sinhVien);
        return "profile";
    }

}