package com.testows.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PaginationUtilImpl: PaginationUtil {
    override fun customPaginate(page: Int, size: Int): Pageable {
        return PageRequest.of(if (page != 0) { page - 1 } else page, size)
    }
}