package com.moviles.pharmapp.view.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.view.adapter.CalendarAdapter
import com.moviles.pharmapp.view.ui.activities.LoginActivity
import com.moviles.pharmapp.view.ui.activities.MainActivity
import com.moviles.pharmapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    private lateinit var calendarAdpater: CalendarAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var listener: IHomeListener

    interface IHomeListener {
        fun logOut()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IHomeListener) {
            listener = context
        } else {
            throw ClassCastException(
                context.toString() + " must implement OnDogSelected.")
        }
    }

    companion object {
        fun newInstance(): HomeFragment {
            val instance = HomeFragment()
            return instance
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarAdpater = CalendarAdapter()
        recycler_calendar.apply {
            val linear = LinearLayoutManager(context)
            layoutManager = linear
            adapter = calendarAdpater
        }

        ivLogout.setOnClickListener {
            listener?.logOut()
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.refresh()

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.liveUser.observe(viewLifecycleOwner, Observer<User> { user ->
            user.let {
                userName.text = user.name
            }
        })
    }


}