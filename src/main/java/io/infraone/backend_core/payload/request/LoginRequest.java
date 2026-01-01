package io.infraone.backend_core.payload.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
