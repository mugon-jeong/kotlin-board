package com.fastcampus.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)
