package com.example.movie.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movie.R
import com.example.movie.databinding.FragmentProfileBinding
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import com.example.movie.viewmodel.HomeViewModel
import kotlinx.coroutines.*

class ProfileFragment() : DialogFragment() {
    lateinit var viewModel: HomeViewModel
    private var myDB: UserDatabase?= null
    private var _binding: FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private val sharedPreferences = "sharedPreferences"
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileScreen: SharedPreferences = requireActivity().getSharedPreferences(sharedPreferences, Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)



        myDB = UserDatabase.getInstance(requireContext())

        binding.etUsername.setText(args.user?.username)
        binding.etEmail.setText(args.user?.email)
        binding.etPassword.setText(args.user?.password)

        binding.btnUpdate.setOnClickListener {

            val objectUser = User(
                args.user?.id,
                binding.etUsername.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
            GlobalScope.async {
                val result = myDB?.userDao()?.updateItem(objectUser)
                runBlocking(Dispatchers.Main) {
                    if (result != 0) {
                        viewModel.getDataUser(objectUser)
                        val editor: SharedPreferences.Editor = profileScreen.edit()
                        editor.putString("username", binding.etUsername.text.toString())
                        editor.putString("email", binding.etEmail.text.toString())
                        editor.putString("password", binding.etPassword.text.toString())
                        editor.apply()
                        Toast.makeText(requireContext(), "Sukses mengubah profil user", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_profileFragment_to_menuFragment2)
                    } else {
                        Toast.makeText(requireContext(), "Gagal mengubah profil user", Toast.LENGTH_SHORT).show()
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
}
