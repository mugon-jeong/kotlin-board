package com.fastcampus.board.controller.dto

import com.fastcampus.board.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
    val tags: List<String> = emptyList(),
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = title,
    content = content,
    updatedBy = updatedBy,
    tags = tags
)
