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
import androidx.navigation.fragment.navArgs
import com.example.movie.R
import com.example.movie.adapter.PopularAdapter
import com.example.movie.adapter.TvAdapter
import com.example.movie.databinding.FragmentMenuBinding
import com.example.movie.model.Popular
import com.example.movie.model.Result
import com.example.movie.model.ResultX
import com.example.movie.model.Tv
import com.example.movie.room.UserDatabase
import com.example.movie.service.ApiClient
import com.example.movie.viewmodel.MenuViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    var myDb : UserDatabase? = null
    private val args : MenuFragmentArgs by navArgs()
    private lateinit var MenuViewModel: MenuViewModel
    private lateinit var preferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireContext().getSharedPreferences(LoginFragment.AKUN, Context.MODE_PRIVATE)
        binding.tvJudul.text = "Welcome ${preferences.getString(LoginFragment.USERNAME,null)}"
        binding.btnProfile.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_profileFragment)
        }
        fetchAllDataMovie()
        fetchAllDataSeries()

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
        val adapter = PopularAdapter(object : PopularAdapter.OnClickListener{
            override fun onClickItem(data: Result) {
                val id = data.id
            }
        })
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
        val adapter = TvAdapter(object : TvAdapter.OnClickListener{
            override fun onClickItem(data: ResultX) {
                val id = data.number
            }
        })
        adapter.submitData(data)
        binding.rvPopularSeries.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}