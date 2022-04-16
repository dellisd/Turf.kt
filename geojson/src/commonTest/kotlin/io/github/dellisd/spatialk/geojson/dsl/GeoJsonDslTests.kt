package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Position
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MagicNumber")
class GeoJsonDslTests {

    private val collectionDsl = featureCollection {
        val simplePoint = point(-75.0, 45.0, 100.0)
        // Point
        feature(geometry = simplePoint, id = "point1") {
            put("name", "Hello World")
        }
        // MultiPoint
        feature(geometry = multiPoint {
            +simplePoint
            +Position(45.0, 45.0)
            +Position(0.0, 0.0)
        })


        val simpleLine = lineString {
            +Position(45.0, 45.0)
            +Position(0.0, 0.0)
        }

        // LineString
        feature(geometry = simpleLine)

        // MultiLineString
        feature(geometry = multiLineString {
            +simpleLine
            lineString {
                +Position(44.4, 55.5)
                +Position(55.5, 66.6)
            }
        })

        val simplePolygon = polygon {
            ring {
                +simpleLine
                point(12.0, 12.0)
                complete()
            }
            ring {
                point(4.0, 4.0)
                point(2.0, 2.0)
                point(3.0, 3.0)
                complete()
            }
        }

        // Polygon
        feature(geometry = simplePolygon)

        feature(geometry = multiPolygon {
            +simplePolygon
            polygon {
                ring {
                    point(12.0, 0.0)
                    point(0.0, 12.0)
                    point(-12.0, 0.0)
                    point(5.0, 5.0)
                    complete()
                }
            }
        })

        feature(geometry = geometryCollection {
            +simplePoint
            +simpleLine
            +simplePolygon
        })
    }

    private val collectionJson =
        """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":
|[-75.0,45.0,100.0]},"id":"point1","properties":{"name":"Hello World"}},{"type":"Feature","geometry":
|{"type":"MultiPoint","coordinates":[[-75.0,45.0,100.0],[45.0,45.0],[0.0,0.0]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"LineString","coordinates":[[45.0,45.0],[0.0,0.0]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"MultiLineString","coordinates":[[[45.0,45.0],[0.0,0.0]],[[44.4,55.5],[55.5,66.6]]]},
|"properties":{}},{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[45.0,45.0],[0.0,0.0],[12.0,12.0],
|[45.0,45.0]],[[4.0,4.0],[2.0,2.0],[3.0,3.0],[4.0,4.0]]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"MultiPolygon","coordinates":[[[[45.0,45.0],[0.0,0.0],[12.0,12.0],[45.0,45.0]],[[4.0,4.0],[2.0,2.0],
|[3.0,3.0],[4.0,4.0]]],[[[12.0,0.0],[0.0,12.0],[-12.0,0.0],[5.0,5.0],[12.0,0.0]]]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"GeometryCollection","geometries":[{"type":"Point","coordinates":[-75.0,45.0,100.0]},
|{"type":"LineString","coordinates":[[45.0,45.0],[0.0,0.0]]},{"type":"Polygon","coordinates":[[[45.0,45.0],[0.0,0.0],
|[12.0,12.0],[45.0,45.0]],[[4.0,4.0],[2.0,2.0],[3.0,3.0],[4.0,4.0]]]}]},"properties":{}}]}""".trimMargin()

    @Test
    fun testDslConstruction() {
        assertEquals(FeatureCollection.fromJson(collectionJson), collectionDsl)
    }
}
