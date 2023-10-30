package sh.maga.blazecloud.backend.group;

import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class GroupService {

    @Inject
    GroupRepository repository;

    @ConfigProperty(name = "default.group", defaultValue = "Member")
    String defaultGroupName;



    public Group create(String name, String... permissions) {
        return create(name, new HashSet<>(Arrays.asList(permissions)));
    }

    public Group create(String name, Set<String> permissions) {
        Group group = new Group();
        group.setId(UUID.randomUUID());
        group.setName(name);
        group.setPermissions(permissions);
        return group;
    }

    public Group getDefaultGroup() {
        return repository.find("name", defaultGroupName).firstResult();
    }

    public void onStart(@Observes @Priority(1) StartupEvent event) {
        if(repository.count() == 0) {
            repository.persist(create("Admin", "*"));
            repository.persist(create("Member"));
        }
    }


}
