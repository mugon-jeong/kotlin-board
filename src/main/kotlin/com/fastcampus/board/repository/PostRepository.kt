package com.fastcampus.board.repository

import com.fastcampus.board.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository :JpaRepository<Post, Long> {
}
