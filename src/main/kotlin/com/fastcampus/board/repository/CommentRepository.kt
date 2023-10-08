package com.fastcampus.board.repository

import com.fastcampus.board.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>
