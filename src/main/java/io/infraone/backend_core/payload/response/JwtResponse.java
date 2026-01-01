package io.infraone.backend_core.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
