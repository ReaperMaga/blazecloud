package sh.maga.blazecloud.backend;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.maga.blazecloud.backend.group.GroupService;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;
import sh.maga.blazecloud.backend.group.resource.GroupResource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@QuarkusTest
@TestHTTPEndpoint(GroupResource.class)
@TestSecurity(authorizationEnabled = false)
public class GroupResourceTest {

    @Inject
    GroupRepository repository;

    @Inject
    GroupService service;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test

    public void testGetAll() {
        Group group = service.create("Test");
        repository.persist(group);
        given()
                .when().get()
                .then()
                  .statusCode(200)
                  .body("size()", is(1));
    }

    @Test
    public void testGet() {
        Group group = service.create("Test");
        repository.persist(group);
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body("id", is(group.getId()));
    }
}
