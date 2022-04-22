package com.example.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.databinding.FragmentRegisterBinding
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RegisterFragment : Fragment() {
    private var mDB: UserDatabase?=null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
    : View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDB = UserDatabase.getInstance(requireContext())

        binding.btnDaftar.setOnClickListener {
            when {
                binding.etUsername.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty() || binding.etPassword.text.isNullOrEmpty() || binding.etConfirmPassword.text.isNullOrEmpty() ->{
                    Toast.makeText(activity, "Terdapat Data Yang Belum Terisi", Toast.LENGTH_SHORT).show()
                }
                binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString() -> {
                    Toast.makeText(activity, "Password anda tidak sesuai", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val user = User(
                        null,
                        binding.etUsername.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                    GlobalScope.async {
                        val result =mDB?.userDao()?.addUser(user)
                        activity?.runOnUiThread {
                            if (result != 0.toLong()){
                                Toast.makeText(activity, "Pendaftaran akun anda berhasil", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(activity, "Pendaftaran akun anda gagal", Toast.LENGTH_SHORT).show()
                            }
                            onStop()
                        }
                    }

                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
