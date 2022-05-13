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
import com.example.movie.viewmodel.HomeViewModel
import com.example.movie.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private var mDB: UserDatabase?= null
    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!
    private val sharedPreferences = "sharedPreferences"
    private lateinit var viewModel: HomeViewModel
    private lateinit var pref: DataStoreManager

    companion object{
        const val USERNAME = "username"
    }

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
        mDB = UserDatabase.getInstance(requireContext())
        pref = DataStoreManager(requireContext())
        viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        cekLogin()
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
                        val getUser = mDB?.userDao()?.getUser(
                            username
                        )
                        activity?.runOnUiThread {
                            if (getUser != null) {
                                Toast.makeText(requireContext(), "Login Berhasil", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
                            } else {
                                Toast.makeText(requireContext(), "Username / Password salah!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        if (getUser != null) {
                            viewModel.setDataUser(getUser)
                        }
                    }
                }
            }

        }
    }
    private fun cekLogin() {
        viewModel.apply {
            getDataUser().observe(viewLifecycleOwner) {
                if (it.id != -1) {
                    findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
                }
            }
        }
    }
}

