package com.firmansyah.malangcamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.firmansyah.malangcamp.databinding.ListBookingBinding
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.pelanggan.ui.pembayaran.BookingDetailFragment
import com.firmansyah.malangcamp.theme.MalangCampTheme

class BookingAdapter(
    private val listBooking: ArrayList<Pembayaran>, private val isAdmin: Boolean
) : RecyclerView.Adapter<BookingAdapter.ListViewHolder>() {
    fun setData(data: List<Pembayaran>) {
        listBooking.clear()
        listBooking.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(pembayaran: Pembayaran) {
            with(binding) {
                composeListBooking.setContent {
                    MalangCampTheme {
                        Item(pembayaran, isAdmin)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBooking[position])
    }

    override fun getItemCount(): Int = listBooking.size
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Item(pembayaran: Pembayaran, isAdmin: Boolean) {
    val context = LocalContext.current
    Card(
        onClick = { toDetailPembayaran(context, pembayaran, isAdmin) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Diambil ${pembayaran.tanggalPengambilan}; ${pembayaran.jamPengambilan}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = pembayaran.namaPenyewa, fontWeight = FontWeight.Bold
            )
            Text(
                text = pembayaran.noTelp, modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

private fun toDetailPembayaran(
    context: Context,
    pembayaran: Pembayaran,
    isAdmin: Boolean
) {
    val bookingDetailFragment = BookingDetailFragment()
    val mFragmentManager = (context as AppCompatActivity).supportFragmentManager
    val bundle = Bundle()

    bundle.putParcelable(BookingDetailFragment.EXTRA_PEMBAYARAN, pembayaran)
    bundle.putBoolean(BookingDetailFragment.EXTRA_ISADMIN, isAdmin)
    bookingDetailFragment.arguments = bundle
    bookingDetailFragment.show(mFragmentManager, BookingDetailFragment::class.java.simpleName)
}

@Preview
@Composable
fun ItemPreview() {
    MalangCampTheme {
        Item(Pembayaran(), true)
    }
}