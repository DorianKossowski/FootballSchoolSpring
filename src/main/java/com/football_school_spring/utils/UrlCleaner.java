package com.football_school_spring.utils;

import org.springframework.ui.Model;

public class UrlCleaner {
    public static String redirectWithCleaning(Model model, String redirectPath) {
        // to remove userName attribute from URL
        model.asMap().clear();
        return "redirect:" + redirectPath;
    }
}