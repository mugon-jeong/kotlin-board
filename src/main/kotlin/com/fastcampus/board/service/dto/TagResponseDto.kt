package com.fastcampus.board.service.dto

import com.fastcampus.board.domain.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun Page<Tag>.toSummaryResponseDto(likeCount: (Long) -> Long) = PageImpl(
    content.map { it.toSummaryResponseDto(likeCount) },
    pageable,
    totalElements
)

fun Tag.toSummaryResponseDto(likeCount: (Long) -> Long) = PostSummaryResponseDto(
    id = post.id,
    title = post.title,
    createdBy = post.createdBy,
    createdAt = post.createdAt.toString(),
    firstTag = name,
    likeCount = likeCount(post.id)
)
