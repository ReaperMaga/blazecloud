package sh.maga.blazecloud.backend.group.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import sh.maga.blazecloud.backend.exception.ValidationGroups;

import java.util.Set;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@MongoEntity(collection = "groups")
@Data
public class Group {

    @BsonId
    private UUID id;
    @NotBlank(groups = ValidationGroups.Post.class)
    private String name;
    private Set<String> permissions;
}
