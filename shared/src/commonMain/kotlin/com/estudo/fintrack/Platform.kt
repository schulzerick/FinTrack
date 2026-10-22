package com.estudo.fintrack

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform