package com.example.movie.fragment

import android.content.ContentResolver
import android.net.Uri
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
import com.example.movie.room.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RegisterFragment : Fragment() {
    private lateinit var repository: UserRepository
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
        repository = UserRepository(requireContext())

        binding.btnDaftar.setOnClickListener {
            val imageUri: Uri = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + resources.getResourcePackageName(R.drawable.default_profile)
                        + '/' + resources.getResourceTypeName(R.drawable.default_profile) + '/'
                        + resources.getResourceEntryName(R.drawable.default_profile)
            )
            val username = binding.etUsername.text
            val email = binding.etEmail.text
            val password = binding.etPassword.text
            val konfirmasiPassword = binding.etConfirmPassword.text

            when {
                binding.etUsername.text.isNullOrEmpty() || binding.etEmail.text.isNullOrEmpty() || binding.etPassword.text.isNullOrEmpty() || binding.etConfirmPassword.text.isNullOrEmpty() ->{
                    Toast.makeText(activity, "Terdapat Data Yang Belum Terisi", Toast.LENGTH_SHORT).show()
                }
                binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString() -> {
                    Toast.makeText(activity, "Password anda tidak sesuai", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val user = User(
                        null, username.toString(), email.toString(), password.toString(), imageUri.toString())
                    GlobalScope.async {
                        val result =repository.addUser(user)
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
