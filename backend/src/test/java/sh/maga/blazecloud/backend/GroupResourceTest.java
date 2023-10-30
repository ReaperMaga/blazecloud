package sh.maga.blazecloud.backend;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
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
                .when().get("/" + group.getId())
                .then()
                .statusCode(200)
                .body("name", is(group.getName()));
    }

    @Test
    public void testCreate() {
        Group group = service.create("Test");
        given().body(group).contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(201)
                .body("name", is(group.getName()));
    }

    @Test
    public void testCreateFail() {
        Group group = service.create("Test");
        repository.persist(group);
        given().body(group).contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(403);
    }

    @Test
    public void testUpdate() {
        Group group = service.create("Test");
        repository.persist(group);

        Group newUpdate = new Group();
        newUpdate.setName("OtherTest");
        given().body(newUpdate).contentType(ContentType.JSON)
                .when().patch("/" + group.getId())
                .then()
                .statusCode(200)
                .body("name", is(newUpdate.getName()));
    }
}
