package com.example.eduadmin.vmf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eduadmin.vm.UsersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersFactory(val firebaseAuth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(firebaseAuth,firebaseFirestore) as T
    }
}