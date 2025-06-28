package ir.hrka.kotlin.local_ai_chat.exceptions

import java.util.concurrent.CancellationException


class ManuallyGenerationCancellationException : CancellationException("Generation is canceled!")