package com.example.movie.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movie.databinding.FragmentLoginBinding
import com.example.movie.room.UserDatabase
import com.example.movie.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async



class LoginFragment : Fragment() {
    private var mDB: UserDatabase?= null
    private var _binding: FragmentLoginBinding?= null
    private val binding get() = _binding!!

    companion object{
        const val AKUN = "user_login"
        const val USERNAME = "username"
        const val PASSWORD = "password"
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

        val loginscreen: SharedPreferences = requireActivity().getSharedPreferences(AKUN, Context.MODE_PRIVATE)
        if (loginscreen!!.getString(USERNAME,null)!=null){

            findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
            val username = loginscreen.getString(USERNAME,null)
            Toast.makeText(context, "Selamat datang $username", Toast.LENGTH_SHORT).show()
        }

        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnMasuk.setOnClickListener {
            GlobalScope.async {
                val flags = mDB?.userDao()?.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
                activity?.runOnUiThread {
                    if (flags == true) {
                        val sharpref: SharedPreferences.Editor = loginscreen.edit()
                        sharpref.putString("username", binding.etUsername.text.toString())
                        sharpref.apply()
                        findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
                    } else {
                        Toast.makeText(context, "Tidak Berhasil Login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

