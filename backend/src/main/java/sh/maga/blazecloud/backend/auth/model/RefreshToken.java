package sh.maga.blazecloud.backend.auth.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@MongoEntity(collection = "refresh_tokens")
@Data
public class RefreshToken {

    @BsonId
    private UUID id;

    private UUID user;
    private long createdAt;
    private long expireAt;
}
