package com.reqres.tests;

import com.reqres.base.BaseApiTest;
import com.reqres.models.UserData;
import com.reqres.models.UserListResponse;
import com.reqres.specs.ResponseSpecs;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Feature("Users API")
class UsersGetTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    @Story("List users")
    void testListUsersPage2() {
        UserListResponse response = given(requestSpec)
                .queryParam("page", 2)
                .when()
                .get("/api/users")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(UserListResponse.class);

        Assertions.assertThat(response.getPage()).isEqualTo(2);
        Assertions.assertThat(response.getData()).isNotEmpty();
        Assertions.assertThat(response.getTotal()).isGreaterThan(0);
    }

    @Test
    @Tag("smoke")
    @Story("Single user")
    void testGetSingleUser() {
        UserData response = given(requestSpec)
                .when()
                .get("/api/users/2")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(UserData.class);

        Assertions.assertThat(response.getData()).isNotNull();
        Assertions.assertThat(response.getData().getId()).isEqualTo(2);
        Assertions.assertThat(response.getData().getEmail()).isNotBlank();
        Assertions.assertThat(response.getData().getFirstName()).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Story("Single user not found")
    void testGetUserNotFound() {
        given(requestSpec)
                .when()
                .get("/api/users/23")
                .then()
                .spec(ResponseSpecs.notFound404());
    }

    @Test
    @Tag("regression")
    @Story("List users page 1")
    void testListUsersPage1() {
        given(requestSpec)
                .queryParam("page", 1)
                .when()
                .get("/api/users")
                .then()
                .spec(ResponseSpecs.ok200())
                .body("page", equalTo(1))
                .body("data", not(empty()));
    }

    @Test
    @Tag("regression")
    @Story("User data fields")
    void testUserHasRequiredFields() {
        given(requestSpec)
                .when()
                .get("/api/users/1")
                .then()
                .spec(ResponseSpecs.ok200())
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue());
    }

    @Test
    @Tag("regression")
    @Story("Delayed response")
    void testDelayedResponse() {
        given(requestSpec)
                .queryParam("delay", 1)
                .when()
                .get("/api/users")
                .then()
                .spec(ResponseSpecs.ok200())
                .body("data", not(empty()));
    }
}
