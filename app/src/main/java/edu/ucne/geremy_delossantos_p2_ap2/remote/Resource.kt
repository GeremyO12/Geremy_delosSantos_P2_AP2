package edu.ucne.geremy_delossantos_p2_ap2.remote

sealed class Resource <T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T? = null): Resource<T>(data)
}