package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.BarangAdapter
import com.firmansyah.malangcamp.databinding.FragmentBarangsewaBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class BarangSewaFragment : Fragment() {

    private lateinit var barangSewaViewModel: BarangSewaViewModel
    private var _binding: FragmentBarangsewaBinding? = null
    private lateinit var adapter: BarangAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var auth: FirebaseAuth

    private var jumlahSave: Int = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        barangSewaViewModel =
            ViewModelProvider(this).get(BarangSewaViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = Firebase.database
        databaseRef = database.getReference("barang")
        userRef = database.getReference("users/${auth.currentUser?.uid}/keranjang")

        _binding = FragmentBarangsewaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel()
    }

    private fun initAdapter() {
        adapter = BarangAdapter(arrayListOf(), false) { barang, jumlah ->
            uploadToFirebase(barang, jumlah)
        }
        binding.rvInfoBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvInfoBarang.adapter = adapter
    }

    private fun uploadToFirebase(barang: Barang, jumlah: Int) {
        if (jumlah != 0) {
            val model = Keranjang(
                barang.id,
                barang.nama,
                barang.harga,
                jumlah,
                barang.harga * jumlah
            )
            userRef.child(barang.id).setValue(model)
        } else {
            userRef.child(barang.id).get().addOnSuccessListener {
                it.ref.removeValue()
            }.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun viewModel() {
        with(barangSewaViewModel) {
            getListBarang(databaseRef)
            listBarang.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.setData(it)
                }
            })
            toast.observe(viewLifecycleOwner, {
                if (it != null) {
                    val toast = it.format(this)
                    Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}