package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentDetailInformasiBinding
import com.firmansyah.malangcamp.databinding.FragmentInformasiBarangBinding

class DetailInformasiFragment : Fragment() {

    private var _binding: FragmentDetailInformasiBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailInformasiBinding.inflate(inflater, container, false)
        return binding.root
    }
}