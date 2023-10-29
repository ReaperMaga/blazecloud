package sh.maga.blazecloud.backend.auth.resource.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class AuthRegisterRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
