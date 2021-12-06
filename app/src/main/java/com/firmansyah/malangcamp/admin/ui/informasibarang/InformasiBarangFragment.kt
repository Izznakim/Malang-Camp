package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.InfoBarangAdapter
import com.firmansyah.malangcamp.databinding.FragmentInformasiBarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class InformasiBarangFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentInformasiBarangBinding? = null
    private lateinit var adapter: InfoBarangAdapter
    private lateinit var listBarang: ArrayList<Barang>
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = Firebase.database
        databaseRef = database.getReference("barang")

        _binding = FragmentInformasiBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        listBarang= arrayListOf()

        databaseRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach {
                val barang = it.getValue(Barang::class.java)
                if (barang != null) {
                    listBarang.add(barang)
                }
            }
            adapter.setData(listBarang)
        }.addOnFailureListener {
            Toast.makeText(activity,it.message, Toast.LENGTH_SHORT).show()
        }

        binding.fabAdd.setOnClickListener {
            val submitBarangFragment=SubmitBarangFragment()
            val mFragmentManager=childFragmentManager
            submitBarangFragment.show(mFragmentManager,SubmitBarangFragment::class.java.simpleName)
        }
    }

    private fun initAdapter() {
        adapter = InfoBarangAdapter(arrayListOf()) { model ->
            deleteBarang(model)
        }
        binding.rvInfoBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvInfoBarang.adapter = adapter
    }

    private fun deleteBarang(model: Barang) {
        databaseRef.child(model.id).get().addOnSuccessListener {
            it.ref.removeValue()
            storageRef.child("${model.id}.jpg").delete()
            Toast.makeText(activity, "${model.nama} telah dihapus", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}