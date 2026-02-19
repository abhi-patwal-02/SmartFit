package com.example.smartfit.view.components

import android.opengl.Matrix
import android.view.Choreographer
import android.view.SurfaceView
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.smartfit.model.Exercise
import com.example.smartfit.model.ExerciseType
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer

// ---------- GLOBAL STATE ----------
private var currentExercise: Exercise? = null
private var curlAnimator: BicepCurlAnimator? = null

private var engineRef: Engine? = null
private var rendererRef: Renderer? = null
private var sceneRef: Scene? = null
private var viewRef: View? = null
private var cameraRef: Camera? = null
private var swapChainRef: SwapChain? = null
private var choreographerRef: Choreographer? = null
private var frameCallbackRef: Choreographer.FrameCallback? = null
private var assetRef: FilamentAsset? = null

private var pivotX = 0f
private var pivotY = 0f
private var pivotZ = 0f


// ---------- PUBLIC API ----------
fun setExercise(ex: Exercise?) {
    currentExercise = ex
}

fun destroyFilamentResources() {

    frameCallbackRef?.let {
        choreographerRef?.removeFrameCallback(it)
    }
    frameCallbackRef = null

    assetRef?.let {
        sceneRef?.removeEntities(it.entities)
        val materials = UbershaderProvider(engineRef!!)
        val loader = AssetLoader(engineRef!!, materials, EntityManager.get())
        loader.destroyAsset(it)
    }
    assetRef = null

    engineRef?.let {
        rendererRef?.let { r -> it.destroyRenderer(r) }
        sceneRef?.let { s -> it.destroyScene(s) }
        swapChainRef?.let { sc -> it.destroySwapChain(sc) }
        it.destroy()
    }

    engineRef = null
    rendererRef = null
    sceneRef = null
    viewRef = null
    cameraRef = null
    swapChainRef = null
    choreographerRef = null
}

// ---------- COMPOSABLE ----------
@Composable
fun Model3DViewer(
    modelPath: String,
    modifier: Modifier = Modifier,
    exercise: Exercise? = null
) {

    val context = LocalContext.current

    LaunchedEffect(exercise) {
        currentExercise = exercise
    }

    var yaw by remember { mutableStateOf(0f) }
    var pitch by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(0.5f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    yaw += pan.x * 0.5f
                    pitch += pan.y * 0.5f
                    scale = (scale * zoom).coerceIn(0.2f, 5f)
                }
            }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                SurfaceView(ctx).apply {
                    setupFilament(this, context, modelPath)
                }
            }
        )
    }

    currentYaw = yaw
    currentPitch = pitch
    currentScale = scale
}

// ---------- TRANSFORM STATE ----------
private var currentYaw = 0f
private var currentPitch = 0f
private var currentScale = 0.5f

// ---------- FILAMENT SETUP ----------
private fun setupFilament(
    surfaceView: SurfaceView,
    context: android.content.Context,
    modelPath: String
) {

    val engine = engineRef ?: Engine.create().also { engineRef = it }
    val renderer = rendererRef ?: engine.createRenderer().also { rendererRef = it }
    val scene = sceneRef ?: engine.createScene().also { sceneRef = it }
    val view = viewRef ?: engine.createView().also { viewRef = it }
    val camera = cameraRef ?: engine.createCamera(engine.entityManager.create()).also { cameraRef = it }

    var swapChain = swapChainRef

    val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    uiHelper.renderCallback = object : UiHelper.RendererCallback {

        override fun onNativeWindowChanged(surface: android.view.Surface) {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = engine.createSwapChain(surface)
            swapChainRef = swapChain
        }

        override fun onDetachedFromSurface() {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = null
            swapChainRef = null
        }

        override fun onResized(width: Int, height: Int) {
            view.viewport = Viewport(0, 0, width, height)
            camera.setProjection(
                45.0,
                width.toDouble() / height,
                0.1,
                50.0,
                Camera.Fov.VERTICAL
            )
        }
    }

    uiHelper.attachTo(surfaceView)

    view.camera = camera
    view.scene = scene

    camera.lookAt(
        0.0, 2.0, 4.0,
        0.0, 0.0, 0.0,
        0.0, 1.0, 0.0
    )

    // ---------- LOAD MODEL ----------
    if (assetRef == null) {

        val buffer = context.assets.open(modelPath).use {
            val bytes = it.readBytes()
            ByteBuffer.allocateDirect(bytes.size).apply {
                put(bytes)
                rewind()
            }
        }

        val materials = UbershaderProvider(engine)
        val loader = AssetLoader(engine, materials, EntityManager.get())

        val asset = loader.createAsset(buffer)

        asset?.let { a ->
            val resourceLoader = ResourceLoader(engine)
            resourceLoader.loadResources(a)

            scene.addEntities(a.entities)

            assetRef = a

            // ⭐ USE THE CORRECT ANIMATOR
            curlAnimator = BicepCurlAnimator(engine, a)

            val bbox = a.boundingBox
            val c = bbox.center

// store globally
            pivotX = c[0]
            pivotY = c[1] * 0.3f   // chest is usually slightly below center
            pivotZ = c[2]
        }

        scene.indirectLight = IndirectLight.Builder()
            .intensity(30000f)
            .build(engine)

        val sun = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .intensity(100000f)
            .direction(0f, -1f, 0f)
            .build(engine, sun)
        scene.addEntity(sun)
    }

    // ---------- RENDER LOOP ----------
    val choreographer = choreographerRef ?: Choreographer.getInstance().also { choreographerRef = it }

    if (frameCallbackRef == null) {

        val frameCallback = object : Choreographer.FrameCallback {

            override fun doFrame(frameTimeNanos: Long) {

                choreographer.postFrameCallback(this)

                val asset = assetRef ?: return
                val engine = engineRef ?: return

                // ⭐ ROOT TRANSFORM
                val tm = engine.transformManager
                val root = asset.root

                val matrix = FloatArray(16)
                Matrix.setIdentityM(matrix, 0)

// 1️⃣ move torso to origin
                Matrix.translateM(matrix, 0, -pivotX, -pivotY, -pivotZ)

// 2️⃣ rotate around torso
                Matrix.rotateM(matrix, 0, currentYaw, 0f, 1f, 0f)
                Matrix.rotateM(matrix, 0, currentPitch, 1f, 0f, 0f)

// 3️⃣ scale around torso
                Matrix.scaleM(matrix, 0, currentScale, currentScale, currentScale)

// 4️⃣ move back
                Matrix.translateM(matrix, 0, pivotX, pivotY, pivotZ)


                tm.setTransform(root, matrix)

                swapChain?.let { chain ->
                    if (renderer.beginFrame(chain, frameTimeNanos)) {

                        // ⭐ CORRECT CURL ANIMATION
                        currentExercise?.let { ex ->
                            if (ex.type == ExerciseType.BICEP_CURL) {
                                val timeSec = (frameTimeNanos / 1_000_000_000.0).toFloat()
                                curlAnimator?.update(timeSec)
                                asset.getInstance().getAnimator().updateBoneMatrices()
                            }
                        }

                        renderer.render(view)
                        renderer.endFrame()
                    }
                }
            }
        }

        frameCallbackRef = frameCallback
        choreographer.postFrameCallback(frameCallback)
    }
}
