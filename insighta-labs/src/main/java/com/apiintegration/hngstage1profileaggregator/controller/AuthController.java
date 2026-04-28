package com.apiintegration.hngstage1profileaggregator.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

    @PostMapping("/github/callback")
    public String githubCallback(@RequestParam String code) {

        return "github callback";
    }

}
