package ir.hrka.kotlin.core.utilities

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ir.hrka.kotlin.core.Constants.DEFAULT_VERSION_CODE
import ir.hrka.kotlin.domain.entities.db.Topic
import java.lang.reflect.Type

class TopicDeserializer : JsonDeserializer<Topic> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Topic {
        val jsonObject = json.asJsonObject

        return Topic(
            id = jsonObject["topic_id"]?.asInt ?: -1,
            courseName = jsonObject["course_name"]?.asString ?: "Unknown",
            topicTitle = jsonObject["topic_title"]?.asString ?: "Corrupted topic",
            fileName = jsonObject["topic_file_name"]?.asString ?: "",
            hasVisualizer = jsonObject["has_visualizer"]?.asBoolean ?: false,
            isActive = jsonObject["is_active"]?.asBoolean ?: false,
            visualizerDestination = jsonObject["visualizer_destination"]?.asString ?: "",
            hasUpdate = jsonObject["has_update"]?.asBoolean ?: true,
            visualizerVersionCode = jsonObject["visualizer_version_code"]?.asInt ?: 1000000000
        )
    }
}