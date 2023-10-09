package com.fastcampus.board.service

import com.fastcampus.board.exception.PostNotDeletableException
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.repository.TagRepository
import com.fastcampus.board.service.dto.PageSearchRequestDto
import com.fastcampus.board.service.dto.PostCreateRequestDto
import com.fastcampus.board.service.dto.PostDetailResponseDto
import com.fastcampus.board.service.dto.PostSummaryResponseDto
import com.fastcampus.board.service.dto.PostUpdateRequestDto
import com.fastcampus.board.service.dto.toDetailResponseDto
import com.fastcampus.board.service.dto.toEntity
import com.fastcampus.board.service.dto.toSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val likeService: LikeService,
    private val tagRepository: TagRepository,
) {

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

    fun getPost(id: Long): PostDetailResponseDto {
        val likeCount = likeService.countLike(id)
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto(likeCount) ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PageSearchRequestDto): Page<PostSummaryResponseDto> {
        postSearchRequestDto.tag?.let {
            return tagRepository.findPageBy(pageRequest, it).toSummaryResponseDto(likeService::countLike)
        }

        // 태그로 검색 조회시
        // post id 별로 태그 조회 n번이 추가적으로 실행
        return postRepository.findPageBy(pageRequest, postSearchRequestDto).toSummaryResponseDto(likeService::countLike)
    }
}
