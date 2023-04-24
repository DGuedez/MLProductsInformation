package com.david.mlproductinformation.search.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.david.mlproductinformation.databinding.FragmentSearchProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchProductsFragment : Fragment() {
    private var _binding: FragmentSearchProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchProductsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {

        setUpSearchViewListener()
        observeViewState()
        observerViewEffects()

    }

    private fun observerViewEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewEffect.collect { actionTo(it) }
        }
    }

    private fun actionTo(effects: SearchProductsViewEffects) {
        when (effects) {
            SearchProductsViewEffects.NavigateToSearchProductsList -> navigateToProductList()
        }
    }

    private fun navigateToProductList() {
        //TODO("Not yet implemented")
    }


    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { renderState(it) }
        }
    }

    private fun renderState(state: SearchProductViewState) {
        with(binding) {
            productSearchProgressBar.isVisible = state.loading
        }
    }

    private fun setUpSearchViewListener() {
        binding.productSearchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.onEvent(SearchProductEvent.RequestSearch(query.orEmpty()))
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}