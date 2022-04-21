package com.example.movie.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.databinding.FragmentProfileBinding
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ProfileFragment() : DialogFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    lateinit var itemSelected : User
    constructor(itemSelected:User):this(){
        this.itemSelected = itemSelected
    }
    var mDb:UserDatabase?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = UserDatabase.getInstance(requireContext())
        preferences = requireContext().getSharedPreferences(LoginFragment.AKUN, Context.MODE_PRIVATE)

        logout()

        if(this::itemSelected.isInitialized){
            binding.etUsername.setText(itemSelected.username)
            binding.etEmail.setText(itemSelected.email)
            binding.etPassword.setText(itemSelected.password)
        }

        binding.btnUpdate.setOnClickListener {
            when {
                binding.etEmail.text.isNullOrEmpty() -> {
                    binding.wrapEmail.error = "Data Belum Terisi"
                }
                binding.etUsername.text.isNullOrEmpty() -> {
                    binding.wrapUsername.error = "Data Belum Terisi"
                }
                binding.etPassword.text.isNullOrEmpty() -> {
                    binding.wrapPassword.error = "Data Belum Terisi"
                }

                else -> {
                    val email: String = binding.etEmail.text.toString()
                    val username: String = binding.etUsername.text.toString()
                    val password: String = binding.etPassword.text.toString()

                    val objectItem = itemSelected
                    objectItem.email = email
                    objectItem.username = username
                    objectItem.password = password

                    GlobalScope.async {
                        val result = mDb?.userDao()?.updateItem(objectItem)
                        runBlocking {
                            if (result != 0) {
                                Toast.makeText(
                                    it.context,
                                    "Profile ${itemSelected.username} sukses diubah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(it.context, "Harga gagal diubah", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun logout() {
        binding.btnLogout.setOnClickListener {
            val dialogKonfirmasi = AlertDialog.Builder(requireContext())
            dialogKonfirmasi.apply {
                setTitle("Logout")
                setMessage("Apakah anda yakin ingin log out?")
                setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }
                setPositiveButton("Ya") { dialog, which ->
                    dialog.dismiss()

                    preferences.edit().clear().apply()
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
            dialogKonfirmasi.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}