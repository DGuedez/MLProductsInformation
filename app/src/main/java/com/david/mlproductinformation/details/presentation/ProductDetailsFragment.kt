package com.david.mlproductinformation.details.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.david.mlproductinformation.databinding.FragmentProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
        observeViewState()
        retrieveProductDetails()
    }

    private fun retrieveProductDetails() {
        viewModel.onEvent(ProductDetailsEvents.RetrieveProductProductDetails)
    }

    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { render(it) }
        }
    }

    private fun render(viewState: ProductDetailsViewState) {
        with(viewState) {
            updateWebViewContent(details)
        }
    }

    private fun updateWebViewContent(detailsUrl: String) {
        binding.detailsProductWebview.loadUrl(detailsUrl)
    }

    private fun setUpWebView() {
        binding.detailsProductWebview.apply {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.detailsProductProgressbar.apply {
                        progress = 0
                        visibility = View.VISIBLE
                        incrementProgressBy(newProgress)
                    }
                    if (newProgress == 100) {
                        binding.detailsProductProgressbar.visibility = View.GONE
                    }
                }
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}