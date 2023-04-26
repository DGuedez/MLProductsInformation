package com.david.mlproductinformation.products.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.david.mlproductinformation.R
import com.david.mlproductinformation.common.presentation.Event
import com.david.mlproductinformation.databinding.FragmentProductsListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductsViewModel by viewModels()
    private val navigationAction = ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        requestProducts()
    }

    private fun requestProducts() {
        viewModel.onEvent(ProductsEvents.RequestProductsFromSearch)
    }

    private fun setUpUi() {
        val adapter = createAdapter()
        setUpRecyclerView(adapter)
        observeViewState(adapter)
        observeViewEffects()
    }

    private fun observeViewEffects() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewEffect.collect { actionTo(it) }
        }
    }

    private fun actionTo(effects: ProductViewEffects) {
        when (effects) {
            is ProductViewEffects.Failure -> handleFailure(effects.failure)
            ProductViewEffects.NavigateToProductDetails -> navigateToProductDetails()
        }
    }

    private fun handleFailure(failure: Event<Throwable>) {
        val unhandledFailure = failure.getContentIfNotHandled() ?: return
        val defaultFailureMessage = getString(R.string.default_failure_message)
        val snackBarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
            defaultFailureMessage
        } else {
            unhandledFailure.message!!
        }
        Snackbar.make(requireView(), snackBarMessage, Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateToProductDetails() {
        findNavController().navigate(navigationAction)
    }

    private fun observeViewState(adapter: ProductsAdapter) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { renderState(it, adapter) }
        }
    }

    private fun renderState(viewState: ProductViewState, adapter: ProductsAdapter) {

        with(binding) {
            productListProgressBar.isVisible = viewState.loading
            productListRecyclerView.isVisible = viewState.showList
            productListNoProductsMessage.isVisible = viewState.productsNotFound
            adapter.submitList(viewState.products)
        }

    }

    private fun setUpRecyclerView(productsListAdapter: ProductsAdapter) {
        binding.productListRecyclerView.apply {
            adapter = productsListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun createAdapter(): ProductsAdapter = ProductsAdapter(::clickOnProduct)

    private fun clickOnProduct(detailUrl: String) {
        if (detailUrl.isEmpty()) {
            showNoProductDetailPrompt()
        } else {
            requestProductDetails(detailUrl)
        }
    }

    private fun requestProductDetails(detailUrl: String) {
        viewModel.onEvent(ProductsEvents.RequestProductDetails(detailUrl))
    }

    private fun showNoProductDetailPrompt() {
        Snackbar.make(
            requireView(),
            getString(R.string.found_product_has_not_details),
            Snackbar.LENGTH_SHORT
        ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}