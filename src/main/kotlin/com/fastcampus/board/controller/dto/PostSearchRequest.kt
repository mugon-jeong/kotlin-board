package com.fastcampus.board.controller.dto

import com.fastcampus.board.service.dto.PageSearchRequestDto
import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val createdBy: String?,
    @RequestParam
    val tag: String?,
)

fun PostSearchRequest.toDto() = PageSearchRequestDto(
    title = title,
    createdBy = createdBy,
    tag = tag
)
