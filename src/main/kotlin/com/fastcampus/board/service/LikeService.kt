package com.fastcampus.board.service

import com.fastcampus.board.repository.LikeRepository
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.util.RedisUtil
import com.fastcampus.event.dto.LikeEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun createLike(postId: Long, createdBy: String) {
//        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
//        redisUtil.increment(redisUtil.getLikeCountKey(postId))
//        return likeRepository.save(Like(post, createdBy)).id
        applicationEventPublisher.publishEvent(LikeEvent(postId, createdBy))
    }

    fun countLike(postId: Long): Long {
        redisUtil.getCount(redisUtil.getLikeCountKey(postId))?.let { return it }
        with(likeRepository.countByPostId(postId)) {
            redisUtil.setData(redisUtil.getLikeCountKey(postId), this)
            return this
        }
    }
}
