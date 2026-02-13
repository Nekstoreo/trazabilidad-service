package com.pragma.trazabilidad.infrastructure.constant;

public final class ApiConstants {

    private ApiConstants() {
        throw new AssertionError("Cannot instantiate ApiConstants");
    }

    public static final String TRACEABILITY_BASE_PATH = "/traceability";
    public static final String EFFICIENCY_PATH = "/efficiency";
    public static final String RESTAURANT_PATH = "/restaurant";
    public static final String ORDERS_PATH = "/orders";

    public static final String SWAGGER_PATH = "/swagger-ui.html";
    public static final String API_DOCS_PATH = "/api-docs";

    public static final String STATUS_200 = "200";
    public static final String STATUS_400 = "400";
    public static final String STATUS_401 = "401";
    public static final String STATUS_403 = "403";
    public static final String STATUS_404 = "404";
    public static final String STATUS_500 = "500";

    public static final String APPLICATION_JSON = "application/json";
}
