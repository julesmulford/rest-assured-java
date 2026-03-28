package com.reqres.tests;

import com.reqres.base.BaseApiTest;
import com.reqres.models.LoginRequest;
import com.reqres.models.LoginResponse;
import com.reqres.specs.ResponseSpecs;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Authentication API")
class AuthTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    @Story("Login success")
    void testLoginReturnsToken() {
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", "cityslicka");

        LoginResponse response = given(requestSpec)
                .body(request)
                .when()
                .post("/api/login")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(LoginResponse.class);

        Assertions.assertThat(response.getToken()).isNotBlank();
    }

    @Test
    @Tag("regression")
    @Story("Login missing password")
    void testLoginWithMissingPassword() {
        LoginRequest request = new LoginRequest("peter@klaven.com", null);

        given(requestSpec)
                .body(request)
                .when()
                .post("/api/login")
                .then()
                .spec(ResponseSpecs.badRequest400())
                .body("error", notNullValue());
    }

    @Test
    @Tag("smoke")
    @Story("Register success")
    void testRegisterReturnsToken() {
        LoginRequest request = new LoginRequest("eve.holt@reqres.in", "pistol");

        LoginResponse response = given(requestSpec)
                .body(request)
                .when()
                .post("/api/register")
                .then()
                .spec(ResponseSpecs.ok200())
                .extract()
                .as(LoginResponse.class);

        Assertions.assertThat(response.getToken()).isNotBlank();
        Assertions.assertThat(response.getId()).isGreaterThan(0);
    }

    @Test
    @Tag("regression")
    @Story("Register missing password")
    void testRegisterWithMissingPassword() {
        LoginRequest request = new LoginRequest("sydney@fife.com", null);

        given(requestSpec)
                .body(request)
                .when()
                .post("/api/register")
                .then()
                .spec(ResponseSpecs.badRequest400())
                .body("error", notNullValue());
    }
}
