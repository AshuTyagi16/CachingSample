package com.ryuk.cachingsample.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ryuk.cachingsample.databinding.CellMovieBinding
import com.ryuk.cachingsample.domain.model.Movie
import com.ryuk.cachingsample.util.dp

class MovieViewHolder(private val binding: CellMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }

    fun setMovie(movie: Movie) {
        Glide.with(itemView).load(IMAGE_BASE_URL.plus(movie.poster_path))
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(20.dp)))
            .into(binding.ivImage)
    }
}