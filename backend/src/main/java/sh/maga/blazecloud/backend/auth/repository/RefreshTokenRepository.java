package sh.maga.blazecloud.backend.auth.repository;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import sh.maga.blazecloud.backend.auth.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class RefreshTokenRepository implements PanacheMongoRepositoryBase<RefreshToken, UUID> {

    public Optional<RefreshToken> findByUser(UUID id) {
        return find("user", id).firstResultOptional();
    }

    public boolean existsByUser(UUID id) {
        return findByUser(id).isPresent();
    }

    public void onStart(@Observes StartupEvent event) {
        mongoCollection().createIndex(Indexes.ascending("user"));
    }
}
