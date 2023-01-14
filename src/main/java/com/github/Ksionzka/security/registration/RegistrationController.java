package com.github.Ksionzka.security.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Value("${client.url}")
    private String clientUrl;

    @PostMapping()
    public void register(@Valid @RequestBody RegistrationRequest request) {
        registrationService.register(request);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String token, HttpServletResponse response) throws IOException {
        response.sendRedirect(this.clientUrl);
        return registrationService.confirmToken(token);
    }
}
