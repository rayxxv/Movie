package com.example.movie.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movie.R
import com.example.movie.databinding.FragmentProfileBinding
import com.example.movie.datastore.DataStoreManager
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import com.example.movie.room.UserRepository
import com.example.movie.viewmodel.HomeViewModel
import kotlinx.coroutines.*
import java.io.File

class ProfileFragment : DialogFragment() {
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentProfileBinding?= null
    private val binding get() = _binding!!
    private lateinit var repository: UserRepository
    private lateinit var pref: DataStoreManager
    private val args: ProfileFragmentArgs by navArgs()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = UserRepository(requireContext())
        pref = DataStoreManager(requireContext())

        binding.btnImage.setImageURI(args.user?.uri.toString().toUri())
        binding.etUsername.setText(args.user?.username)
        binding.etEmail.setText(args.user?.email)
        binding.etPassword.setText(args.user?.password)

        binding.btnUpdate.setOnClickListener {
            val user = User(
                args.user?.id,
                binding.etUsername.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                imageUri.toString()
            )
            lifecycleScope.launch(Dispatchers.IO) {
                val result = repository.updateUser(user)
                runBlocking(Dispatchers.Main) {
                    if (result != 0){
                        Toast.makeText(requireContext(), "Profile berhasil diupdate", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Profile gagal diupdate", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            findNavController().navigate(R.id.action_profileFragment_to_menuFragment2)
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->
                    dialog.dismiss()
                    lifecycleScope.launch(Dispatchers.IO) {
                        pref.deleteUserFromPref()
                    }
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
                .setNegativeButton("Batal"){ dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

//        binding.profileImage.setOnClickListener {
//            openImagePicker()
//        }

    }

//    private val startForProfileImageResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            val resultCode = result.resultCode
//            val data = result.data
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    //Image Uri will not be null for RESULT_OK
//                    imageUri = data?.data
//                    loadImage(imageUri)
//
//                }
//                ImagePicker.RESULT_ERROR -> {
//                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//
//                }
//            }
//        }
//
//    private fun openImagePicker() {
//        ImagePicker.with(this)
//            .crop()
//            .saveDir(
//                File(
//                    requireContext().externalCacheDir,
//                    "ImagePicker"
//                )
//            ) //Crop image(Optional), Check Customization for more option
//            .compress(1024)            //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                1080,
//                1080
//            )    //Final image resolution will be less than 1080 x 1080(Optional)
//            .createIntent { intent ->
//                startForProfileImageResult.launch(intent)
//            }
//    }
//
//    private fun loadImage(uri: Uri?) {
//        uri?.let {
//            binding.btnImage.setImageURI(it)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
