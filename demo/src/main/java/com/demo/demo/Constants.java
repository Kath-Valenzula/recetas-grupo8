package com.demo.demo;

/**
 * // FIX: Centralize shared auth constants to avoid duplication and warning-prone literals.
 */
public final class Constants {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String SUPER_SECRET_KEY = "change-me-please-32bytes-min";

    private Constants() {
        // FIX: Prevent instantiation of constants holder.
    }
}
