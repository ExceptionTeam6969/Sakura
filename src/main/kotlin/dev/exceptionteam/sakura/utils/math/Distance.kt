package dev.exceptionteam.sakura.utils.math

import net.minecraft.entity.Entity
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import kotlin.math.sqrt

fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    return length(x2 - x1, y2 - y1)
}

fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Double {
    return length(x2 - x1, y2 - y1)
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return length(x2 - x1, y2 - y1)
}

fun length(x: Double, y: Double): Double {
    return sqrt(lengthSq(x, y))
}

fun length(x: Float, y: Float): Double {
    return sqrt(lengthSq(x.toDouble(), y.toDouble()))
}

fun length(x: Int, y: Int): Double {
    return sqrt(lengthSq(x.toDouble(), y.toDouble()))
}

fun distanceSq(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    return lengthSq(x2 - x1, y2 - y1)
}

fun distanceSq(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    return lengthSq(x2 - x1, y2 - y1)
}

fun distanceSq(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    return lengthSq(x2 - x1, y2 - y1)
}

fun lengthSq(x: Double, y: Double): Double {
    return x.sq + y.sq
}

fun lengthSq(x: Float, y: Float): Float {
    return x.sq + y.sq
}

fun lengthSq(x: Int, y: Int): Int {
    return x.sq + y.sq
}


fun distance(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
    return sqrt(distanceSq(x1, y1, z1, x2, y2, z2))
}

