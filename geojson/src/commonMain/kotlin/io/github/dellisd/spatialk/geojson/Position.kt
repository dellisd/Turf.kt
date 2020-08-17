package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.PositionSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmSynthetic

/**
 * A position is the fundamental geometry construct.
 *
 * In JSON, a position is an array of numbers. There MUST be two or more elements.
 * The first two elements are [longitude] and [latitude], or easting and northing,
 * precisely in that order using decimal numbers.
 * [Altitude][altitude] or elevation MAY be included as an optional third element.
 *
 * When serialized, the [latitude], [longitude], and [altitude] (if present) will be represented as an array.
 *
 * ```kotlin
 * LngLat(longitude = -75.0, latitude = 45.0)
 * ```
 * will be serialized as
 * ```json
 * [-75.0,45.0]
 * ```
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.1.1">
 *     https://tools.ietf.org/html/rfc7946#section-3.1.1</a>
 * @see PositionSerializer
 *
 * @property latitude The latitude value of this position (or northing value for projected coordinates)
 * @property longitude The longitude value of this position (or easting value for projected coordinates)
 * @property altitude Optionally, an altitude or elevation for this position
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable(with = PositionSerializer::class)
interface Position {
    val latitude: Double
    val longitude: Double
    val altitude: Double?

    /**
     * Component function for getting the [longitude]
     * @return [longitude]
     */
    @JvmSynthetic
    operator fun component1(): Double

    /**
     * Component function for getting the [latitude]
     * @return [latitude]
     */
    @JvmSynthetic
    operator fun component2(): Double

    /**
     * Component function for getting the [altitude]
     * @return [altitude]
     */
    @JvmSynthetic
    operator fun component3(): Double?

    companion object
}
