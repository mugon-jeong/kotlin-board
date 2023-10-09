package com.fastcampus.board.service.dto

import com.fastcampus.board.domain.Comment
import com.fastcampus.board.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
