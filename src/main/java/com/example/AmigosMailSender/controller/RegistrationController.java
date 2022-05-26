package com.example.AmigosMailSender.controller;

import com.example.AmigosMailSender.model.RegistrationRequest;
import com.example.AmigosMailSender.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    @PostMapping
    public String register(@RequestBody RegistrationRequest request ){
        return registrationService.register(request);
    }
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
