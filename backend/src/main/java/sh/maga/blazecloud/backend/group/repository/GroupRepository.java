package sh.maga.blazecloud.backend.group.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import sh.maga.blazecloud.backend.group.model.Group;

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
public class GroupRepository implements PanacheMongoRepositoryBase<Group, UUID> {

    public Optional<Group> findByName(String name) {
        return find("name", name).firstResultOptional();
    }


}
