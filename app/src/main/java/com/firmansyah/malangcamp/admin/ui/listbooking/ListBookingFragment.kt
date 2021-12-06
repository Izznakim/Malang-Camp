package com.firmansyah.malangcamp.admin.ui.listbooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.BookingAdapter
import com.firmansyah.malangcamp.databinding.FragmentListBookingBinding
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListBookingFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentListBookingBinding? = null
    private lateinit var adapter: BookingAdapter
    private lateinit var listUser:ArrayList<Pelanggan>
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        database = Firebase.database
        ref = database.getReference("users")

        auth = Firebase.auth

        _binding = FragmentListBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        listUser = arrayListOf()

        ref.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach {
                if (!it.child("isAdmin").exists()) {
                    val pelanggan = it.getValue(Pelanggan::class.java)
                    if (pelanggan != null) {
                        listUser.add(pelanggan)
                    }
                }
            }
            adapter.setData(listUser)
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initAdapter() {
        adapter = BookingAdapter(arrayListOf()) { model ->
            deletePelanggan(model)
        }
        binding.rvListBooking.layoutManager = LinearLayoutManager(activity)
        binding.rvListBooking.adapter = adapter
    }

    private fun deletePelanggan(model: Pelanggan) {
        ref.child(model.id).get().addOnSuccessListener {
            it.ref.removeValue()
            Toast.makeText(activity, "${model.username} telah dihapus", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}