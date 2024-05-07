package com.eventProject.dto.records;

import com.eventProject.enuns.UserRole;
import java.time.LocalDateTime;

public record RegisterDTO(String name,
                          String cpf,
                          String login,
                          String password,
                          UserRole role,
                          LocalDateTime created,
                          LocalDateTime updated) {
}
