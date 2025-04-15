package com.example.eduadmin.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.data.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupportViewModel(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore) : ViewModel(){


    private val _get = MutableStateFlow<Resource<List<MessageModel>>>(Resource.Unspecified())
    val get : StateFlow<Resource<List<MessageModel>>>
        get() = _get.asStateFlow()





    fun getChats(){
        val list : MutableList<MessageModel> = mutableListOf()
        firestore.collection("chats").get()
            .addOnSuccessListener {
                it.forEach {
                    val data = it.toObject(MessageModel::class.java)
                    list.add(data)
                }
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