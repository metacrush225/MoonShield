package com.example.moveshield.Listeners

import android.view.View
import android.widget.Toast

class MyClickListener : View.OnClickListener {
    override fun onClick(view: View) {
        // Add your code here to handle the button click event
        Toast.makeText(view.context, "Button clicked!", Toast.LENGTH_SHORT).show()
    }
}

