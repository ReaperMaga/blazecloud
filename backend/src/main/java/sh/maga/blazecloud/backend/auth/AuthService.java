package sh.maga.blazecloud.backend.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String tokenIssuer;

    @ConfigProperty(name = "access-token.lifetime.seconds", defaultValue = "300")
    long accessTokenLifeTimeSeconds;

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
}
