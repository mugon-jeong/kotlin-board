package com.fastcampus.board.service

import com.fastcampus.board.domain.Comment
import com.fastcampus.board.domain.Post
import com.fastcampus.board.domain.Tag
import com.fastcampus.board.exception.PostNotDeletableException
import com.fastcampus.board.exception.PostNotFoundException
import com.fastcampus.board.exception.PostNotUpdatableException
import com.fastcampus.board.repository.CommentRepository
import com.fastcampus.board.repository.PostRepository
import com.fastcampus.board.repository.TagRepository
import com.fastcampus.board.service.dto.PageSearchRequestDto
import com.fastcampus.board.service.dto.PostCreateRequestDto
import com.fastcampus.board.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
) : BehaviorSpec({
    beforeTest {
        postRepository.saveAll(
            listOf(
                Post(
                    title = "title1",
                    content = "content1",
                    createdBy = "harris1",
                    tags = listOf("tag1", "tag2", "tag3")
                ),
                Post(
                    title = "title12",
                    content = "content1",
                    createdBy = "harris1",
                    tags = listOf("tag1", "tag2", "tag3")
                ),
                Post(
                    title = "title13",
                    content = "content1",
                    createdBy = "harris1",
                    tags = listOf("tag1", "tag2", "tag3")
                ),
                Post(
                    title = "title14",
                    content = "content1",
                    createdBy = "harris1",
                    tags = listOf("tag1", "tag2", "tag3")
                ),
                Post(
                    title = "title15",
                    content = "content1",
                    createdBy = "harris1",
                    tags = listOf("tag1", "tag2", "tag3")
                ),
                Post(
                    title = "title6",
                    content = "content1",
                    createdBy = "harris2",
                    tags = listOf("tag1", "tag2", "tag5")
                ),
                Post(
                    title = "title7",
                    content = "content1",
                    createdBy = "harris2",
                    tags = listOf("tag1", "tag2", "tag5")
                ),
                Post(
                    title = "title8",
                    content = "content1",
                    createdBy = "harris2",
                    tags = listOf("tag1", "tag2", "tag5")
                ),
                Post(
                    title = "title9",
                    content = "content1",
                    createdBy = "harris2",
                    tags = listOf("tag1", "tag2", "tag5")
                ),
                Post(
                    title = "title10",
                    content = "content1",
                    createdBy = "harris2",
                    tags = listOf("tag1", "tag2", "tag5")
                )
            )
        )
    }
    given("게시글 생성 시") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "harris"
                )
            )
            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "harris"
            }
        }
        When("태그가 추가되면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "harris",
                    tags = listOf("tag1", "tag2", "tag3")
                )
            )
            then("태그가 정삭적으로 추가됨을 확인한다.") {
                val tags = tagRepository.findByPostId(postId)
                tags.size shouldBe 3
            }
        }
    }

    given("게시글 수정시") {
        val saved = postRepository.save(
            Post(
                title = "title",
                content = "content",
                createdBy = "harris",
                tags = listOf("tag1", "tag2")
            )
        )
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "harris"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없을때") {
            then("게시글을 찾을수 없다는 에러가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "harris"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("수정할 수 없는 게시글 입니다. 예외가 발생한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update harris"
                        )
                    )
                }
            }
        }
        When("태그가 수정되었을때") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "harris",
                    tags = listOf("tag1", "tag2", "tag3")
                )
            )
            then("정상적으로 수정됨을 확인한다.") {
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag3"
            }
            then("태그 순서가 변경되었을때 정상적으로 변경됨을 확인한다.") {
                postService.updatePost(
                    saved.id,
                    PostUpdateRequestDto(
                        title = "update title",
                        content = "update content",
                        updatedBy = "harris",
                        tags = listOf("tag3", "tag2", "tag1")
                    )
                )
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag1"
            }
        }
    }

    given("게시글 삭제시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
        When("정상 삭제시") {
            val postId = postService.deletePost(saved.id, "harris")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        When("작성자가 동일하지 않으면") {
            val saved2 = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
            then("삭제할 수 없는 게시물입니다. 에러가 발생한다.") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved2.id, "harris2")
                }
            }
        }
    }

    given("게시글 상세 조회시") {
        val saved = postRepository.save(Post(title = "title", content = "content", createdBy = "harris"))
        tagRepository.saveAll(
            listOf(
                Tag(name = "tag1", post = saved, createdBy = "harris"),
                Tag(name = "tag2", post = saved, createdBy = "harris"),
                Tag(name = "tag3", post = saved, createdBy = "harris")
            )
        )
        When("정상 조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환됨을 확인한다.") {
                post.id shouldBe saved.id
                post.title shouldBe saved.title
                post.content shouldBe saved.content
                post.createdBy shouldBe saved.createdBy
            }
            then("태그가 정상적으로 조회됨을 확인한다.") {
                post.tags.size shouldBe 3
            }
        }
        When("게시글이 없을 때") {
            then("게시글을 찾을 수 없다는 에러가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.getPost(9999L)
                }
            }
        }
        When("댓글 추가시") {
            commentRepository.save(Comment(content = "댓글 내용1", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용2", post = saved, createdBy = "댓글 작성자"))
            commentRepository.save(Comment(content = "댓글 내용3", post = saved, createdBy = "댓글 작성자"))
            val post = postService.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인한다.") {
                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "댓글 내용1"
            }
        }
    }
    given("게시글 목록 조회시") {
        When("정상 조회시") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PageSearchRequestDto())
            then("게시글 페이지가 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "harris"
            }
        }
        When("타이틀로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PageSearchRequestDto(title = "title1"))
            then("타이틀에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "harris"
            }
        }
        When("작성자로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PageSearchRequestDto(createdBy = "harris1"))
            then("작성자에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldBe "harris1"
            }
            then("첫번째 태그가 함께 조회됨을 확인한다.") {
                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }
        When("태그로 검색") {
            val postPage = postService.findPageBy(PageRequest.of(0, 5), PageSearchRequestDto(tag = "tag5"))
            then("태그에 해당하는 게시글이 반환된다.") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].firstTag shouldBe "tag5"
            }
        }
    }
})
