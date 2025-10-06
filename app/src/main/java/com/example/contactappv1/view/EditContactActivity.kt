package com.example.contactappv1.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.contactappv1.R
import com.example.contactappv1.Utils.ContactValidator.validateInfor
import com.example.contactappv1.databinding.ActivityEditContactBinding
import com.example.contactappv1.model.DataUser
import com.example.contactappv1.model.UserDao
import com.example.contactappv1.viewmodel.AppDatabase
import kotlinx.coroutines.launch

class EditContactActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditContactBinding.inflate(layoutInflater)
    }
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
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var user: LiveData<DataUser>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getInstance(applicationContext)
        userDao = db.userDao()
        var userId = intent.getIntExtra("UserId", -1)
        user = userDao.getUserById(userId)
        user.observe(this) { userData ->
            setUp()
        }
        binding.icCamera.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        binding.tvSave.setOnClickListener {
            updateContact()
        }
        binding.ivClose.setOnClickListener {
            finish()
        }
    }
    fun updateContact(){
        val newName = binding.etName.text.toString()
        val newEmail = binding.etEmail.text.toString()
        val newPhone = binding.etPhone.text.toString()
        val avatar = selectedUri.ifBlank { null }
        if (newName == user.value!!.name &&
            newPhone == user.value!!.phoneNumer &&
            newEmail == user.value!!.email &&
            selectedUri == user.value!!.srcAvatar) {
            Toast.makeText(this, "Information doesn't change", Toast.LENGTH_LONG).show()
            return
        }
        else{
            if(validateInfor(this, newName, newPhone, newEmail)){
                val mail = newEmail.ifBlank { null }
                val avatar = selectedUri.ifBlank { null }
                user.value!!.name = newName
                user.value!!.phoneNumer = newPhone
                user.value!!.email = mail
                user.value!!.srcAvatar = avatar
                lifecycleScope.launch {
                    userDao.updateInfor(user.value!!)
                }
                finish()
                Toast.makeText(this, "Update contact successful", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun setUp() {
        val userInfor = user.value!!
        binding.etName.setText(userInfor.name)
        binding.etPhone.setText(userInfor.phoneNumer)
        binding.etEmail.setText(userInfor.email)
        Glide.with(this)
            .load(userInfor.srcAvatar ?: R.drawable.avatar_default)
            .into(binding.ivAvatar)
        selectedUri = userInfor.srcAvatar ?: ""
    }

}