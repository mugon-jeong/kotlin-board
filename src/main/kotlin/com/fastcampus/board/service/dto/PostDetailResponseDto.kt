package com.fastcampus.board.service.dto

import com.fastcampus.board.domain.Post

data class PostDetailResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val createdBy: String,
    val comments: List<CommentResponseDto>,
    val tags: List<String> = emptyList(),
    val likeCount: Long = 0,
)

fun Post.toDetailResponseDto(likeCount: Long) = PostDetailResponseDto(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt.toString(),
    createdBy = createdBy,
    comments = comments.map { it.toResponseDto() },
    tags = tags.map { it.name },
    likeCount = likeCount
)
