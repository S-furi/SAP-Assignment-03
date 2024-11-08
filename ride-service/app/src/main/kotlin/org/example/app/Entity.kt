package org.example.app

interface Entity<K : Any> {
    fun getId() : K
}
