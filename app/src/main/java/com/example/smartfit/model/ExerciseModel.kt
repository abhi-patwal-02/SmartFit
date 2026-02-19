package com.example.smartfit.model

data class Vector3(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

data class BonePose(
    val boneName: String,
    val rotation: Vector3
)

data class ExercisePose(
    val name: String,
    val bones: List<BonePose>
)

object ExercisePoses {

    // ==============================
    // BICEP CURL — START POSITION
    // ==============================
    val BICEP_CURL_START = ExercisePose(
        name = "Bicep Curl - Start",
        bones = listOf(

            // ---------- LEFT ARM ----------
            BonePose("mixamorig:LeftArm", Vector3(-80f, 0f, 0f)),          // drop arm from T-pose
            BonePose("mixamorig:LeftForeArm", Vector3(0f, 0f, 0f)),       // straight elbow
            BonePose("mixamorig:LeftHand", Vector3(0f, -90f, 0f)),        // wrist inward

            // ---------- RIGHT ARM ----------
            BonePose("mixamorig:RightArm", Vector3(-80f, 0f, 0f)),
            BonePose("mixamorig:RightForeArm", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:RightHand", Vector3(0f, 90f, 0f)),

            // ---------- LEFT FIST ----------
            BonePose("mixamorig:LeftHandThumb1", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandThumb2", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandThumb3", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandIndex1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandMiddle1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandRing1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandPinky1", Vector3(65f, 0f, 0f)),

            // ---------- RIGHT FIST ----------
            BonePose("mixamorig:RightHandThumb1", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandThumb2", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandThumb3", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandIndex1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandMiddle1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandRing1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandPinky1", Vector3(65f, 0f, 0f))
        )
    )


    // ==============================
    // BICEP CURL — PEAK POSITION
    // ==============================
    val BICEP_CURL_PEAK = ExercisePose(
        name = "Bicep Curl - Peak",
        bones = listOf(

            // ---------- LEFT ARM ----------
            BonePose("mixamorig:LeftArm", Vector3(80f, 0f, 0f)),          // shoulder fixed down
            BonePose("mixamorig:LeftForeArm", Vector3(120f, 0f, 0f)),     // curl elbow
            BonePose("mixamorig:LeftHand", Vector3(0f, -90f, 0f)),

            // ---------- RIGHT ARM ----------
            BonePose("mixamorig:RightArm", Vector3(80f, 0f, 0f)),
            BonePose("mixamorig:RightForeArm", Vector3(-120f, 0f, 0f)),
            BonePose("mixamorig:RightHand", Vector3(0f, 90f, 0f)),

            // ---------- FISTS SAME AS START ----------
            BonePose("mixamorig:LeftHandThumb1", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandThumb2", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandThumb3", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:LeftHandIndex1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandMiddle1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandRing1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:LeftHandPinky1", Vector3(65f, 0f, 0f)),

            BonePose("mixamorig:RightHandThumb1", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandThumb2", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandThumb3", Vector3(40f, 0f, 0f)),
            BonePose("mixamorig:RightHandIndex1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandMiddle1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandRing1", Vector3(65f, 0f, 0f)),
            BonePose("mixamorig:RightHandPinky1", Vector3(65f, 0f, 0f))
        )
    )


    // ==============================
    // SQUAT POSES (UNCHANGED — already correct)
    // ==============================
    val SQUAT_STANDING = ExercisePose(
        name = "Squat - Standing",
        bones = listOf(
            BonePose("mixamorig:Hips", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:Spine", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:RightUpLeg", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:RightLeg", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:LeftUpLeg", Vector3(0f, 0f, 0f)),
            BonePose("mixamorig:LeftLeg", Vector3(0f, 0f, 0f))
        )
    )

    val SQUAT_BOTTOM = ExercisePose(
        name = "Squat - Bottom",
        bones = listOf(
            BonePose("mixamorig:Hips", Vector3(30f, 0f, 0f)),
            BonePose("mixamorig:Spine", Vector3(20f, 0f, 0f)),
            BonePose("mixamorig:RightUpLeg", Vector3(-80f, 0f, 0f)),
            BonePose("mixamorig:RightLeg", Vector3(80f, 0f, 0f)),
            BonePose("mixamorig:LeftUpLeg", Vector3(-80f, 0f, 0f)),
            BonePose("mixamorig:LeftLeg", Vector3(80f, 0f, 0f))
        )
    )
}

enum class ExerciseType {
    BICEP_CURL,
    SQUAT
}

data class Exercise(
    val type: ExerciseType,
    val startPose: ExercisePose,
    val endPose: ExercisePose,
    val duration: Float = 2000f
)