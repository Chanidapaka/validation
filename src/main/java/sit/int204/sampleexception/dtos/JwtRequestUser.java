package sit.int204.sampleexception.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// week 8 JWT-based Authentication
@Data
public class JwtRequestUser {
    @NotBlank //ห้ามว่าง
    private String username;

    @Size(min = 8)
    @NotBlank //ห้ามว่าง
    private String password;
}
