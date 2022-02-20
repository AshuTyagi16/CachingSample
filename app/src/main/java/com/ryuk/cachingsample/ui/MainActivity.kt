package com.ryuk.cachingsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.ryuk.cachingsample.databinding.ActivityMainBinding
import com.ryuk.cachingsample.util.dp
import com.ryuk.cachingsample.util.observeCached
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding as ActivityMainBinding

    private val viewModel by viewModels<MainActivityViewModel> { defaultViewModelProviderFactory }

    private var adapter: MovieAdapter? = null

    private val spaceItemDecoration = SpaceItemDecoration(6.dp, 6.dp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(requireNotNull(binding.root))
        setupUI()
        observeLiveData()
        getData()
    }

    private fun setupUI() {
        binding.rvPopular.layoutManager = GridLayoutManager(this, 2)
        binding.rvPopular.addItemDecoration(spaceItemDecoration)
        adapter = MovieAdapter()
        binding.rvPopular.adapter = adapter
    }

    private fun observeLiveData() {
        viewModel.moviesLiveData.observeCached(
            this,
            onLoading = {
                binding.progressBar.isVisible = true
                binding.rvPopular.isVisible = false
            },
            onSuccess = {
                binding.progressBar.isVisible = false
                binding.rvPopular.isVisible = true
                adapter?.submitList(it.results)
            },
            onError = {
                binding.progressBar.isVisible = false
                binding.rvPopular.isVisible = false
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun getData() {
        viewModel.fetchPopularMovies()
    }

}