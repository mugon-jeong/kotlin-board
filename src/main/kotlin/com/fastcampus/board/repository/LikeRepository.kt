package com.fastcampus.board.repository

import com.fastcampus.board.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun countByPostId(postId: Long): Long
}
