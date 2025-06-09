package ir.hrka.kotlin.domain.entities

import ir.hrka.kotlin.core.chat.MessageOwner

data class ChatMessage(
    val text: String,
    val owner: MessageOwner
)
