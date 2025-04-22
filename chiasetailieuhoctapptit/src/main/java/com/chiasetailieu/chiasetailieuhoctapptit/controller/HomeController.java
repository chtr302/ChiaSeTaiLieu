package com.chiasetailieu.chiasetailieuhoctapptit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/documents")
    public String documents() {
        return "documents";
    }
    @GetMapping("/signin")
    public String signin() {
        return "signin";
    }
    @GetMapping("/test")
    public String test() {
        return "index";
    }
}