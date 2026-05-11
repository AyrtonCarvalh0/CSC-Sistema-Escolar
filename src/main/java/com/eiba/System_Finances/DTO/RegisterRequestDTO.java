package com.eiba.System_Finances.DTO;

import com.eiba.System_Finances.Enums.UserRole;

public record RegisterRequestDTO(String login, String senha, UserRole role) {}
