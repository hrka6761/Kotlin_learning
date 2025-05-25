package ir.hrka.kotlin.domain.entities.git

interface Data<T> {

    fun getMasterData(): T
}