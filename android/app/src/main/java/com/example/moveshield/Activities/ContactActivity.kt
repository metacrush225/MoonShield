package com.example.moveshield.Activities

import android.content.ContentValues
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moveshield.*
import com.example.moveshield.Adapters.AllContactsAdapter
import com.example.moveshield.Helpers.ContactDatabaseHelper
import com.example.moveshield.Modeles.Contact

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val listView = findViewById<ListView>(R.id.listView)

        val contacts = loadAllContacts()
        val adapter = AllContactsAdapter(this, contacts)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            registerContact(contact)
        }

    }


    private fun loadAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val id = if (idIndex >= 0) it.getString(idIndex) else ""

                //val id = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val name = if (nameIndex >= 0) it.getString(nameIndex) else ""

                val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumber = if (phoneNumberIndex >= 0) it.getString(phoneNumberIndex) else ""

                contacts.add(Contact(id, name, phoneNumber))

            }
        }
        return contacts
    }
    private fun registerContact(contact: Contact?) {
        if (contact != null) {
            val db = ContactDatabaseHelper(this).writableDatabase
            val values = ContentValues().apply {
                put("name", contact.name)
                put("phone", contact.phoneNumber)
            }
            db.insert("contacts", null, values)
            db.close()
            Toast.makeText(this, "Registered ${contact.name}", Toast.LENGTH_SHORT).show()

        }
    }

    private fun dropContactsTable() {
            val db = ContactDatabaseHelper(this).writableDatabase
            db.execSQL("DROP TABLE IF EXISTS contacts")
        Toast.makeText(this, "Column dropped", Toast.LENGTH_SHORT).show()
    }


}