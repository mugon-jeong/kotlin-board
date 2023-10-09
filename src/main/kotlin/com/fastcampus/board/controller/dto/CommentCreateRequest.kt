package com.fastcampus.board.controller.dto

import com.fastcampus.board.service.dto.CommentCreateRequestDto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequest.toDto() = CommentCreateRequestDto(
    content = content,
    createdBy = createdBy
)
