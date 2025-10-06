package com.example.contactappv1.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.contactappv1.R
import com.example.contactappv1.databinding.ActivityDetailContactBinding
import com.example.contactappv1.model.DataUser
import com.example.contactappv1.model.UserDao
import com.example.contactappv1.viewmodel.AppDatabase
import kotlinx.coroutines.launch

class DetailContactActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailContactBinding.inflate((layoutInflater))
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
        val db = AppDatabase.getInstance(applicationContext)
        val userDao = db.userDao()
        var userId = intent.getIntExtra("UserId", -1)
        val user = userDao.getUserById(userId)
        user.observe(this){user ->
            if(user != null)
                disPlay(user)
            else{
                binding.ivDelete.isClickable = false
                binding.ivEdit.isClickable = false
            }
        }
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.ivDelete.setOnClickListener {
            deleteContact(user, userDao)
        }
        binding.ivEdit.setOnClickListener {
            val intentEdit = Intent(this, EditContactActivity::class.java)
            intentEdit.putExtra("UserId", userId)
            startActivity(intentEdit)
        }
    }
    fun deleteContact(user: LiveData<DataUser>, userDao: UserDao){
        binding.overlay.visibility = View.VISIBLE
        binding.btnComfirm.visibility = View.VISIBLE
        binding.tvYes.setOnClickListener {
            lifecycleScope.launch {
                if(user.value != null){
                    userDao.deleteContact(user.value!!)
                }
                else Toast.makeText(this@DetailContactActivity, "User not exist", Toast.LENGTH_LONG).show()
            }
            binding.overlay.visibility = View.GONE
            binding.btnComfirm.visibility = View.GONE
            Toast.makeText(this@DetailContactActivity, "Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.tvNo.setOnClickListener{
            binding.overlay.visibility = View.GONE
            binding.btnComfirm.visibility = View.GONE
        }
    }
    fun disPlay(user: DataUser){
        Glide.with(this)
            .load(user.srcAvatar)
            .fallback(R.drawable.avatar_default)
            .into(binding.ivAvatar)
        binding.tvName.text = user.name
        binding.tvPhone.text = user.phoneNumer
        if (!user.email.isNullOrBlank()) {
            binding.tvEmail.text = user.email
            binding.tvEmail.visibility = View.VISIBLE
            binding.icMail.visibility = View.VISIBLE
        } else {
            binding.tvEmail.visibility = View.GONE
            binding.icMail.visibility = View.GONE
        }
    }

}