package com.moviles.pharmapp.view.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.Medication
import kotlinx.android.synthetic.main.fragment_medication_detail_dialog.*

class MedicationDetailDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.fullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medication_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val medicine = arguments?.getSerializable("medicine") as Medication

        tvMedicineTag.text = medicine.tag

        tvMedicationName.text = medicine.name

        tvIdMedicine.text = medicine.id

        tvMedicationInfo.text = medicine.description

        Glide.with(this)
            .load(R.drawable.ic_medicina)
            .apply(RequestOptions.circleCropTransform())
            .into(ivPictureMedicine)


    }

    override fun onStart() {

        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }



}