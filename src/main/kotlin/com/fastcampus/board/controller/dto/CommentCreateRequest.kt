package com.fastcampus.board.controller.dto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
