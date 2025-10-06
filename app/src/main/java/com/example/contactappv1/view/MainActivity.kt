package com.example.contactappv1.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactappv1.viewmodel.AppDatabase
import com.example.contactappv1.databinding.ActivityMainBinding
import com.example.contactappv1.viewmodel.ContactAdapter

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = AppDatabase.Companion.getInstance(applicationContext)
        val userDao = db.userDao()
        var users = userDao.getAll()
        binding.rcv.layoutManager = LinearLayoutManager(this)
        val adapter = ContactAdapter(mutableListOf()){userId ->
            val intentDetail = Intent(this, DetailContactActivity::class.java)
            intentDetail.putExtra("UserId", userId)
            startActivity(intentDetail)
        }
        binding.rcv.adapter = adapter

        userDao.getAll().observe(this) { users ->
            adapter.updateData(users)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }
        binding.ivSearch.setOnClickListener {
            binding.etSearch.visibility = View.VISIBLE
            binding.tvContacts.visibility = View.GONE
            binding.etSearch.requestFocus()
        }
        binding.etSearch.addTextChangedListener { editable ->
            val query = editable.toString()
            if (query.isEmpty()) {
                userDao.getAll().observe(this) { users ->
                    adapter.updateData(users)
                }
            } else {
                userDao.findUsersByName("%$query%").observe(this) { users ->
                    adapter.updateData(users)
                }
            }
        }

    }
}