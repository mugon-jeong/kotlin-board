package com.fastcampus.board.controller.dto

data class PostSummaryResponse(
    val id: String,
    val title: String,
    val createdBy: String,
    val createdAt: String,
)
