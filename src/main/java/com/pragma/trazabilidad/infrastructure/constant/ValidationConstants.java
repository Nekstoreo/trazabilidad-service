package com.pragma.trazabilidad.infrastructure.constant;

public final class ValidationConstants {

    private ValidationConstants() {
        throw new AssertionError("Cannot instantiate ValidationConstants");
    }

    public static final int MIN_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MIN_PAGE_SIZE = 1;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
}
