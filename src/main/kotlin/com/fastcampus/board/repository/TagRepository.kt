package com.fastcampus.board.repository

import com.fastcampus.board.domain.QPost.post
import com.fastcampus.board.domain.QTag.tag
import com.fastcampus.board.domain.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface TagRepository : JpaRepository<Tag, Long>, CustomTagRepository {
    fun findByPostId(postId: Long): List<Tag>
}

interface CustomTagRepository {
    fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag>
}

class CustomTagRepositoryImpl : CustomTagRepository, QuerydslRepositorySupport(Tag::class.java) {
    override fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag> {
        return from(tag)
            // order by의 tag.post와 join의 tag.post가 매핑이 안될때 join문이 2개 생성됨
            // join에 2번째 인자로 post 넣어 매핑해줘야함
            .join(tag.post, post).fetchJoin() // n+1 쿼리 해결(tag조회시 post 함께 조회)
            .where(tag.name.eq(tagName))
            .orderBy(tag.post.createdAt.desc())
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetchResults()
            .let { PageImpl(it.results, pageRequest, it.total) }
    }
}
