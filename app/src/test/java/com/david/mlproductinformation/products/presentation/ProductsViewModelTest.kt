package com.david.mlproductinformation.products.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.david.mlproductinformation.common.data.ProductInfoRepository
import com.david.mlproductinformation.common.domain.NetworkException
import com.david.mlproductinformation.common.domain.model.Installment
import com.david.mlproductinformation.common.domain.model.Product
import com.david.mlproductinformation.common.domain.model.Shipping
import com.david.mlproductinformation.common.utils.DispatchersProvider
import com.david.mlproductinformation.products.domain.usecases.RequestProductsUseCase
import com.david.mlproductinformation.products.domain.usecases.StoreProductDetailsUrlUseCase
import com.david.mlproductinformation.products.presentation.mappers.UIProductMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After

import org.junit.Before
import org.junit.Rule
import org.junit.Test


@Suppress("DEPRECATION")
class ProductsViewModelTest {

    lateinit var viewModel: ProductsViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    @MockK
    lateinit var repository: ProductInfoRepository

    lateinit var requestProductsUseCase: RequestProductsUseCase

    lateinit var storeProductDetailsUrlUseCase: StoreProductDetailsUrlUseCase

    lateinit var uiProductMapper: UIProductMapper

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var product1: Product

    private lateinit var product2: Product

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        setUpDispatcher()
        setProductValues()
        setUpViewModel()

    }

    private fun setProductValues() {
        product1 = Product(
            id = "1",
            title = "title1",
            price = 12000,
            thumbnail = "thumbnailurl",
            installments = Installment.KNOWN(quantity = 5, amount = 1.01F),
            detailsUrl = "urlProduct1",
            Shipping(freeShipping = false)
        )

        product2 = Product(
            id = "2",
            title = "title2",
            price = 242000,
            thumbnail = "thumbnailurl",
            installments = Installment.KNOWN(quantity = 1, amount = 2.01F),
            detailsUrl = "urlProduct2",
            Shipping(freeShipping = false)
        )
    }

    private fun setUpDispatcher() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }


    @After
    fun dropDown() {
        resetDispatcher()
    }

    private fun resetDispatcher() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }


    private fun setUpViewModel() {
        uiProductMapper = UIProductMapper()

        requestProductsUseCase = spyk(RequestProductsUseCase(repository))

        storeProductDetailsUrlUseCase = spyk(StoreProductDetailsUrlUseCase(repository))

        val dispatchersProvider = object : DispatchersProvider {
            override fun io() = Dispatchers.Main
        }

        viewModel = ProductsViewModel(
            dispatchersProvider,
            requestProductsUseCase,
            uiProductMapper,
            storeProductDetailsUrlUseCase
        )
    }

    @Test
    fun `onRequestProductDetails is called and product list with content is retrieved`() {
        testCoroutineScope.runBlockingTest {
            //Given
            val apiResult = listOf(product1, product2)
            val expectedRemoteProducts = apiResult.map { uiProductMapper.mapToView(it) }

            val expectedViewState = ProductViewState(
                loading = false,
                expectedRemoteProducts,
                productsNotFound = false
            )
            coEvery { repository.getProducts() } returns apiResult

            //When
            val requestProductsEvent = ProductsEvents.RequestProductsFromSearch

            viewModel.onEvent(requestProductsEvent)

            //Then
            coVerify(exactly = 1) { requestProductsUseCase() }

            val viewState = viewModel.viewState.value
            val productListResult = viewState.products

            assertThat(productListResult).isEqualTo(expectedViewState.products)
        }
    }


    @Test
    fun `onRequestProductDetails is called , detailurl variable is stored and NavigateToProductDetails effect is triggered`() {
        testCoroutineScope.runBlockingTest {
            //Given
            val apiResult = listOf(product1, product2)
            val expectedRemoteProducts = apiResult.map { uiProductMapper.mapToView(it) }

            coEvery { repository.getProducts() } returns apiResult

            coEvery { repository.storeProductDetailUrl(product1.detailsUrl) } just Runs

            //When
            val requestProductsDetailsEvent =
                ProductsEvents.RequestProductDetails(product1.detailsUrl)
            viewModel.onEvent(requestProductsDetailsEvent)

            //Then
            coVerify(exactly = 1) { storeProductDetailsUrlUseCase(product1.detailsUrl) }

            val job = launch {
                val result = viewModel.viewEffect.first()
                assertThat(result).isInstanceOf(ProductViewEffects.NavigateToProductDetails::class.java)
            }
            job.cancel()
        }
    }

    @Test
    fun `onRequestProductDetails is called and network Exception is thrown`() {
        testCoroutineScope.runBlockingTest {
            //Given
            val failureMessage = "messageFailure"
            val expectedFailure = NetworkException(failureMessage)

            coEvery { repository.getProducts() } throws expectedFailure

            //When
            viewModel.onEvent(ProductsEvents.RequestProductsFromSearch)

            coVerify(exactly = 1) { requestProductsUseCase() }

            val job = launch {
                val result = viewModel.viewEffect.first()
                assertThat(result).isInstanceOf(ProductViewEffects.Failure::class.java)
            }
            job.cancel()

        }
    }


}