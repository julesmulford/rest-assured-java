package com.reqres.base;

import com.reqres.specs.RequestSpecs;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class BaseApiTest {
    protected RequestSpecification requestSpec;

    @BeforeAll
    static void globalSetup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        requestSpec = RequestSpecs.defaultSpec();
        Allure.parameter("Test", testInfo.getDisplayName());
    }
}
