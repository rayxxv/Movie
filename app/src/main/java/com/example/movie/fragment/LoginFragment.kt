package com.example.movie.fragment

import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.databinding.FragmentLoginBinding
import com.example.movie.datastore.DataStoreManager
import com.example.movie.room.UserDatabase
import com.example.movie.room.UserRepository
import com.example.movie.viewmodel.HomeViewModel
import com.example.movie.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*


class LoginFragment : Fragment() {
    private var mDB: UserDatabase?= null
    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!
    private lateinit var repository: UserRepository
    private lateinit var viewModel: HomeViewModel
    private lateinit var pref: DataStoreManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = UserRepository(requireContext())
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        viewModel.getDataStore().observe(viewLifecycleOwner) {
            if (it.toString() != DataStoreManager.DEFAULT_USERNAME) {
                findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
            }
        }

        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnMasuk.setOnClickListener {
            when {
                binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString()
                    .isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        "Data tidak boleh Kosong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val username = binding.etUsername.text.toString()
                    val password = binding.etPassword.text.toString()
                    lifecycleScope.launch(Dispatchers.IO) {
                        val getUser = repository.checkUser(username, password.toString())
                        runBlocking(Dispatchers.Main) {
                            if (getUser == false) {
                                val snackbar = Snackbar.make(it,"Login gagal, coba periksa email atau password anda", Snackbar.LENGTH_INDEFINITE)
                                snackbar.setAction("Oke") {
                                    snackbar.dismiss()
                                }
                                snackbar.show()
                            } else {
                                Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
                            }
                        }
                        if (getUser != false){
                            viewModel.saveDataStore(username.toString())
                        }
                    }
                }
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

