package sh.maga.blazecloud.backend.auth.resource.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class AuthRefreshRequest {

    @NotNull
    private UUID refreshToken;
    @NotBlank
    private String accessToken;
}
