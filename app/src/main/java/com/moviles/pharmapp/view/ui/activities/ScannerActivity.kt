package com.moviles.pharmapp.view.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.moviles.pharmapp.R
import kotlinx.android.synthetic.main.activity_scanner.*


typealias LumaListener = (luma: Double) -> Unit

class ScannerActivity : AppCompatActivity() {
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var tvAction: TextView? = null

    //private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var faceTracking: ImageAnalysis? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService





    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)


        tvAction = findViewById<TextView>(R.id.tvCode)


        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Setup the listener for take photo button
        //camera_capture_button.setOnClickListener { takePhoto() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {


        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                .build()

            imageCapture = ImageCapture.Builder().build()



            faceTracking = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()



            faceTracking!!.setAnalyzer(cameraExecutor, ImageProcessor())


            // Select back camera
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()


                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, faceTracking, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    val uris = arrayOf(savedUri.toString())

                    scanFile(photoFile.absolutePath)
                }
            })
    }

    private fun scanFile(path: String) {
        MediaScannerConnection.scanFile(
            this@ScannerActivity, arrayOf(path), null
        ) { path, uri -> Log.i("TAG", "Finished scanning $path") }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {

        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }


    inner class ImageProcessor : ImageAnalysis.Analyzer {

        val realTimeOpts = FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()



        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()



        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {





            val mediaImage = imageProxy.image

            if (mediaImage != null) {

                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                // ...

                val detector = FaceDetection.getClient(realTimeOpts)


                val scanner = BarcodeScanning.getClient()

                val result = scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        // Task completed successfully
                        // ...
                        for (barcode in barcodes)  {

                            Log.v("data", barcode.toString())
                            val bounds = barcode.boundingBox
                            val corners = barcode.cornerPoints

                            val rawValue = barcode.rawValue

                            val valueType = barcode.valueType


                            val code = rawValue.toString()

                            Log.v("data", code)
                            Log.v("data", valueType.toString())

                            runOnUiThread {

                                tvCode.text = code
                            }

                            when (valueType) {

                                Barcode.FORMAT_ALL_FORMATS -> {




                                }

                            }

                        }
                        Log.v("data", "cerrando")

                        //Let the frame go so another frame can be analyzed.
                        imageProxy.close()
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        Log.v("data", "fallo")
                        e.printStackTrace()
                    }

            }

        }
    }


}


