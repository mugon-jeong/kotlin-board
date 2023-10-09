package com.fastcampus.board.service.dto

import com.fastcampus.board.domain.Post

data class PostDetailResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val createdBy: String,
)
fun Post.toDetailResponseDto() = PostDetailResponseDto(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt.toString(),
    createdBy = createdBy
)