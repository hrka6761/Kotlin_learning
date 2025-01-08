package ir.hrka.kotlin.domain.repositories.write

import ir.hrka.kotlin.core.utilities.Resource
import ir.hrka.kotlin.domain.entities.db.SnippetCode

interface WriteSnippetCodesRepo {

    suspend fun saveSnippetCodes(snippetCodes: Array<SnippetCode>): Resource<Boolean?>
    suspend fun removeSnippetCodes(pointId: Long): Resource<Boolean?>
}