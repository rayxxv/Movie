package com.example.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.databinding.ItemMovieBinding
import com.example.movie.fragment.MenuFragmentDirections
import com.example.movie.model.Result

class PopularAdapter :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean = oldItem.hashCode() == newItem.hashCode()

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitData(value: List<Result>?) = differ.submitList(value)

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemMovieBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let { holder.bind(data) }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.apply {
                Glide.with(binding.root).load(IMAGE_BASE + data.posterPath).fitCenter().into(ivMovie)
                tvMovieTitle.text = data.originalTitle
                tvMovieRating.text = data.voteAverage.toString()
                root.setOnClickListener {
                    val id = MenuFragmentDirections.actionMenuFragmentToDetailFragment(data.id)
                    it.findNavController().navigate(id)
                }
            }
        }
    }

}


