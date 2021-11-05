package com.firmansyah.malangcamp.admin.ui.listbooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.BookingAdapter
import com.firmansyah.malangcamp.data.BookingData
import com.firmansyah.malangcamp.databinding.FragmentListBookingBinding
import com.firmansyah.malangcamp.model.Booking

class ListBookingFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentListBookingBinding? = null
    private lateinit var adapter: BookingAdapter
    private var list:ArrayList<Booking> = arrayListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentListBookingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(BookingData.listData)
        adapter = BookingAdapter(list)

        binding.rvListBooking.layoutManager = LinearLayoutManager(activity)
        binding.rvListBooking.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}