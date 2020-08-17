package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.LngLat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@ExperimentalSerializationApi
@InternalSerializationApi
internal object BoundingBoxSerializer : KSerializer<BoundingBox> {
    private const val ARRAY_SIZE_2D = 4
    private const val ARRAY_SIZE_3D = 6

    override val descriptor: SerialDescriptor
        get() = buildSerialDescriptor("BoundingBox", StructureKind.LIST)

    @Suppress("MagicNumber")
    override fun deserialize(decoder: Decoder): BoundingBox {
        val input = decoder as? JsonDecoder ?: throw SerializationException("This class can only be loaded from JSON")
        val array = input.decodeJsonElement().jsonArray

        return when (array.size) {
            ARRAY_SIZE_2D -> {
                BoundingBox(
                    LngLat(array[0].jsonPrimitive.double, array[1].jsonPrimitive.double),
                    LngLat(array[2].jsonPrimitive.double, array[3].jsonPrimitive.double)
                )
            }
            ARRAY_SIZE_3D -> {
                BoundingBox(
                    LngLat(
                        array[0].jsonPrimitive.double,
                        array[1].jsonPrimitive.double,
                        array.getOrNull(2)?.jsonPrimitive?.double
                    ),
                    LngLat(
                        array[3].jsonPrimitive.double,
                        array[4].jsonPrimitive.double,
                        array.getOrNull(5)?.jsonPrimitive?.double
                    )
                )
            }
            else -> {
                throw SerializationException("Expected array of size 4 or 6. Got array of size ${array.size}")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: BoundingBox) {
        val output = encoder as? JsonEncoder ?: throw SerializationException("This class can only be saved as JSON")

        println(value)
        val includeAltitudes = value.southwest.altitude != null && value.northeast.altitude != null
        val array = buildJsonArray {
            add((value.southwest.longitude as Double?))
            add(value.southwest.latitude as Double?)
            if (includeAltitudes) {
                add(value.southwest.altitude)
            }

            add(value.northeast.longitude as Double?)
            add(value.northeast.latitude as Double?)
            if (includeAltitudes) {
                add(value.northeast.altitude)
            }
        }

        output.encodeJsonElement(array)
    }
}
