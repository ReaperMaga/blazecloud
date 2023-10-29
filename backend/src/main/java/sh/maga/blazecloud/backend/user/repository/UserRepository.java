package sh.maga.blazecloud.backend.user.repository;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.bson.Document;
import sh.maga.blazecloud.backend.user.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class UserRepository implements PanacheMongoRepositoryBase<User, UUID> {

    private final Collation nameCollation = Collation.builder().locale("en").collationStrength(CollationStrength.SECONDARY).build();

    public Optional<User> findByName(String name) {
        return find("name", name).withCollation(nameCollation).firstResultOptional();
    }

    public boolean existsByName(String name) {
        return findByName(name).isPresent();
    }

    public void onStart(@Observes StartupEvent event) {
        IndexOptions options = new IndexOptions().collation(nameCollation);
        mongoCollection().createIndex(Indexes.ascending("name"), options);
    }


}
