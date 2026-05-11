package com.eiba.System_Finances.DTO;

import com.eiba.System_Finances.Enums.Role;

public record CadastroUsuarioDTO(String login, String senha, Role role) {}
