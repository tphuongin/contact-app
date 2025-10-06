package com.example.contactappv1.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.contactappv1.viewmodel.AppDatabase
import com.example.contactappv1.Utils.ContactValidator.validateInfor
import com.example.contactappv1.databinding.ActivityAddContactBinding
import com.example.contactappv1.model.DataUser
import kotlinx.coroutines.launch

class AddContactActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddContactBinding.inflate(layoutInflater) }
    private var selectedUri = ""
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){uri ->
        if(uri != null){
            Glide.with(this)
                .load(uri)
                .into(binding.ivAvatar)
            selectedUri = uri.toString()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val dp = AppDatabase.Companion.getInstance(applicationContext)
        val userDao = dp.userDao()
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.icCamera.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.tvSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            val avatar = selectedUri.ifBlank { null }
            if(validateInfor(this, name, phone, email)){
                val mail = if(binding.etEmail.text != null) binding.etEmail.text.toString() else null
                val user = DataUser(
                    name = binding.etName.text.toString(),
                    phoneNumer = binding.etPhone.text.toString(),
                    email = mail,
                    srcAvatar = avatar
                )
                lifecycleScope.launch {
                    userDao.addUser(user)
                }
                finish()
                Toast.makeText(this, "Create new contact successful", Toast.LENGTH_LONG).show()
            }
        }

    }

}