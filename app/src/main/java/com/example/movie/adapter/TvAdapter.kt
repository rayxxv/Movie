package com.example.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.databinding.ItemTvSeriesBinding
import com.example.movie.fragment.MenuFragmentDirections
import com.example.movie.model.ResultX

class TvAdapter :
    RecyclerView.Adapter<TvAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<ResultX>() {
        override fun areItemsTheSame(
            oldItem: ResultX,
            newItem: ResultX
        ): Boolean = oldItem.number == newItem.number

        override fun areContentsTheSame(
            oldItem: ResultX,
            newItem: ResultX
        ): Boolean = oldItem.hashCode() == newItem.hashCode()

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<ResultX>?) = differ.submitList(value)

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTvSeriesBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TvAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let { holder.bind(data) }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemTvSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResultX) {
            binding.apply {
                Glide.with(binding.root).load(IMAGE_BASE + data.posterPath).fitCenter().into(ivSeries)
                tvSeriesTitle.text = data.name
                tvSeriesRating.text = data.voteAverage.toString()
                root.setOnClickListener {
                    val id = MenuFragmentDirections.actionMenuFragmentToDetailSeriesFragment(data.number)
                    it.findNavController().navigate(id)
                }
            }
        }
    }

}


