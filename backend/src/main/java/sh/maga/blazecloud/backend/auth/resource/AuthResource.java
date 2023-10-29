package sh.maga.blazecloud.backend.auth.resource;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sh.maga.blazecloud.backend.auth.AuthService;
import sh.maga.blazecloud.backend.auth.model.RefreshToken;
import sh.maga.blazecloud.backend.auth.repository.RefreshTokenRepository;
import sh.maga.blazecloud.backend.auth.resource.request.AuthLoginBasicRequest;
import sh.maga.blazecloud.backend.auth.resource.request.AuthRefreshRequest;
import sh.maga.blazecloud.backend.auth.resource.request.AuthRegisterRequest;
import sh.maga.blazecloud.backend.auth.resource.response.AuthResponse;
import sh.maga.blazecloud.backend.exception.ServiceException;
import sh.maga.blazecloud.backend.group.GroupService;
import sh.maga.blazecloud.backend.group.model.Group;
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
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    UserService userService;

    @Inject
    GroupService groupService;

    @Inject
    AuthService authService;

    @Inject
    JWTParser jwtParser;

    @Path("/login/basic")
    @POST
    public AuthResponse loginBasic(@Valid AuthLoginBasicRequest request) throws ServiceException {
        Optional<User> optional = repository.findByName(request.getName());
        if(optional.isEmpty()) {
            throw new ServiceException(403, "Wrong credentials");
        }
        User user = optional.get();
        if(!userService.verifyHash(request.getPassword(), user.getPassword())) {
            throw new ServiceException(403, "Wrong credentials");
        }
        String token = authService.generateToken(user);
        Optional<RefreshToken> refreshTokenOptional = authService.fetchRefreshToken(user);
        RefreshToken refreshToken = refreshTokenOptional.orElseGet(() -> authService.generateRefreshToken(user));
        return new AuthResponse(user, token, refreshToken.getId());
    }

    @Path("/register")
    @POST
    public AuthResponse register(@Valid AuthRegisterRequest request) throws ServiceException {
        if(repository.existsByName(request.getName())) {
            throw new ServiceException(403, "User name is taken");
        }
        Group defaultGroup = groupService.getDefaultGroup();
        User user = userService.create(request.getName(), request.getPassword(), defaultGroup.getId());
        repository.persist(user);
        String token = authService.generateToken(user);
        RefreshToken refreshToken = authService.generateRefreshToken(user);
        return new AuthResponse(user, token, refreshToken.getId());
    }

    @Path("/refresh")
    @POST
    public AuthResponse refresh(@Valid AuthRefreshRequest request) throws ServiceException {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByIdOptional(request.getRefreshToken());
        if(refreshTokenOptional.isEmpty()) {
            throw new ServiceException(403, "Invalid refresh token");
        }
        RefreshToken token = refreshTokenOptional.get();
        if(System.currentTimeMillis() > token.getExpireAt()) {
            authService.invalidateRefreshToken(token);
            throw new ServiceException(403, "Refresh token expired. Please login again");
        }
        Optional<User> userOptional = repository.findByIdOptional(token.getUser());
        if(userOptional.isEmpty()) {
            throw new ServiceException(409, "Invalid user");
        }
        User user = userOptional.get();
        String accessToken;
        try {
            jwtParser.parse(request.getAccessToken());
            accessToken = authService.generateToken(user);
        } catch (ParseException e) {
            throw new ServiceException(409, "Invalid old access token");
        }
        return new AuthResponse(user, accessToken, token.getId());
    }
}
