package com.moviles.pharmapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.view.adapter.MedicationAdapter
import com.moviles.pharmapp.view.adapter.MedicationListener
import com.moviles.pharmapp.viewmodel.MedicineViewModel
import kotlinx.android.synthetic.main.activity_scanner.*
import kotlinx.android.synthetic.main.fragment_medication.*
import kotlinx.android.synthetic.main.fragment_medication.view.*

class MedicationFragment: Fragment(),
    MedicationListener {

    private lateinit var medicineAdpater: MedicationAdapter
    private lateinit var viewModel: MedicineViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view =inflater.inflate(R.layout.fragment_medication, container, false)

        var addBtn = view.allMedsButton

        addBtn.setOnClickListener {

            findNavController().navigate(R.id.scanner_activity)
        }


        return view

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)
        viewModel.refresh()

        medicineAdpater =
            MedicationAdapter(this)

        rvMedicine.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = medicineAdpater
        }

        observeViewModel()
    }

    fun observeViewModel() {


        viewModel.listMedicine.observe(viewLifecycleOwner, Observer<List<Medication>> { medicine ->
            medicine.let {

                medicineAdpater.updateData(medicine)
            }


        })


    }

    override fun onMedicineClicked(medication: Medication, position: Int) {


        val bundle = bundleOf("medicine" to medication)
        findNavController().navigate(R.id.MedicineDetailFragmentDialog, bundle)


    }

    override fun onResume() {
        super.onResume()

        viewModel.refresh2()

        medicineAdpater =
            MedicationAdapter(this)

        rvMedicine.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = medicineAdpater
        }

        observeViewModel()
    }




}