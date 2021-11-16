package com.firmansyah.malangcamp.admin.ui.barangsewa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.InfoBarangAdapter
import com.firmansyah.malangcamp.adapter.SewaBarangAdapter
import com.firmansyah.malangcamp.data.DataBarang
import com.firmansyah.malangcamp.data.DataTenda
import com.firmansyah.malangcamp.databinding.FragmentBarangSewaBinding
import com.firmansyah.malangcamp.databinding.FragmentInformasiBarangBinding
import com.firmansyah.malangcamp.model.Barang

class BarangSewaFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentBarangSewaBinding? = null
    private lateinit var adapter:SewaBarangAdapter
    private var list:ArrayList<Barang> = arrayListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentBarangSewaBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.addAll(DataBarang.listData)
        adapter = SewaBarangAdapter(list)

        binding.rvSewaBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvSewaBarang.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}