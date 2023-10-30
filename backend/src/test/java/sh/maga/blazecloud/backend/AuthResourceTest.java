package sh.maga.blazecloud.backend;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sh.maga.blazecloud.backend.auth.resource.AuthResource;
import sh.maga.blazecloud.backend.auth.resource.request.AuthLoginBasicRequest;
import sh.maga.blazecloud.backend.auth.resource.request.AuthRegisterRequest;
import sh.maga.blazecloud.backend.group.GroupService;
import sh.maga.blazecloud.backend.group.model.Group;
import sh.maga.blazecloud.backend.group.repository.GroupRepository;
import sh.maga.blazecloud.backend.group.resource.GroupResource;
import sh.maga.blazecloud.backend.user.UserService;
import sh.maga.blazecloud.backend.user.model.User;
import sh.maga.blazecloud.backend.user.repository.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@QuarkusTest
@TestHTTPEndpoint(AuthResource.class)
@TestSecurity(authorizationEnabled = false)
public class AuthResourceTest {

    @Inject
    UserRepository repository;

    @Inject
    UserService userService;

    @Inject
    GroupService groupService;

    @Inject
    GroupRepository groupRepository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        groupRepository.deleteAll();
        groupRepository.persist(groupService.create("Member"));
    }

    @Test
    public void testLoginBasic() {
        User user = userService.create("TestUser", "test");
        repository.persist(user);
        given().body(new AuthLoginBasicRequest(user.getName(), "test")).contentType(ContentType.JSON)
                .when().post("/login/basic")
                .then()
                .statusCode(200)
                .body("user.id", is(user.getId().toString()));
    }

    @Test
    public void testRegister() {
        given().body(new AuthRegisterRequest("TestUser", "test")).contentType(ContentType.JSON)
                .when().post("/register")
                .then()
                .statusCode(200)
                .body("user.name", is("TestUser"));
    }

    @Test
    public void testRegisterFailNameTaken() {
        User user = userService.create("TestUser", "test");
        repository.persist(user);
        given().body(new AuthRegisterRequest("TestUser", "test")).contentType(ContentType.JSON)
                .when().post("/register")
                .then()
                .statusCode(403);
    }


}
