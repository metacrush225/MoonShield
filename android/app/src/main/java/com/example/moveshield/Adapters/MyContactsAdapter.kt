package com.example.moveshield.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveshield.Modeles.Contact
import com.example.moveshield.R


class MyContactsAdapter(private val data: List<Contact>) : RecyclerView.Adapter<MyContactsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val phoneTextView: TextView = view.findViewById(R.id.phone_text_view)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_contacts_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myData = data[position]
        holder.nameTextView.text = myData.name
        holder.phoneTextView.text = myData.phoneNumber



    }
}