package com.testows.model

data class PageableAndSortableData<T>(
        var page: Int,
        var size: Int,
        var hasPrevious: Boolean,
        var hasNext: Boolean,
        var totalElements: Long,
        var sort: String,
        var data: List<T>
)