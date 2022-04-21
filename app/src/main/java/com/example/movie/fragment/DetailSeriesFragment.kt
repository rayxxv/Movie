package com.example.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.databinding.FragmentDetailSeriesBinding
import com.example.movie.viewmodel.DetailSeriesViewModel



class DetailSeriesFragment : Fragment() {
    private var _binding: FragmentDetailSeriesBinding? = null
    private val binding get() = _binding!!
    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
    private lateinit var viewModel: DetailSeriesViewModel
    private val args: DetailSeriesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailSeriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailSeriesViewModel::class.java]

        binding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        viewModel.loading.observe(viewLifecycleOwner){
            if (it){
                binding.loadingContainer.visibility = View.VISIBLE
            } else{
                binding.loadingContainer.visibility = View.GONE
            }
        }

        val seriesId = args.seriesId

        viewModel.detailSeries.observe(viewLifecycleOwner) { it ->

            Glide.with(binding.ivBackdrop)
                .load(IMAGE_BASE + it?.backdropPath)
                .error(R.drawable.ic_broken)
                .into(binding.ivBackdrop)

            Glide.with(binding.ivPoster)
                .load(IMAGE_BASE + it?.posterPath)
                .error(R.drawable.ic_broken)
                .into(binding.ivPoster)

            binding.apply {
                tvJudul.text = it?.name
                tvTitle.text = it?.name
                tvVoteCount.text = it?.voteCount.toString()
                tvOverview.text = it?.overview
                it?.voteAverage.let {
                    if (it != null) {
                        rbRating.rating = (it / 2).toFloat()
                    }
                }
                if (it?.releaseDate != null && it.releaseDate.isNotBlank()){
                    tvReleaseDate.text = it.releaseDate
                }else{
                    tvReleaseDate.visibility = View.GONE
                }
            }
        }
        viewModel.getDetailSeries(seriesId)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

