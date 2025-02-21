package com.itti.digital.atm.atm_authorization_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivenessController {
    @GetMapping("/public/health")
    public String liveness() {
        return "alive";
    }
}
