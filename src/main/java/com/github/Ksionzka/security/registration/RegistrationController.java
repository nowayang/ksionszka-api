package com.github.Ksionzka.security.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping()
    public void register(@Valid @RequestBody RegistrationRequest request) {
        registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token) {
        return registrationService.confirmToken(token);
    }
}
