package ir.hrka.kotlin.core.utilities.coroutine_visualizers_utilities

sealed class ComponentState<out T: ComponentData>(val componentData: T? = null) {

    class Stop<T: ComponentData> : ComponentState<T>()
    class Processing<T: ComponentData>(componentData: T) : ComponentState<T>(componentData)
    class Done<T: ComponentData>(componentData: T) : ComponentState<T>(componentData)
    class Cancel<T: ComponentData>(componentData: T) : ComponentState<T>(componentData)
    class Failed<T: ComponentData>(componentData: T) : ComponentState<T>(componentData)
}