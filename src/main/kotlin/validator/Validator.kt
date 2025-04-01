package org.example.validator

interface Validator<T> {
    fun validate(item: T): T
}