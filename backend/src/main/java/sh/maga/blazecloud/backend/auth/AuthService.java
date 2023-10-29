package sh.maga.blazecloud.backend.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sh.maga.blazecloud.backend.auth.model.RefreshToken;
import sh.maga.blazecloud.backend.auth.repository.RefreshTokenRepository;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;
import sh.maga.blazecloud.backend.user.model.User;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class AuthService {

    @Inject
    GroupRepository groupRepository;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String tokenIssuer;

    @ConfigProperty(name = "access-token.lifetime.seconds", defaultValue = "300")
    long accessTokenLifeTimeSeconds;

    @ConfigProperty(name = "refresh-token.lifetime.minutes", defaultValue = "43200")
    long refreshTokenLifeTimeMinutes;

    public String generateToken(User user) {
        Set<String> permissions = new HashSet<>();
        for (UUID groupId : user.getGroups()) {
            Optional<Group> optional = groupRepository.findByIdOptional(groupId);
            optional.ifPresent(group -> permissions.addAll(group.getPermissions()));
        }
        return Jwt.issuer(tokenIssuer)
                .upn(user.getId().toString())
                .claim("permissions", permissions)
                .groups("test")
                .expiresIn(Duration.ofSeconds(accessTokenLifeTimeSeconds))
                .sign();
    }


    public Optional<RefreshToken> fetchRefreshToken(User user) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser(user.getId());
        if(optional.isEmpty()) {
            return Optional.empty();
        }
        RefreshToken token = optional.get();
        if(System.currentTimeMillis() > token.getExpireAt()) {
            invalidateRefreshToken(token);
            return Optional.empty();
        }
        return optional;
    }

    public void invalidateRefreshToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setUser(user.getId());
        refreshToken.setCreatedAt(System.currentTimeMillis());
        refreshToken.setExpireAt(System.currentTimeMillis() + 1000 * 60 * refreshTokenLifeTimeMinutes);
        refreshTokenRepository.persist(refreshToken);
        return refreshToken;
    }
}
