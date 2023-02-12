package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import io.github.dellisd.spatialk.geojson.serialization.jsonJoin
import io.github.dellisd.spatialk.geojson.serialization.jsonProp
import io.github.dellisd.spatialk.geojson.serialization.toBbox
import io.github.dellisd.spatialk.geojson.serialization.toPosition
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = GeometrySerializer::class)
class MultiPoint @JvmOverloads constructor(
    val coordinates: List<Position>,
    override val bbox: BoundingBox? = null
) : Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(::Position), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiPoint

        if (coordinates != other.coordinates) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }

    override fun json(): String =
        """{"type":"MultiPoint",${bbox.jsonProp()}"coordinates":${coordinates.jsonJoin(transform = Position::json)}}"""

    companion object {
        @JvmStatic
        public fun fromJson(json: String): MultiPoint = fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        public fun fromJsonOrNull(json: String): MultiPoint? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        public fun fromJson(json: JsonObject): MultiPoint {
            require(json.getValue("type").jsonPrimitive.content == "MultiPoint") {
                "Object \"type\" is not \"MultiPoint\"."
            }

            val coords = json.getValue("coordinates").jsonArray.map { it.jsonArray.toPosition() }
            val bbox = json["bbox"]?.jsonArray?.toBbox()

            return MultiPoint(coords, bbox)
        }
    }
}
