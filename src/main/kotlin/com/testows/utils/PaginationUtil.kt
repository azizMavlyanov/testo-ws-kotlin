package com.testows.utils

import org.springframework.data.domain.Pageable

interface PaginationUtil {
    fun customPaginate(page: Int, size: Int): Pageable
}