package com.moviles.pharmapp.view.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.moviles.pharmapp.R
import com.moviles.pharmapp.viewmodel.MedicineViewModel
import java.io.File
import java.util.concurrent.ExecutorService
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.fragment_scanner.*
import kotlinx.android.synthetic.main.fragment_scanner.view.*
import java.util.concurrent.Executors


class ScannerFragment: Fragment(){

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var tvAction: TextView? = null

    //private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var faceTracking: ImageAnalysis? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService


    private lateinit var viewModel: MedicineViewModel



    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    }




    fun Fragment.toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == ScannerFragment.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = ScannerFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view =inflater.inflate(R.layout.fragment_scanner, container, false)

        tvAction = view.tvCode
        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), ScannerFragment.REQUIRED_PERMISSIONS, ScannerFragment.REQUEST_CODE_PERMISSIONS
            )
        }

        // Setup the listener for take photo button
        //camera_capture_button.setOnClickListener { takePhoto() }



        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()

    }

    fun startCamera() {


        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

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
                Log.e("failure", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    inner class ImageProcessor : ImageAnalysis.Analyzer {



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

                            requireActivity().runOnUiThread {

                                tvCode.text = code
                            }

                            if (!code.isEmpty()) {

                                val medicine = viewModel.findMedicine(code)

                                Log.i("Medicina",medicine.name+"SCANEEEEEER")

                                if (!medicine.name.equals("")) {

                                    val bundle = bundleOf("medicine" to medicine)

                                    findNavController().navigate(R.id.AddMedicineDetailFragmentDialog,bundle)

                                    stopCamera()
                                }

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