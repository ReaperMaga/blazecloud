package sh.maga.blazecloud.backend.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;
import sh.maga.blazecloud.backend.user.model.User;
import sh.maga.blazecloud.backend.user.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class UserService {

    @Inject
    UserRepository repository;

    @Inject
    GroupRepository groupRepository;

    @ConfigProperty(name = "bcrypt.cost", defaultValue = "12")
    int bcryptCost;

    public void onStart(@Observes @Priority(2) StartupEvent event) {
        if(repository.count() == 0) {
            Optional<Group> adminOptional = groupRepository.findByName("Admin");
            adminOptional.ifPresent(group -> repository.persist(create("admin", "admin", group.getId())));
        }
    }

    public User create(String name, String password, UUID... groups) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setPassword(hash(password));
        user.setGroups(new HashSet<>(Arrays.asList(groups)));
        return user;
    }

    public boolean verifyHash(String first, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(first.toCharArray(), hash);
        return result.verified;
    }

    public String hash(String text) {
        String result = BCrypt.withDefaults().hashToString(bcryptCost, text.toCharArray());
        return result;
    }
}
