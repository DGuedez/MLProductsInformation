package com.david.mlproductinformation.common.presentation

interface UIMapper<E, V> {
    fun mapToView(input: E): V
}