package com.softserve.edu.sporthubujp.controller;

import com.softserve.edu.sporthubujp.dto.ForgotPasswordDTO;
import com.softserve.edu.sporthubujp.service.UserService;
import com.softserve.edu.sporthubujp.service.impl.ForgotPasswordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/forgot/password")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {
    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(UserService userService, ForgotPasswordService forgotPasswordService) {
        this.userService = userService;
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/newpassword")
    public ResponseEntity<Void> updatePassword(@RequestBody ForgotPasswordDTO request)
    {
        log.info(String.format("Controller: set new user password with token %s", request.getToken()));
        return ResponseEntity.status(HttpStatus.OK).body(
                forgotPasswordService.setNewPassword(request.getPassword(), request.getToken()));
    }

    @PostMapping()
    public ResponseEntity<String> resetPassword(@RequestParam String email)
            throws IOException, SendFailedException {
        log.info(String.format("Controller: confirm user email %s", email));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(forgotPasswordService.resetPassword(email));
    }

}
