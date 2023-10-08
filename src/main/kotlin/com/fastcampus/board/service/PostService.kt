package com.fastcampus.board.service

import com.fastcampus.board.domain.QPost.post
import com.fastcampus.board.exception.PostNotDeletableException
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.service.dto.PostCreateRequestDto
import com.fastcampus.board.service.dto.PostUpdateRequestDto
import com.fastcampus.board.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(private val postRepository: PostRepository) {

    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if (post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.delete(post)
        return id
    }
}
