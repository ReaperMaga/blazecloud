package sh.maga.blazecloud.backend.user.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Set;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@MongoEntity(collection = "users")
@Data
public class User  {

    @BsonId
    private UUID id;
    private String name;
    private String password;
    private Set<UUID> groups;
}
