package com.testows.models

enum class ErrorMessages(val errorMessage: String) {
    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields"),
    RECORD_ALREADY_EXISTS("Resource already exists in the system"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Resource with provided credentials is not found in the system"),
    AUTHENTICATION_FAILED("Authentication failed"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    TOKEN_HAS_EXPIRED("Could not delete record");
}