fun distance(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Double {
    return sqrt(distanceSq(x1, y1, z1, x2, y2, z2).toDouble())
}

fun distance(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Double {
    return sqrt(distanceSq(x1, y1, z1, x2, y2, z2).toDouble())
}

fun length(x: Double, y: Double, z: Double): Double {
    return sqrt(lengthSq(x, y, z))
}

fun length(x: Float, y: Float, z: Float): Double {
    return sqrt(lengthSq(x.toDouble(), y.toDouble(), z.toDouble()))
}

fun length(x: Int, y: Int, z: Int): Double {
    return sqrt(lengthSq(x.toDouble(), y.toDouble(), z.toDouble()))
}

fun distanceSq(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double {
    return lengthSq(x2 - x1, y2 - y1, z2 - z1)
}

fun distanceSq(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Float {
    return lengthSq(x2 - x1, y2 - y1, z2 - z1)
}

fun distanceSq(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Int {
    return lengthSq(x2 - x1, y2 - y1, z2 - z1)
}

fun lengthSq(x: Double, y: Double, z: Double): Double {
    return x.sq + y.sq + z.sq
}

fun lengthSq(x: Float, y: Float, z: Float): Float {
    return x.sq + y.sq + z.sq
}

fun lengthSq(x: Int, y: Int, z: Int): Int {
    return x.sq + y.sq + z.sq
}

/* Vector */

fun Vec3i.distanceToCenter(x: Double, y: Double, z: Double): Double {
    return distance(this.x + 0.5, this.y + 0.5, this.z + 0.5, x, y, z)
}

fun Vec3i.distanceSqToCenter(x: Double, y: Double, z: Double): Double {
    return distanceSq(this.x + 0.5, this.y + 0.5, this.z + 0.5, x, y, z)
}


fun Vec3i.distanceToCenter(vec3d: Vec3d): Double {
    return distance(this.x + 0.5, this.y + 0.5, this.z + 0.5, vec3d.x, vec3d.y, vec3d.z)
}

fun Vec3i.distanceSqToCenter(vec3d: Vec3d): Double {
    return distanceSq(this.x + 0.5, this.y + 0.5, this.z + 0.5, vec3d.x, vec3d.y, vec3d.z)
}


fun Vec3i.distanceTo(x: Int, y: Int, z: Int): Double {
    return distance(this.x, this.y, this.z, x, y, z)
}

fun Vec3i.distanceSqTo(x: Int, y: Int, z: Int): Int {
    return distanceSq(this.x, this.y, this.z, x, y, z)
}


fun Vec3i.distanceTo(vec3i: Vec3i): Double {
    return distance(this.x, this.y, this.z, vec3i.x, vec3i.y, vec3i.z)
}

fun Vec3i.distanceSqTo(vec3i: Vec3i): Int {
    return distanceSq(this.x, this.y, this.z, vec3i.x, vec3i.y, vec3i.z)
}


fun Vec3d.distanceTo(x: Double, y: Double, z: Double): Double {
    return distance(this.x, this.y, this.z, x, y, z)
}

fun Vec3d.distanceSqTo(x: Double, y: Double, z: Double): Double {
    return distanceSq(this.x, this.y, this.z, x, y, z)
}


fun Vec3d.distanceTo(vec3d: Vec3d): Double {
    return distanceTo(vec3d.x, vec3d.y, vec3d.z)
}

fun Vec3d.distanceSqTo(vec3d: Vec3d): Double {
    return distanceSqTo(vec3d.x, vec3d.y, vec3d.z)
}


fun Vec3d.distanceTo(entity: Entity): Double {
    return distanceTo(entity.x, entity.y, entity.z)
}

fun Vec3d.distanceSqTo(entity: Entity): Double {
    return distanceSqTo(entity.x, entity.y, entity.z)
}


fun Vec3d.distanceToCenter(x: Int, y: Int, z: Int): Double {
    return distance(this.x, this.y, this.z, x + 0.5, y + 0.5, z + 0.5)
}

fun Vec3d.distanceSqToCenter(x: Int, y: Int, z: Int): Double {
    return distanceSq(this.x, this.y, this.z, x + 0.5, y + 0.5, z + 0.5)
}


fun Vec3d.distanceToCenter(vec3i: Vec3i): Double {
    return distanceToCenter(vec3i.x, vec3i.y, vec3i.z)
}

fun Vec3d.distanceSqToCenter(vec3i: Vec3i): Double {
    return distanceSqToCenter(vec3i.x, vec3i.y, vec3i.z)
}


fun Entity.distanceTo(x: Double, y: Double, z: Double): Double {
    return distance(this.x, this.y, this.z, x, y, z)
}

fun Entity.distanceSqTo(x: Double, y: Double, z: Double): Double {
    return distanceSq(this.x, this.y, this.z, x, y, z)
}


fun Entity.distanceTo(vec3d: Vec3d): Double {
    return distanceTo(vec3d.x, vec3d.y, vec3d.z)
}

fun Entity.distanceSqTo(vec3d: Vec3d): Double {
    return distanceSqTo(vec3d.x, vec3d.y, vec3d.z)
}


fun Entity.distanceTo(entity: Entity): Double {
    return distanceTo(entity.x, entity.y, entity.z)
}

fun Entity.distanceSqTo(entity: Entity): Double {
    return distanceSqTo(entity.x, entity.y, entity.z)
}


fun Entity.distanceToCenter(x: Int, y: Int, z: Int): Double {
    return distance(this.x, this.y, this.z, x + 0.5, y + 0.5, z + 0.5)
}

fun Entity.distanceSqToCenter(x: Int, y: Int, z: Int): Double {
    return distanceSq(this.x, this.y, this.z, x + 0.5, y + 0.5, z + 0.5)
}


fun Entity.distanceToCenter(vec3i: Vec3i): Double {
    return distanceToCenter(vec3i.x, vec3i.y, vec3i.z)
}

fun Entity.distanceSqToCenter(vec3i: Vec3i): Double {
    return distanceSqToCenter(vec3i.x, vec3i.y, vec3i.z)
}

fun Entity.hDistanceTo(x: Double, y: Double): Double {
    return distance(this.x, this.y, x, y)
}

fun Entity.hDistanceSqTo(x: Double, y: Double): Double {
    return distanceSq(this.x, this.y, x, y)
}

fun Entity.hDistanceToCenter(x: Int, y: Int): Double {
    return distance(this.x, this.y, x + 0.5, y + 0.5)
}

fun Entity.hDistanceSqToCenter(x: Int, y: Int): Double {
    return distanceSq(this.x, this.y, x + 0.5, y + 0.5)
}

fun Entity.hDistanceToCenter(chunkPos: ChunkPos): Double {
    return hDistanceToCenter(chunkPos.x * 16 + 8, chunkPos.z * 16 + 8)
}

fun Entity.hDistanceSqToCenter(chunkPos: ChunkPos): Double {
    return hDistanceSqToCenter(chunkPos.x * 16 + 8, chunkPos.z * 16 + 8)
}