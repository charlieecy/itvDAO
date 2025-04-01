package org.example.repository

interface CRUDRepository <T, ID>{
    fun getAll (): List<T>
    fun getById(id : ID): T?
    fun save(item: T):T
    fun delete(id: ID): T?
    fun update(id: ID, item: T): T?
}