package com.example.storyapp.ui.story

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.storyapp.data.repository.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel (application: Application): AndroidViewModel(application){
    private val repository = Repository(application)

    fun getStories(token: String) = repository.getAllStories(token)
    fun addStories(token: String, file: MultipartBody.Part, description: RequestBody) = repository.addNewStory(token,file,description)

}