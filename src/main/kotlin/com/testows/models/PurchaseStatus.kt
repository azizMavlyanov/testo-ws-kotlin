package com.testows.models

enum class PurchaseStatus(val status: String) {
    OPEN("open"),
    PENDING("pending"),
    CLOSED("closed"),
    REJECTED("rejected")
}