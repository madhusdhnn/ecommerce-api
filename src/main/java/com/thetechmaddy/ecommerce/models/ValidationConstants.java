package com.thetechmaddy.ecommerce.models;

public class ValidationConstants {

    public static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String NOT_NULL_MESSAGE_SUFFIX = "must not be null";
    public static final String FIRST_NAME_MIN_LENGTH_MESSAGE = "First Name must be minimum 1 character length.";
    public static final String LAST_NAME_MIN_LENGTH_MESSAGE = "Last Name must be minimum 1 character length.";
}
