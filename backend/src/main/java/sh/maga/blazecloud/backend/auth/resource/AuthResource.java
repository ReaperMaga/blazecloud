package sh.maga.blazecloud.backend.auth.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import sh.maga.blazecloud.backend.auth.AuthService;
import sh.maga.blazecloud.backend.auth.resource.request.AuthLoginBasicRequest;
import sh.maga.blazecloud.backend.auth.resource.response.AuthResponse;
import sh.maga.blazecloud.backend.exception.ServiceException;
import sh.maga.blazecloud.backend.user.UserService;
import sh.maga.blazecloud.backend.user.model.User;
import sh.maga.blazecloud.backend.user.repository.UserRepository;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Path("/auth")
public class AuthResource {

    @Inject
    UserRepository repository;

    @Inject
    UserService userService;

    @Inject
    AuthService authService;

    @Path("/login/basic")
    @POST
    public AuthResponse loginBasic(AuthLoginBasicRequest request) throws ServiceException {
        Optional<User> optional = repository.findByName(request.getName());
        if(optional.isEmpty()) {
            throw new ServiceException(403, "Wrong credentials");
        }
        User user = optional.get();
        if(!userService.verifyHash(request.getPassword(), user.getPassword())) {
            throw new ServiceException(403, "Wrong credentials");
        }
        String token = authService.generateToken(user);
        return new AuthResponse(user, token);
    }
}
