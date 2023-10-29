package sh.maga.blazecloud.backend.auth.resource.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sh.maga.blazecloud.backend.user.model.User;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private User user;

    private String accessToken;
    private UUID refreshToken;
}
