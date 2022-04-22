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
import com.example.movie.model.Popular
import com.example.movie.model.Result
import com.example.movie.model.ResultX
import com.example.movie.model.Tv
import com.example.movie.room.User
import com.example.movie.room.UserDatabase
import com.example.movie.service.ApiClient
import com.example.movie.viewmodel.HomeViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SharedPreferences
    private var myDB: UserDatabase?= null
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireActivity().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        getUser()
        fetchAllDataMovie()
        fetchAllDataSeries()

        binding.btnProfile.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }
        viewModel.user.observe(viewLifecycleOwner) {
            binding.tvJudul.text = "Hallo ${it.username}"
        }
    }
    private fun getUser() {
        myDB = UserDatabase.getInstance(requireContext())
        preferences = requireContext().getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val username = preferences.getString("username", null)


        GlobalScope.async {
            val user = myDB?.userDao()?.getUser(username.toString())
            runBlocking(Dispatchers.Main) {
                if (user != null) {
                    val data = User(user.id, user.username, user.email, user.password)
                    viewModel.getDataUser(data)
                    binding.btnProfile.setOnClickListener {
                        val direct = MenuFragmentDirections.actionMenuFragmentToProfileFragment(data)
                        findNavController().navigate(direct)
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