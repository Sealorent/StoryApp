package com.example.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.data.Resource
import com.example.storyapp.data.response.GetStoriesResponse
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ProfileActivity
import com.example.storyapp.ui.story.StoryAdapter
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.ui.story.add.AddStoryActivity
import com.example.storyapp.util.SessionManager
import com.example.storyapp.util.ViewStateCallback

class MainActivity : AppCompatActivity() , ViewStateCallback<GetStoriesResponse>{
    private lateinit var mainBinding: ActivityMainBinding
    private val viewModel by viewModels<StoryViewModel>()
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var pref: SessionManager
    private var token: String? = null
    private var nameUser: String? = null


    companion object {

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        storyAdapter = StoryAdapter()
        mainBinding.rvStories.apply {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL  ,false)
        }

        pref = SessionManager(this)
        token = pref.getToken
        nameUser = pref.getUserName
        print(token.toString())

        getAllStories(token.toString())
        initAction()
    }


    private fun initAction() {
        mainBinding.fabNewStory.setOnClickListener {
            AddStoryActivity.start(this)
        }
        mainBinding.btnAccount.setOnClickListener {
            ProfileActivity.start(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
    private fun getAllStories(token:String){
        viewModel.getStories(token).observe(this){
            when(it){
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> it.data?.let{ it1 ->onSuccess(it1) }
            }
        }
    }

    override fun onSuccess(data: GetStoriesResponse) {
        var stories = data.listStory
        storyAdapter.setAllData(stories)
        mainBinding.apply {
            progressBar.visibility = invisible
            textError.visibility = invisible
            name.text = nameUser
        }
    }

    override fun onLoading() {
        mainBinding.apply {
            progressBar.visibility = visible
        }
    }


    override fun onFailed(message: String?) {
        mainBinding.apply {
            if (message == null) {
                textError.apply {
                    text = resources.getString(R.string.story_not_found)
                    visibility = visible
                }
            } else {
                textError.apply {
                    text = message
                    visibility = visible
                }
            }
            progressBar.visibility = invisible
            rvStories.visibility = invisible
        }
    }

}