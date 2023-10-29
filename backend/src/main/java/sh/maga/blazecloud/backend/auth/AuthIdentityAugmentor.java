package sh.maga.blazecloud.backend.auth;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonString;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class AuthIdentityAugmentor implements SecurityIdentityAugmentor {


    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity securityIdentity, AuthenticationRequestContext authenticationRequestContext) {
        return securityIdentity.isAnonymous() ? Uni.createFrom().item(securityIdentity) : Uni.createFrom().item(build(securityIdentity));
    }

    private SecurityIdentity build(SecurityIdentity current) {
        DefaultJWTCallerPrincipal principal = current.getAttribute("quarkus.user");
        List<JsonString> permissions = principal.getClaim("permissions");
        return QuarkusSecurityIdentity.builder(current).addPermissionChecker(permission -> {
            for(JsonString string : permissions) {
                if(string.getString().equals("*") || string.getString().equalsIgnoreCase(permission.getName())) {
                    return Uni.createFrom().item(true);
                }
            }
            return Uni.createFrom().item(false);
        }).build();
    }
}
