package com.fastcampus.event

import com.fastcampus.board.domain.Like
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.repository.LikeRepository
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.util.RedisUtil
import com.fastcampus.event.dto.LikeEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LikeEventHandler(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val redisUtil: RedisUtil,
) {

    @Async
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        Thread.sleep(3000)
        val post = postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException()
        redisUtil.increment(redisUtil.getLikeCountKey(event.postId))
        likeRepository.save(Like(post, event.createdBy)).id
    }
}
