package com.example.tribetrip

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform