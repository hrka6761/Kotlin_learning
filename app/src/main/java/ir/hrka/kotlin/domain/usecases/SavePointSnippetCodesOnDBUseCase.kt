package ir.hrka.kotlin.domain.usecases

import ir.hrka.kotlin.domain.entities.db.SnippetCode
import ir.hrka.kotlin.domain.repositories.WriteCheatsheetRepo
import javax.inject.Inject

class SavePointSnippetCodesOnDBUseCase @Inject constructor(private val writeCheatsheetRepo: WriteCheatsheetRepo) {

    suspend operator fun invoke(snippetCodes: Array<SnippetCode>) =
        writeCheatsheetRepo.saveSnippetCodesOnDB(snippetCodes)
}