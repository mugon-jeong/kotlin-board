package com.fastcampus.board.controller

import com.fastcampus.board.controller.dto.CommentCreateRequest
import com.fastcampus.board.controller.dto.CommentUpdateRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController {
    @PostMapping("posts/{postId}/comments")
    fun createCommnet(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        return 1L
    }

    @PutMapping("comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        return 1L
    }

    @DeleteMapping("comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        return 1L
    }
}
