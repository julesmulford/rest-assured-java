package com.reqres.tests;

import com.reqres.base.BaseApiTest;
import com.reqres.models.CreateUserRequest;
import com.reqres.models.CreateUserResponse;
import com.reqres.specs.ResponseSpecs;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Feature("Users API")
class UsersCreateUpdateTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    @Story("Create user")
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "leader");

        CreateUserResponse response = given(requestSpec)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .spec(ResponseSpecs.created201())
                .extract()
                .as(CreateUserResponse.class);

        Assertions.assertThat(response.getId()).isNotBlank();
        Assertions.assertThat(response.getName()).isEqualTo("morpheus");
        Assertions.assertThat(response.getJob()).isEqualTo("leader");
        Assertions.assertThat(response.getCreatedAt()).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Story("Update user PUT")
    void testUpdateUserWithPut() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "zion resident");

        CreateUserResponse response = given(requestSpec)
                .body(request)
                .when()
                .put("/api/users/2")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(CreateUserResponse.class);

        Assertions.assertThat(response.getName()).isEqualTo("morpheus");
        Assertions.assertThat(response.getJob()).isEqualTo("zion resident");
        Assertions.assertThat(response.getUpdatedAt()).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Story("Update user PATCH")
    void testUpdateUserWithPatch() {
        CreateUserRequest request = new CreateUserRequest("morpheus", "zion resident");

        CreateUserResponse response = given(requestSpec)
                .body(request)
                .when()
                .patch("/api/users/2")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(CreateUserResponse.class);

        Assertions.assertThat(response.getName()).isEqualTo("morpheus");
        Assertions.assertThat(response.getUpdatedAt()).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Story("Create user with different role")
    void testCreateUserWithDifferentRole() {
        CreateUserRequest request = new CreateUserRequest("neo", "chosen one");

        CreateUserResponse response = given(requestSpec)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .spec(ResponseSpecs.created201())
                .extract()
                .as(CreateUserResponse.class);

        Assertions.assertThat(response.getName()).isEqualTo("neo");
        Assertions.assertThat(response.getJob()).isEqualTo("chosen one");
    }

    @Test
    @Tag("regression")
    @Story("Created user has timestamp")
    void testCreatedUserHasTimestamp() {
        CreateUserRequest request = new CreateUserRequest("trinity", "operator");

        given(requestSpec)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .spec(ResponseSpecs.created201())
                .body("createdAt", org.hamcrest.Matchers.notNullValue())
                .body("id", org.hamcrest.Matchers.notNullValue());
    }
}
