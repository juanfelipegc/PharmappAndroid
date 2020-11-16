package com.moviles.pharmapp.view.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.viewmodel.MedicineViewModel
import kotlinx.android.synthetic.main.fragment_add_medicine_detail_dialog.*
import kotlinx.android.synthetic.main.fragment_add_medicine_detail_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class AddMedicationDetailDialog: DialogFragment() {


    private lateinit var viewModel: MedicineViewModel

    var medicine = Medication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.fullScreenDialogStyle)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_add_medicine_detail_dialog, container, false)

        var addBtn = view.addMedBtn


        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)
        medicine = arguments?.getSerializable("medicine") as Medication
        addBtn.setOnClickListener {
            viewModel.addMedicine(medicine)
//            findNavController().navigate(R.id.medication_fragment_user)
        }

        return view



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.i("Medicina",medicine.name)
        tvMedicineTag.text = medicine.tag

        tvMedicationName.text = medicine.name

        tvIdMedicine.text = medicine.id

        tvMedicationInfo.text = medicine.description

        Glide.with(this)
            .load(R.drawable.ic_medicina_negro)
            .apply(RequestOptions.circleCropTransform())
            .into(ivPictureMedicine)

        observeViewModel()


    }

    fun observeViewModel() {
        viewModel.liveinteractionRes.observe(viewLifecycleOwner, Observer<String> { result ->
            result.let {
                if (result == "Risk"){

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(R.string.dialoTitlegRisk)
                    builder.setMessage(R.string.dialogMessageRisk)
                    builder.setNeutralButton("Ok") {DialogInterface, which ->
                        viewModel.addMedicineRisky(medicine)
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                else if (result == "Added"){
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(R.string.dialoTitlegRisk)
                    builder.setMessage(R.string.dialogMessageRisk)
                    builder.setNeutralButton("Ok") {DialogInterface, which ->

                    }
                    val alertDialog = builder.create()
                    alertDialog.show()

                    findNavController().navigate(R.id.medication_fragment_user)
                }
            }
        })
        viewModel.liveMedicineAdded.observe(viewLifecycleOwner, Observer<String> { result ->
            result.let {
                if (result == "Added"){
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(R.string.dialoTitlegAdd)
                    builder.setMessage(R.string.dialogMessageAdd)
                    builder.setNeutralButton("Ok") {DialogInterface, which ->

                    }
                    val alertDialog = builder.create()
                    alertDialog.show()

                    findNavController().navigate(R.id.medication_fragment_user)
                }
            }
        })
    }

    override fun onStart() {

        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }



}