package com.example.intellipm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login() { return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }

    @GetMapping("/tasks")
    public String tasks() {
        return "tasks";
    }

    @GetMapping("/teams")
    public String teams() {
        return "teams";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/users")
    public String usersPage() {
        return "users";
    }
}