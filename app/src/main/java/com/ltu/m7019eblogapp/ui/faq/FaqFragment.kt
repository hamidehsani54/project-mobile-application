package com.ltu.m7019eblogapp.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ltu.m7019eblogapp.databinding.FragmentFaqBinding
import com.ltu.m7019eblogapp.ui.profile.ProfileFragmentDirections

// Fragment representing the FAQ screen
class FaqFragment : Fragment() {

    private var _binding: FragmentFaqBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Called when the fragment view is being created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Create an instance of FaqListAdapter with the current context
        val listAdapter = FaqListAdapter(this.requireContext())

        // Set the adapter for the faqList RecyclerView
        binding.faqList.setAdapter(listAdapter)


        // Return the root view for the fragment
        return root
    }

    // Called when the fragment view is being destroyed
    override fun onDestroyView() {
        super.onDestroyView()

        // Clear the binding reference to avoid memory leaks
        _binding = null
    }
}
