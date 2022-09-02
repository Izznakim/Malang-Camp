package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.BarangAdapter
import com.firmansyah.malangcamp.databinding.FragmentInformasiBarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

//  Halaman daftar barang
class InformasiBarangFragment : Fragment() {

    private lateinit var informasiBarangViewModel: InformasiBarangViewModel
    private var _binding: FragmentInformasiBarangBinding? = null
    private lateinit var adapter: BarangAdapter
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
        informasiBarangViewModel =
            ViewModelProvider(this)[InformasiBarangViewModel::class.java]

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
        viewModel()

        binding.fabAdd.setOnClickListener {
            val submitBarangFragment=SubmitBarangFragment()
            val mFragmentManager=childFragmentManager
            submitBarangFragment.show(mFragmentManager,SubmitBarangFragment::class.java.simpleName)
        }
    }

    private fun initAdapter() {
        adapter = BarangAdapter(arrayListOf(), databaseRef,true) { model ->
            deleteBarang(model)
        }
        binding.rvInfoBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvInfoBarang.adapter = adapter
    }

    private fun viewModel() {
        with(informasiBarangViewModel) {
            getListBarang(databaseRef)
            listBarang.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.setData(it)
                }
            }
            toast.observe(viewLifecycleOwner) {
                if (it != null) {
                    val toast = it.format(this)
                    Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteBarang(model: Barang) {
        databaseRef.child(model.id).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                storageRef.child("${model.id}.jpg").delete()
                Toast.makeText(activity, "${model.nama} telah dihapus", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}