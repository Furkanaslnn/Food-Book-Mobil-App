package com.example.foodbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbook.adapter.FoodRecyclerAdapter
import com.example.foodbook.databinding.FragmentFoodListBinding
import com.example.foodbook.viewmodel.FoodListViewModel


class FoodListFragment : Fragment() {

    private var _binding: FragmentFoodListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FoodListViewModel
    private val foodRecyclerAdapter = FoodRecyclerAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodListViewModel::class.java]
        viewModel.refreshData()

        binding.foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.foodRecyclerView.adapter = foodRecyclerAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.foodRecyclerView.visibility = View.GONE
            binding.foodErrorMessage.visibility = View.GONE
            binding.foodLoading.visibility = View.VISIBLE
            viewModel.refreshDataFromNet()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()

    }

    private fun observeLiveData() {
        viewModel.foods.observe(viewLifecycleOwner) {
            foodRecyclerAdapter.foodListUpdate(it)
            binding.foodRecyclerView.visibility = View.VISIBLE
        }

        viewModel.foodErrorMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.foodErrorMessage.visibility = View.VISIBLE
                binding.foodRecyclerView.visibility = View.GONE
            } else {
                binding.foodErrorMessage.visibility = View.GONE
            }
        }

        viewModel.foodLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.foodErrorMessage.visibility = View.GONE
                binding.foodRecyclerView.visibility = View.GONE
                binding.foodLoading.visibility = View.VISIBLE
            } else {
                binding.foodLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}