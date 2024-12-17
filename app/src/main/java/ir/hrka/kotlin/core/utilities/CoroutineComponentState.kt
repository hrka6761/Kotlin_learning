package ir.hrka.kotlin.core.utilities

sealed class CoroutineComponentState<out T: ComponentData>(val componentData: T? = null) {

    class Stop<T: ComponentData> : CoroutineComponentState<T>()
    class Processing<T: ComponentData>(componentData: T) : CoroutineComponentState<T>(componentData)
    class Done<T: ComponentData>(componentData: T) : CoroutineComponentState<T>(componentData)
    class Cancel<T: ComponentData>(componentData: T) : CoroutineComponentState<T>(componentData)
    class Failed<T: ComponentData>(componentData: T) : CoroutineComponentState<T>(componentData)
}