package com.reqres.config;

public final class ApiConfig {
    private ApiConfig() {}

    public static String getBaseUrl() {
        return System.getenv().getOrDefault("API_BASE_URL", "https://reqres.in");
    }

    public static int getConnectTimeoutMs() {
        return Integer.parseInt(System.getenv().getOrDefault("CONNECT_TIMEOUT_MS", "10000"));
    }
}
