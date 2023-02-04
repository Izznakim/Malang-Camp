package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.BookingAdapter
import com.firmansyah.malangcamp.databinding.FragmentRiwayatpemesananBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//  Halaman daftar riwayat
class RiwayatPemesananFragment : Fragment() {

    private lateinit var riwayatPemesananViewModel: RiwayatPemesananViewModel
    private lateinit var adapter: BookingAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentRiwayatpemesananBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        riwayatPemesananViewModel =
            ViewModelProvider(this)[RiwayatPemesananViewModel::class.java]

        auth = Firebase.auth

        database = Firebase.database
        ref = database.getReference("pembayaran")

        _binding = FragmentRiwayatpemesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel()
    }

    private fun viewModel() {
        with(riwayatPemesananViewModel) {
            getListRiwayat(ref,auth)
            listRiwayat.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.setData(it)
                }
            }
            toast.observe(viewLifecycleOwner) {
                if (it != null) {
                    val toast = it.format(this)
                    android.widget.Toast.makeText(
                        activity,
                        toast,
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = BookingAdapter(arrayListOf())
        binding.rvListRiwayat.layoutManager = LinearLayoutManager(activity)
        binding.rvListRiwayat.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}