package ir.hrka.kotlin.domain.entities

import ir.hrka.kotlin.local_ai_chat.utilities.MessageOwner

data class ChatMessage(
    val id: Int,
    var text: String,
    val owner: MessageOwner
)
