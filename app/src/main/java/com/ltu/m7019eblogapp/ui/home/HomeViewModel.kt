package com.ltu.m7019eblogapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.data.service.PostSearchBody
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.Tag
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Initialize dependencies
    private val _container: DefaultAppContainer = DefaultAppContainer()

    // LiveData for post list, category list, tag list, and data fetch status
    private val _postList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>>
        get() {
            return _postList
        }

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>>
        get() {
            return _categoryList
        }

    private val _tagList = MutableLiveData<List<Tag>>()
    val tagList: LiveData<List<Tag>>
        get() {
            return _tagList
        }

    private val _dataFetchStatus = MutableLiveData<DataFetchStatus>()
    val dataFetchStatus: LiveData<DataFetchStatus>
        get() {
            return _dataFetchStatus
        }

    private var currentSet: Int = 1

    init {
        // Fetch initial data on ViewModel initialization
        getPosts()
        getCategories()
        getTags()
    }

    // Example text LiveData
    private val _text = MutableLiveData<String>().apply {
        value = "This is the browse Fragment"
    }
    val text: LiveData<String> = _text

    // LiveData for navigation to post details
    private val _navigateToPost = MutableLiveData<Post?>()
    val navigateToPost: MutableLiveData<Post?>
        get() {
            return _navigateToPost
        }

    // Fetch posts from the repository
    fun getPosts() {
        _dataFetchStatus.value = DataFetchStatus.LOADING
        viewModelScope.launch {
            try {
                _postList.value = _container.blogRepository.getPosts(currentSet)
                _text.value = "Success! Loaded ${postList.value?.size} results"
                _dataFetchStatus.value = DataFetchStatus.DONE
            } catch (e: Exception) {
                _postList.value = listOf()
                _text.value = "Error: ${e.message}"
                _dataFetchStatus.value = DataFetchStatus.ERROR
            }
        }
    }

    // Update posts with filter (selected category and tags)
    fun updatePostsWithFilter(selectedCategory: Category?, selectedTags: List<Tag>) {
        _dataFetchStatus.value = DataFetchStatus.LOADING
        if (selectedCategory != null || selectedTags.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val categoryID: String = selectedCategory?.id.orEmpty()
                    val tagIDs: MutableList<String> = mutableListOf()

                    for (tag in selectedTags) {
                        tagIDs.add(tag.id)
                    }

                    // Create a PostSearchBody object with selected category and tags
                    val body = PostSearchBody(
                        Post(
                            "",
                            "",
                            "",
                            "",
                            "",
                            categoryID,
                            tagIDs.toList(),
                            null,
                            null,
                            null,
                            ""
                        )
                    )
                    _postList.value = _container.blogRepository.searchPost(body)
                    _dataFetchStatus.value = DataFetchStatus.DONE
                } catch(e : Exception) {
                    _postList.value = listOf()
                    println("Error: ${e.message}")
                    _dataFetchStatus.value = DataFetchStatus.ERROR
                }
            }
        }
    }

    // Fetch categories from the repository
    private fun getCategories() {
        viewModelScope.launch {
            try {
                _categoryList.value = _container.blogRepository.getCategories(1)
            } catch (e: Exception) {
                println("Error: ${e.message}")
                _categoryList.value = listOf()
            }
        }
    }

    // Fetch tags from the repository
    private fun getTags() {
        viewModelScope.launch {
            try {
                _tagList.value = _container.blogRepository.getTags(1)
            } catch (e: Exception) {
                println("Error: ${e.message}")
                _tagList.value = listOf()
            }
        }
    }

    // Handle click on a post list item
    fun onPostListItemClicked(post: Post) {
        println("navigating to ${post.title}")
        _navigateToPost.value = post
    }

    // Reset navigation to post details
    fun onPostNavigationComplete() {
        println("nav complete!")
        _navigateToPost.value = null
    }
}
