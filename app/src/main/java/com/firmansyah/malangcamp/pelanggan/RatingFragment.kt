package com.firmansyah.malangcamp.pelanggan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentRatingBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.pelanggan.ui.barangsewa.DetailBarangSewaFragment.Companion.EXTRA_BARANG
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RatingFragment : DialogFragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var barangRef: DatabaseReference

    private var _binding: FragmentRatingBinding? = null
    private var barangSewa: ArrayList<Keranjang>? = null
    private var nilai = 0
    private val listNilai: ArrayList<Int> = arrayListOf()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance()
        barangRef = database.getReference("barang")

        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            barangSewa = arguments?.getParcelableArrayList(EXTRA_BARANG)
        }

        with(binding) {
            rgRating.setOnCheckedChangeListener { _, checkedId ->
                tvRate.text = when (checkedId) {
                    R.id.rb_rate1 -> "Sangat Kurang"
                    R.id.rb_rate2 -> "Kurang"
                    R.id.rb_rate3 -> "Cukup"
                    R.id.rb_rate4 -> "Baik"
                    R.id.rb_rate5 -> "Sangat Baik"
                    else -> "Belum diberi rating"
                }
            }

            btnSendRate.setOnClickListener {
                barangSewa?.forEach {
                    barangRef.child(it.idBarang).child("Rating").get().addOnSuccessListener { data ->
                        listNilai.clear()
                        when {
                            rbRate1.isChecked -> nilai = rbRate1.text.toString().toInt()
                            rbRate2.isChecked -> nilai = rbRate2.text.toString().toInt()
                            rbRate3.isChecked -> nilai = rbRate3.text.toString().toInt()
                            rbRate4.isChecked -> nilai = rbRate4.text.toString().toInt()
                            rbRate5.isChecked -> nilai = rbRate5.text.toString().toInt()
                        }
                        listNilai.add(nilai)

                        val result=data.value
                        if (result!=null) {
                            result.toString().split(", ").toTypedArray().forEach { item ->
                                listNilai.add(item.toInt())
                            }
                            Log.d("RatingFragment", "result: $result")
                        }
                        barangRef.child(it.idBarang).child("Rating").setValue(
                            listNilai.joinToString { item-> item.toString() }
                        )
                        Log.d("RatingFragment", "onViewCreated: $listNilai")

                        dialog?.dismiss()
                    }
                }
            }

            btnNoRate.setOnClickListener {
                dialog?.dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}