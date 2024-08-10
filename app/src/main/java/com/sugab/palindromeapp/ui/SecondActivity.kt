package com.sugab.palindromeapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.sugab.palindromeapp.R
import com.sugab.palindromeapp.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private val chooseUserLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedUserName = result.data?.getStringExtra("SELECTED_USER")
                binding.tvSelectedUser.text = selectedUserName
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // View Binding
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("NAME") ?: "Guest"

        // Menampilkan nama di TextView
        binding.tvName.text = getString(R.string.your_name, name)

        // Inside SecondActivity
        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            chooseUserLauncher.launch(intent)
        }
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}