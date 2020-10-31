package com.moviles.pharmapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.R
import com.moviles.pharmapp.view.adapter.CalendarAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {

    private lateinit var calendarAdpater: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarAdpater = CalendarAdapter()
        recycler_calendar.apply {
            val linear = LinearLayoutManager(context)
            layoutManager = linear
            adapter = calendarAdpater
        }
    }
}