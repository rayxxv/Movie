package com.example.movie.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.adapter.PopularAdapter
import com.example.movie.adapter.TvAdapter
import com.example.movie.databinding.FragmentMenuBinding
import com.example.movie.datastore.DataStoreManager
import com.example.movie.model.Popular
import com.example.movie.model.Result
import com.example.movie.model.ResultX
import com.example.movie.model.Tv
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import com.example.movie.room.UserRepository
import com.example.movie.service.ApiClient
import com.example.movie.viewmodel.HomeViewModel
import com.example.movie.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: UserRepository
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pref: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = DataStoreManager(requireActivity())
        homeViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(pref))[HomeViewModel::class.java]
        repository = UserRepository(requireContext())

        binding.btnProfile.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }
        homeViewModel.getDataStore().observe(viewLifecycleOwner) {
            binding?.tvJudul.text = it
        }
        getUser()
        fetchAllDataMovie()
        fetchAllDataSeries()

    }
    private fun getUser() {
        binding!!.btnProfile.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val dataUser = repository.getUser(binding!!.tvJudul.text.toString())
                runBlocking(Dispatchers.Main) {
                    if (dataUser != null) {
                        val user = User(
                            dataUser.id,
                            dataUser.username,
                            dataUser.email,
                            dataUser.password,
                            dataUser.uri
                        )
                        val navigateUpdate = MenuFragmentDirections.actionMenuFragmentToProfileFragment(user)
                        findNavController().navigate(navigateUpdate)
                    }
                }
            }
        }
    }
    private fun fetchAllDataMovie(){
        ApiClient.instance.getAllMovie()
            .enqueue(object : Callback<Popular> {
                override fun onResponse(call: Call<Popular>, response: Response<Popular>
                ){
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        showListMovie(body?.results)
                        binding.progressBar.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<Popular>, t: Throwable){
                    binding.progressBar.visibility = View.GONE
                }
            })
    }
    private fun showListMovie(data: List<Result>?){
        val adapter = PopularAdapter()
        adapter.submitData(data)
        binding.rvPopularMovie.adapter = adapter
    }
    private fun fetchAllDataSeries(){
        ApiClient.instance.getAllSeries()
            .enqueue(object : Callback<Tv> {
                override fun onResponse(
                    call: Call<Tv>,
                    response: Response<Tv>
                ){
                    val body = response.body()
                    val code = response.code()
                    if (code == 200){
                        showListSeries(body?.results)
                        binding.progressBar.visibility = View.GONE
                    }else{
                        binding.progressBar.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<Tv>, t: Throwable){
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    private fun showListSeries(data: List<ResultX>?){
        val adapter = TvAdapter()
        adapter.submitData(data)
        binding.rvPopularSeries.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}