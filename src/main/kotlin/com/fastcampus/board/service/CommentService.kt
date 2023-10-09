package com.fastcampus.board.service

import com.fastcampus.board.exception.CommentNotDeletableException
import com.fastcampus.board.exception.CommentNotFoundException
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.repository.CommentRepository
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.service.dto.CommentCreateRequestDto
import com.fastcampus.board.service.dto.CommentUpdateRequestDto
import com.fastcampus.board.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createComment(postId: Long, createRequestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(createRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(commentId: Long, updateRequestDto: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        comment.update(updateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(commentId: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        if (comment.createdBy != deletedBy) throw CommentNotDeletableException()
        commentRepository.delete(comment)
        return commentId
    }
}
