package com.example.eduadmin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eduadmin.adapters.SupportDetailAdapter
import com.example.eduadmin.data.MessageModel
import com.example.eduadmin.data.SingleMessage
import com.example.eduadmin.databinding.FragmentSupportDetailBinding
import com.example.eduadmin.utils.Resource
import com.example.eduadmin.vm.SupportDetailViewModel
import com.example.eduadmin.vmf.SupportDetailFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class FragmentSupportDetail : Fragment(){
    private lateinit var binding: FragmentSupportDetailBinding
    private lateinit var messageModel: MessageModel
    private lateinit var supportDetailAdapter: SupportDetailAdapter
    private lateinit var messages: MutableList<SingleMessage>
    private val navArgs by navArgs<FragmentSupportDetailArgs>()
    private val viewModel by viewModels<SupportDetailViewModel> {
        val firestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        SupportDetailFactory(firebaseAuth,firestore)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMessageAdapter()
        observeMessageSent()
        observeMessagesRetreived()
        onClickListeners()
    }

    private fun retreiveMessages() {
        messageModel = navArgs.message
        supportDetailAdapter.differ.submitList(messageModel.messages)
        viewModel.uid = messageModel.userId
        viewModel.retrieveMessages(messageModel.userId)
    }

    private fun setupMessageAdapter() {
        supportDetailAdapter = SupportDetailAdapter()
        binding.rv.adapter = supportDetailAdapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        layoutManager.stackFromEnd = true
        binding.rv.layoutManager = layoutManager
        retreiveMessages()
    }

    private fun onClickListeners() {
        binding.button6.setOnClickListener {
            val msg = binding.editTextText3.text.toString()
            binding.editTextText3.text.clear()
            messages.add(SingleMessage(getRandomId(),"admin",msg))
            messageModel.messages = messages
            viewModel.addNewMessage(messageModel,messageModel.userId)
        }
    }

    private fun getRandomId(): String {
        return UUID.randomUUID().toString()
    }

    private fun observeMessagesRetreived() {
        lifecycleScope.launch {
            viewModel.retreive.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressBar13.visibility = View.INVISIBLE
                    }
                    is Resource.Loading -> {
                        binding.progressBar13.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        Log.d("khan","newmesssages ${messageModel.messages.size}")
                        messageModel = it.data!!
                        messages = messageModel.messages.toMutableList()
                        val newMsges  = messageModel.messages.toMutableList()
                        supportDetailAdapter.differ.submitList(newMsges){
                            binding.rv.post {
                                binding.rv.scrollToPosition(supportDetailAdapter.itemCount - 1)
                            }
                        }
                        binding.rv.scrollToPosition(supportDetailAdapter.itemCount -1)
                        binding.progressBar13.visibility = View.INVISIBLE
                    }
                    is Resource.Unspecified -> {

                    }
                }
            }
        }
    }


    private fun observeMessageSent() {
        lifecycleScope.launch {
            viewModel.add.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.progressBar13.visibility = View.INVISIBLE
                    }
                    is Resource.Loading -> {
                        binding.progressBar13.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar13.visibility = View.INVISIBLE
                    }
                    is Resource.Unspecified -> {

                    }
                }
            }
        }
    }



}