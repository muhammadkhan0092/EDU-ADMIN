package com.example.eduadmin.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduadmin.data.MessageModel
import com.example.eduadmin.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SupportDetailViewModel(val firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore) : ViewModel(){

    private val _add = MutableStateFlow<Resource<MessageModel>>(Resource.Unspecified())
    val add : StateFlow<Resource<MessageModel>>
        get() = _add.asStateFlow()

    private val _retreive = MutableStateFlow<Resource<MessageModel>>(Resource.Unspecified())
    val retreive : StateFlow<Resource<MessageModel>>
        get() = _retreive.asStateFlow()

    var uid:String = ""
    val messagesCollection = firestore.collection("chats")


    fun retrieveMessages(userId: String) {
        var messageListenerRegistration: ListenerRegistration? = null
        messageListenerRegistration = messagesCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _retreive.emit(Resource.Error(error.message.toString()))
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    Log.d("khan","retreived succesfully")
                    val messages = snapshot.toObject(MessageModel::class.java)
                    if(messages!=null){
                        val msges = messages.messages
                        Log.d("khan","size is ${msges.size}")
                        viewModelScope.launch {
                            _retreive.emit(Resource.Success(messages))
                        }
                    }
                    else
                    {

                    }

                } else {

                }
            }
    }

    fun addNewMessage(messageModel : MessageModel,userId: String) {
        messagesCollection.document(userId).set(messageModel)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _add.emit(Resource.Success(messageModel))
                }
            }
            .addOnFailureListener { error ->
                viewModelScope.launch {
                    _add.emit(Resource.Error(error.message.toString()))
                }
            }
    }


}