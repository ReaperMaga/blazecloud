package sh.maga.blazecloud.backend.group;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;

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

    public void onStart(@Observes StartupEvent event) {
        if(repository.count() == 0) {
            repository.persist(repository.create("Member"));
        }
    }
}
