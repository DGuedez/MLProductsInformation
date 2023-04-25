package com.david.mlproductinformation.common.domain

import java.io.IOException

class NetworkUnavailableException(message: String = "No hay servicio de red disponible.") : IOException(message)

class NetworkException(message: String): Exception(message)