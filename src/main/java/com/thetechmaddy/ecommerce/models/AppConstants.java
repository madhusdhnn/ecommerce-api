package com.thetechmaddy.ecommerce.models;

public class AppConstants {

    public static final String COGNITO_GROUPS_CLAIM_NAME = "cognito:groups";
    public static final String COGNITO_ADMIN_GROUP_NAME = "Admin";
    public static final String COGNITO_USER_GROUP_NAME = "User";

    public static final String CURRENT_USER_REQUEST_ATTRIBUTE = "currentUser";

    public static final String JWT_TOKEN_EMAIL_ATTRIBUTE_NAME = "email";
    public static final String JWT_TOKEN_FIRST_NAME_ATTRIBUTE_NAME = "custom:first_name";
    public static final String JWT_TOKEN_LAST_NAME_ATTRIBUTE_NAME = "custom:last_name";

    public static final String PAYMENT_ID_HEADER_NAME = "X-Payment-ID";

    public static final String SECURITY_SCHEME_NAME = "CognitoAuth";
    public static final String INTERNAL_API_KEY_HEADER_NAME = "X-API-Key";
    public static final String DEFAULT_DELETE_ORDER_STATUS = "PENDING";
}
