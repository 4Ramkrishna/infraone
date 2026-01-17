package io.infraone.backend_core.payload.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String sellerCode = "TEST_SELLER";
}
