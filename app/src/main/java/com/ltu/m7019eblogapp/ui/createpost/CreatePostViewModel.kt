package com.ltu.m7019eblogapp.ui.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the create post Fragment"
    }
    val text: LiveData<String> = _text
}