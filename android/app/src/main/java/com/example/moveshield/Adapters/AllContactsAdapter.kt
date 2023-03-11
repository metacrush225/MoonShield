package com.example.moveshield.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.moveshield.Modeles.Contact

class AllContactsAdapter(context: Context, contacts: List<Contact>) :
    ArrayAdapter<Contact>(context, android.R.layout.simple_list_item_1, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val contact = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${contact?.name} (${contact?.phoneNumber})"

        return view
    }
}