package com.example.foodbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodbook.databinding.FragmentFoodDetailBinding
import com.example.foodbook.util.downloadImage
import com.example.foodbook.util.placeHolder
import com.example.foodbook.viewmodel.FoodDetailViewModel


class FoodDetailFragment : Fragment() {

    private var _binding: FragmentFoodDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FoodDetailViewModel
    private var foodId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FoodDetailViewModel::class.java]

        arguments?.let {
            foodId = FoodDetailFragmentArgs.fromBundle(it).foodId
        }

        viewModel.getRoomData(foodId)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.foodLiveData.observe(viewLifecycleOwner) {
            binding.foodName.text = it.name
            binding.foodCal.text = it.cal
            binding.foodCarbohydrates.text = it.carbohydrates
            binding.foodOil.text = it.oil
            binding.foodProtein.text = it.protein
            binding.imageView.downloadImage(it.image, placeHolder(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}