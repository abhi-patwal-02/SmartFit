package com.example.smartfit.view.components

import com.example.smartfit.model.ExercisePose
import com.example.smartfit.model.Vector3
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.FilamentAsset
import kotlin.math.cos
import kotlin.math.sin

class PoseAnimator(
    private val engine: Engine,
    private val asset: FilamentAsset
) {

    private val tm = engine.transformManager

    private val entityMap: Map<String, Int> = buildMap {
        asset.entities.forEach {
            asset.getName(it)?.let { name ->
                put(name, it)
            }
        }
    }

    private val bindPoseMap = mutableMapOf<Int, FloatArray>()

    init {
        entityMap.values.forEach { entity ->
            val inst = tm.getInstance(entity)
            if (inst != 0) {
                val base = FloatArray(16)
                tm.getTransform(inst, base)
                bindPoseMap[entity] = base
            }
        }
    }

    fun applyInterpolatedPose(
        start: ExercisePose,
        end: ExercisePose,
        progress: Float
    ) {
        val t = progress.coerceIn(0f, 1f)

        for (bone in start.bones) {

            val endBone = end.bones.find { it.boneName == bone.boneName } ?: continue
            val entity = entityMap[bone.boneName] ?: continue
            val inst = tm.getInstance(entity)
            if (inst == 0) continue

            val bind = bindPoseMap[entity] ?: continue
            val rot = lerp(bone.rotation, endBone.rotation, t)

            val rotMatrix = FloatArray(16)
            buildRotationMatrix(rot, rotMatrix)

            val result = FloatArray(16)
            multiplyMM(result, bind, rotMatrix)

            tm.setTransform(inst, result)
        }
    }

    private fun lerp(a: Vector3, b: Vector3, t: Float) =
        Vector3(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        )

    private fun buildRotationMatrix(rot: Vector3, out: FloatArray) {

        val rx = Math.toRadians(rot.x.toDouble()).toFloat()
        val ry = Math.toRadians(rot.y.toDouble()).toFloat()
        val rz = Math.toRadians(rot.z.toDouble()).toFloat()

        val cx = cos(rx); val sx = sin(rx)
        val cy = cos(ry); val sy = sin(ry)
        val cz = cos(rz); val sz = sin(rz)

        out[0] = cy * cz
        out[1] = sx * sy * cz - cx * sz
        out[2] = cx * sy * cz + sx * sz
        out[3] = 0f

        out[4] = cy * sz
        out[5] = sx * sy * sz + cx * cz
        out[6] = cx * sy * sz - sx * cz
        out[7] = 0f

        out[8] = -sy
        out[9] = sx * cy
        out[10] = cx * cy
        out[11] = 0f

        out[12] = 0f
        out[13] = 0f
        out[14] = 0f
        out[15] = 1f
    }

    private fun multiplyMM(
        result: FloatArray,
        lhs: FloatArray,
        rhs: FloatArray
    ) {
        for (i in 0..3) {
            val ai0 = lhs[i]
            val ai1 = lhs[4 + i]
            val ai2 = lhs[8 + i]
            val ai3 = lhs[12 + i]

            result[i]      = ai0 * rhs[0] + ai1 * rhs[1] + ai2 * rhs[2] + ai3 * rhs[3]
            result[4 + i]  = ai0 * rhs[4] + ai1 * rhs[5] + ai2 * rhs[6] + ai3 * rhs[7]
            result[8 + i]  = ai0 * rhs[8] + ai1 * rhs[9] + ai2 * rhs[10] + ai3 * rhs[11]
            result[12 + i] = ai0 * rhs[12] + ai1 * rhs[13] + ai2 * rhs[14] + ai3 * rhs[15]
        }
    }
}
