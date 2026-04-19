package com.gustavo.kmpdexlib.core.domain

sealed interface DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : DomainResult<Nothing>
}
