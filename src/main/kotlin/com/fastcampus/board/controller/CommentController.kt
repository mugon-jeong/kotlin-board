package com.fastcampus.board.controller

import com.fastcampus.board.controller.dto.CommentCreateRequest
import com.fastcampus.board.controller.dto.CommentUpdateRequest
import com.fastcampus.board.controller.dto.toDto
import com.fastcampus.board.service.CommentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("posts/{postId}/comments")
    fun createCommnet(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        return commentService.createComment(postId, commentCreateRequest.toDto())
    }

    @PutMapping("comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        return commentService.updateComment(commentId, commentUpdateRequest.toDto())
    }

    @DeleteMapping("comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        return commentService.deleteComment(commentId, deletedBy)
    }
}
