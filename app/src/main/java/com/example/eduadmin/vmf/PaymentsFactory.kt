package com.example.eduadmin.vmf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eduadmin.vm.PaymentsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentsFactory(val firebaseAuth: FirebaseAuth, val firebaseFirestore: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentsViewModel(firebaseAuth,firebaseFirestore) as T
    }
}