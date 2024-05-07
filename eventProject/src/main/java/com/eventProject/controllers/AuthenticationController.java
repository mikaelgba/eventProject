package com.eventProject.controllers;

import com.eventProject.dto.records.AuthenticationDTO;
import com.eventProject.dto.records.LoginResponseDTO;
import com.eventProject.dto.records.RegisterDTO;
import com.eventProject.dto.UserDTO;
import com.eventProject.enuns.UserRole;
import com.eventProject.exceptions.MessangeError;
import com.eventProject.models.User;
import com.eventProject.repositories.UserRepository;
import com.eventProject.security.TokenService;
import com.eventProject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("auth")
@Tag(name = "Auth", description = "the Auth Api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Login user (Type role User or  role Admin)")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Register new user (Type role User or role Admin)")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessangeError(HttpStatus.BAD_REQUEST, "Login already exists"));

        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserDTO newUser = new UserDTO(
                    data.name(),
                    data.cpf(),
                    data.login(),
                    encryptedPassword,
                    data.role().equals(UserRole.USER) ?
                            UserRole.USER : (data.role().equals(UserRole.ADMIN) ?
                            UserRole.ADMIN : null),
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
        this.userService.save(newUser);;
        return ResponseEntity.ok().build();
    }
}
