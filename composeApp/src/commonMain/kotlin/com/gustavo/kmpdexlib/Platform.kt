package com.gustavo.kmpdexlib

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform