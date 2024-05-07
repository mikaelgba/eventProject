package com.eventProject.controllers;

import com.eventProject.vo.UserVO;
import com.eventProject.dto.UserDTO;
import com.eventProject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "the User Api")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "Get page users, default 10 per page")
    @GetMapping
    public ResponseEntity<Page<UserVO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(summary = "Get user by id, default 10 per page")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserVO> findById(@PathVariable String id) {
        UserVO userVO = userService.findById(id);
        if (userVO != null) {
            return ResponseEntity.ok(userVO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get page users by cpf or username, default 10 per page")
    @GetMapping("/search/{nameOrCpf}")
    public ResponseEntity<Page<UserVO>> findByNameOrCpf(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String nameOrCpf
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<UserVO> userVO = userService.findByNameOrCpf(nameOrCpf, pageable);
        return ResponseEntity.ok(userVO);
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<UserVO> create(@RequestBody @Validated UserDTO userDTO) {
        UserVO savedUserDTO = userService.save(userDTO);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update new user by id with userDTO")
    @PutMapping("/{id}")
    public ResponseEntity<UserVO> update(@PathVariable String id, @RequestBody @Validated UserDTO userDTO) {
        UserVO updatedUserVO = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedUserVO);
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
