package com.reqres.tests;

import com.reqres.base.BaseApiTest;
import com.reqres.specs.ResponseSpecs;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Feature("Users API")
class UsersDeleteTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    @Story("Delete user")
    void testDeleteUser() {
        given(requestSpec)
                .when()
                .delete("/api/users/2")
                .then()
                .spec(ResponseSpecs.noContent204());
    }

    @Test
    @Tag("regression")
    @Story("Delete non-existent user")
    void testDeleteNonExistentUser() {
        given(requestSpec)
                .when()
                .delete("/api/users/999")
                .then()
                .spec(ResponseSpecs.noContent204());
    }
}
