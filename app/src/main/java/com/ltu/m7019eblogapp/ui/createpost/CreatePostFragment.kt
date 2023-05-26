package com.ltu.m7019eblogapp.ui.createpost

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ltu.m7019eblogapp.R
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.data.service.CategorySearchBody
import com.ltu.m7019eblogapp.data.service.TagSearchBody
import com.ltu.m7019eblogapp.databinding.FragmentCreatePostBinding
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.SubmittablePost
import com.ltu.m7019eblogapp.model.Tag
import com.ltu.m7019eblogapp.ui.login.UserSessionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CreatePostFragment : DialogFragment() {

    private var _binding: FragmentCreatePostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val _container = DefaultAppContainer()

    // Selected tags, key of each tag is THE NAME, not the ID
    // TODO: Not a scalable sollution, but suitable for a mini-project
    private val selectedTags = HashMap<String, String>()
    private var selectedCategory : Category? = null

    private val userSession : UserSessionViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Set dialog style
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // Inflate the layout for the dialog
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.fragment_create_post, null)

        // Set dialog width and height
        val width = resources.displayMetrics.widthPixels * 9 / 10 // Adjust the width as per your requirement
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.setContentView(view)
        dialog.window?.setLayout(width, height)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val createPostViewModel =
            ViewModelProvider(this).get(CreatePostViewModel::class.java)

        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.titleCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.titleCreate.error = null
            }
        }
        binding.contentCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.contentCreate.error = null
            }
        }
        binding.mediaCreate.editText?.addTextChangedListener {
            if(it.toString().trim().isNotBlank()){
                binding.mediaCreate.error = null
            }
        }

        initCategoryAutoFill()

        initTagAutoFill()

        val submitBtn = binding.submitCreate
        submitBtn.setOnClickListener {
            submitPost()
        }

        return root
    }

    // --- Multiple tag select helper functions ----
    // Inspired by
    // https://github.com/krrishnaaaa/ChipDemo

    private fun addChip(tag: Tag){
        selectedTags[tag.name] = tag.id
        binding.selectedTagsCreate.addView(getChip(tag))
    }

    private fun getChip(tag: Tag) : Chip {
        return Chip(requireContext()).apply {
            text = tag.name
            isCloseIconVisible = true
            setOnCloseIconClickListener{
                selectedTags.remove((it as Chip).text)
                (it.parent as ChipGroup).removeView(it)
            }
        }
    }

    private fun initCategoryAutoFill(){
        // =========================================================================================
        // ---------------------------- CATEGORY AUTOFILL ------------------------------------------
        // =========================================================================================

        val initialCategories : MutableList<Category> = mutableListOf()
        lifecycleScope.launch {
            initialCategories.addAll(_container.blogRepository.getCategories(1))
        }

        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, initialCategories)
        val categorySelectView = binding.categoryCreate.editText as? AutoCompleteTextView

        categorySelectView?.setAdapter(categoryAdapter)

        categorySelectView?.setOnItemClickListener { parent, _, position, _ ->
            // Handle the selected category
            selectedCategory = parent.getItemAtPosition(position) as Category
            binding.categoryCreate.error = null
            println("USER SELECTED ${selectedCategory!!.name} AS CATEGORY")
        }

        categorySelectView?.addTextChangedListener {
            val query = it.toString().trim()
            if (query.isNotBlank()) {
                binding.categoryCreate.error = null
                // Fetch categories based on the search query
                lifecycleScope.launch {
                    try {
                        val searchObject = CategorySearchBody(Category(name=query))
                        val categories = _container.blogRepository.searchCategory(searchObject)
                        // TODO: We could clear here for scalability, but this is a mini-project :)
                        categoryAdapter.addAll(categories)
                        categoryAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        // Handle API request failure
                        println(e.message.toString())
                    }
                }
            } else {
                selectedCategory = null
                categorySelectView.clearListSelection()
            }

        }



    }

    private fun initTagAutoFill(){
        // ========================================================================================
        // -------------------------------- TAG AUTOFILL ------------------------------------------
        // ========================================================================================

        val initialTags : MutableList<Tag> = mutableListOf()
        lifecycleScope.launch {
            initialTags.addAll(_container.blogRepository.getTags(1))
        }

        val tagAdapter = ArrayAdapter(requireContext(), R.layout.fragment_create_post_list_item, initialTags)

        val tagSelectView = (binding.tagCreate.editText as? AutoCompleteTextView)
        tagSelectView?.setAdapter(tagAdapter)

        tagSelectView?.setOnItemClickListener { parent, _, position, _ ->
            val selectedTag = parent.getItemAtPosition(position) as Tag
            if(selectedTags[selectedTag.name] != null){
                binding.tagCreate.error = "Tag already selected"
            } else {
                binding.tagCreate.error = null
                addChip(selectedTag)
            }
            tagSelectView.setText("")
            println("Selected tags: ${selectedTags.keys.toList()}")
        }

        tagSelectView?.addTextChangedListener {
            val query = it.toString().trim()

            if (query.isNotBlank() && query != "") {
                binding.tagCreate.error = null
                // Fetch tags based on the search query
                lifecycleScope.launch {
                    try {
                        val searchObject = TagSearchBody(Tag(name=query))
                        val tags = _container.blogRepository.searchTag(searchObject)
                        // TODO: We could clear here for scalability, but this is a mini-project :)

                        tagAdapter.addAll(tags)
                        tagAdapter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        // Handle API request failure
                        println(e.message.toString())
                    }
                }
            }
        }
    }

    private fun submitPost(){
        val title = binding.titleCreate.editText?.text.toString()
        val content = binding.contentCreate.editText?.text.toString()
        val media = binding.mediaCreate.editText?.text.toString()

        val tagIDs : MutableList<String> = mutableListOf()

        for(tag in selectedTags){
            tagIDs.add(tag.value)
        }


        lifecycleScope.launch {
            val validMedia = isLoadableImage(media)

            if(title.isNotBlank() && content.isNotBlank() && media.isNotBlank() && tagIDs.isNotEmpty() && selectedCategory != null && validMedia) {
                val post = SubmittablePost(
                    title, content, selectedCategory!!.id, tagIDs,media
                )
                    _container.blogRepository.createPost(userSession.accessToken!!, post)
                    findNavController().navigate(R.id.navigation_home)
                    dismiss()
                    println("POST SUBMITTED!")
                    println(post)


            } else {
                if (title.isBlank()) {
                    binding.titleCreate.error = "Please enter a title"
                } else {
                    binding.titleCreate.error = null
                }
                //--
                if (content.isBlank()) {
                    binding.contentCreate.error = "Please enter content"
                } else {
                    binding.contentCreate.error = null
                }
                //--
                if (media.isBlank()) {
                    binding.mediaCreate.error = "Please enter media"
                } else {
                    binding.mediaCreate.error = null
                }
                //--
                if (tagIDs.isEmpty()) {
                    binding.tagCreate.error = "Please add at least one tag"
                } else {
                    binding.tagCreate.error = null
                }
                //--
                if (selectedCategory == null) {
                    binding.categoryCreate.error = "Please select a category"
                } else {
                    binding.categoryCreate.error = null
                }
                //--
                if (!validMedia){
                    binding.mediaCreate.error = "Unable to get image, please enter a valid image URL"
                } else {
                    binding.mediaCreate.error = null
                }
            }
        }


    }

    /**
     * Checks if a given url string contains a loadable image
     */
    private suspend fun isLoadableImage(urlString: String): Boolean {
        // Launch new scope based on parent context
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null

            try {
                // Open a new connection
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                // Is the connection valid?
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Check if the content type is image
                    val contentType = connection.contentType
                    if (contentType?.startsWith("image") == true) {
                        // Attempt to parse input stream as an additional check
                        val inputStream = connection.inputStream
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        return@withContext bitmap != null
                    }
                }
            } catch (e: IOException) {
                // Error!
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }

            return@withContext false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}