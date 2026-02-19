package com.example.smartfit.view.components

import android.opengl.Matrix
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.FilamentAsset

class BicepCurlAnimator(
    private val engine: Engine,
    private val asset: FilamentAsset
) {

    private val tm = engine.transformManager

    private var leftArm = -1
    private var leftForeArm = -1
    private var leftHand = -1

    private var rightArm = -1
    private var rightForeArm = -1
    private var rightHand = -1

    private val bind = HashMap<Int, FloatArray>()

    init {

        val instance = asset.getInstance()

        for (s in 0 until instance.getSkinCount()) {
            val joints = instance.getJointsAt(s)

            for (j in joints) {

                val name = asset.getName(j) ?: continue

                when (name) {
                    "mixamorig:LeftArm" -> leftArm = j
                    "mixamorig:LeftForeArm" -> leftForeArm = j
                    "mixamorig:LeftHand" -> leftHand = j

                    "mixamorig:RightArm" -> rightArm = j
                    "mixamorig:RightForeArm" -> rightForeArm = j
                    "mixamorig:RightHand" -> rightHand = j
                }

                val m = FloatArray(16)
                tm.getTransform(tm.getInstance(j), m)
                bind[j] = m
            }
        }
    }

    fun update(time: Float) {

        val curl = (kotlin.math.sin(time * 2f) * 0.5f + 0.5f) * 120f

        // ---------- LEFT ARM ----------
        rotate(leftArm, 80f, 1f,0f,0f)
        rotate(leftForeArm, curl, 0f,0f,1f)
        rotate(leftHand, -90f, 0f,1f,0f)

        // ---------- RIGHT ARM ----------
        rotate(rightArm, 80f, 1f,0f,0f)
        rotate(rightForeArm, curl, 0f,0f,-1f)
        rotate(rightHand, 90f, 0f,1f,0f)
    }

    private fun rotate(entity: Int, angle: Float, x:Float,y:Float,z:Float) {

        if (entity == -1) return
        val bindPose = bind[entity] ?: return

        val rot = FloatArray(16)
        val final = FloatArray(16)

        Matrix.setRotateM(rot,0,angle,x,y,z)
        Matrix.multiplyMM(final,0,bindPose,0,rot,0)

        tm.setTransform(tm.getInstance(entity), final)
    }
}
