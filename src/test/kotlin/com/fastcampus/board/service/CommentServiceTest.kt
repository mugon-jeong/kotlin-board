package com.fastcampus.board.service

import com.fastcampus.board.domain.Comment
import com.fastcampus.board.domain.Post
import com.fastcampus.board.exception.CommentNotDeletableException
import com.fastcampus.board.exception.CommentNotUpdatableException
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.repository.CommentRepository
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.service.dto.CommentCreateRequestDto
import com.fastcampus.board.service.dto.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    val redisContainer = GenericContainer<Nothing>("redis:5.0.3-alpine")
    beforeSpec {
        redisContainer.portBindings.add("16379:6379")
        redisContainer.start()
        listener(redisContainer.perSpec()) // spec단위 생명주기
    }
    afterSpec {
        redisContainer.stop()
    }
    given("댓글 생성시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        When("인풋이 정상적으로 들어오면") {
            val commentId = commentService.createComment(
                post.id,
                CommentCreateRequestDto(
                    content = "댓글 내용",
                    createdBy = "댓글 생성자"
                )
            )
            then("생성 완료됨을 확인한다.") {
                commentId shouldBeGreaterThan 0L
                val comment = commentRepository.findByIdOrNull(commentId)
                comment shouldNotBe null
                comment?.content shouldBe "댓글 내용"
            }
        }
        When("게시글이 존재하지 않으면") {
            then("게시글이 존재하지 않음 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    commentService.createComment(
                        9999L,
                        CommentCreateRequestDto(
                            content = "댓글 내용",
                            createdBy = "댓글 생성자"
                        )
                    )
                }
            }
        }
    }
    given("댓글 수정시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        val comment = commentRepository.save(Comment("댓글 내용", post, "댓글 생성자"))
        When("인풋이 정상적으로 들어오면") {
            val updatedId = commentService.updateComment(
                comment.id,
                CommentUpdateRequestDto(
                    content = "수정된 댓글 내용",
                    updatedBy = "댓글 생성자"
                )
            )
            then("정상 수정됨을 확인한다.") {
                updatedId shouldBe comment.id
                val updated = commentRepository.findByIdOrNull(updatedId)
                updated?.content shouldBe "수정된 댓글 내용"
            }
        }
        When("작성자와 수정자가 다르면") {
            then("수정할 수 없는 댓글 예외가 발생한다.") {
                shouldThrow<CommentNotUpdatableException> {
                    commentService.updateComment(
                        comment.id,
                        CommentUpdateRequestDto(
                            content = "수정된 댓글 내용",
                            updatedBy = "수정된 댓글 생성자"
                        )
                    )
                }
            }
        }
    }
    given("댓글 삭제시") {
        val post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        val comment = commentRepository.save(Comment("댓글 내용", post, "댓글 생성자"))
        val comment2 = commentRepository.save(Comment("댓글 내용", post, "댓글 생성자"))
        When("인풋이 정상적으로 들어오면") {
            val commentId = commentService.deleteComment(comment.id, "댓글 생성자")
            then("정상 삭제됨을 확인한다.") {
                comment.id shouldBe commentId
                commentRepository.findByIdOrNull(commentId) shouldBe null
            }
        }
        When("작성자와 삭제자가 다르면") {
            then("삭제할 수 없는 댓글 예외가 발생한다.") {
                shouldThrow<CommentNotDeletableException> {
                    commentService.deleteComment(comment2.id, "삭제자")
                }
            }
        }
    }
})
