package com.example.eduadmin.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduadmin.data.Course
import com.example.eduadmin.data.User
import com.example.eduadmin.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersViewModel(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore) : ViewModel(){
    private val _get = MutableStateFlow<Resource<List<User>>>(Resource.Unspecified())
    val get : StateFlow<Resource<List<User>>>
        get() = _get.asStateFlow()

    private val _set = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val set : StateFlow<Resource<User>>
        get() = _set.asStateFlow()


    fun getUsers() {
        viewModelScope.launch {
            _get.emit(Resource.Loading())
        }
        val list : MutableList<User> = mutableListOf()
        firestore.collection("teacher").get()
            .addOnSuccessListener {
                it.forEach {
                    val data = it.toObject(User::class.java)
                    list.add(data)
                }
                getTeachers(list)
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _get.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun getTeachers(list: MutableList<User>) {
        firestore.collection("student").get()
            .addOnSuccessListener {
                it.forEach {
                    val data = it.toObject(User::class.java)
                    list.add(data)
                }
                Log.d("khan","users is ${list.size}")
                viewModelScope.launch {
                    _get.emit(Resource.Success(list))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _get.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}