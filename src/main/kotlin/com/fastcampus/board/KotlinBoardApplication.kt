package com.fastcampus.board

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class KotlinBoardApplication

fun main(args: Array<String>) {
    runApplication<KotlinBoardApplication>(*args)
}
