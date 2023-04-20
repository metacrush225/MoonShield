package com.example.moveshield.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveshield.Helpers.ContactDatabaseHelper
import com.example.moveshield.Modeles.Contact
import com.example.moveshield.R


class MyContactsAdapter(private val data: List<Contact>) : RecyclerView.Adapter<MyContactsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name_text_view)
        val phoneTextView: TextView = view.findViewById(R.id.phone_text_view)
        val corbeilleImageButton: ImageButton = view.findViewById(R.id.imageButtonCorbeille)

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
        holder.corbeilleImageButton.setOnClickListener{
            Log.d("Position", "Position = " + position)
            deleteContactById(myData.id, holder)

        }
    }

    fun deleteContactById(the_id: String, holder: ViewHolder): Boolean {
        val db = ContactDatabaseHelper(holder.corbeilleImageButton.context).writableDatabase

        return db.delete("contacts", "_id = ${the_id}", null) > 0
    }

}