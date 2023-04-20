package com.example.moveshield.Fragments

import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moveshield.Adapters.MyContactsAdapter
import com.example.moveshield.Modeles.Contact
import com.example.moveshield.R
import com.example.moveshield.Helpers.ContactDatabaseHelper

/**
 * A fragment representing a list of Items.
 */
class FragmentContacts : Fragment() {

    private var columnCount = 1
    private var contactsData: List<Contact> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        contactsData = readContacts()


        displayDataInView(view)

        return view
    }


    private fun displayDataInView(view: View) {
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                //adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
                adapter = MyContactsAdapter(contactsData)
            }
        }
    }

    private fun refreshData() {
        contactsData = readContacts()
        view?.let { displayDataInView(it) } // Update the UI
    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FragmentContacts().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


    private fun readContacts(): MutableList<Contact> {
        val contacts = mutableListOf<Contact>()
        Log.d("TABLECONTACT", "0")

        val dbHelper = ContactDatabaseHelper(requireContext())
        val db = dbHelper.readableDatabase
        Log.d("TABLECONTACT", "1")

        try {
            val cursor = db.query(
                "contacts",
                arrayOf("_id", "name", "phone"),
                null,
                null,
                null,
                null,
                null
            )
            Log.d("TABLECONTACT", "2")

            while (cursor.moveToNext()) {
                var _id = ""
                var name = ""
                var phone = ""

                val _idIndex = cursor.getColumnIndex("_id")
                if(_idIndex > -1)
                    _id = cursor.getString(_idIndex)

                val nameIndex = cursor.getColumnIndex("name")
                if(nameIndex > 0)
                    name = cursor.getString(nameIndex)

                val phoneNumberIndex = cursor.getColumnIndex("phone")
                if(phoneNumberIndex > 0)
                    phone = cursor.getString(phoneNumberIndex)

                Log.d("TABLECONTACT", "id = $_id ; name = $name ; phone = $phone")
                contacts.add(Contact(_id, name, phone))
                // do something with the data
                Log.d("TABLECONTACTNAME :", name)

            }
            Log.d("TABLECONTACT", "3")
            cursor.close()
        } catch (e: SQLiteException) {
            Log.d("TABLECONTACT", "ERR > " + e.message.toString())

        }
        return contacts
    }
}