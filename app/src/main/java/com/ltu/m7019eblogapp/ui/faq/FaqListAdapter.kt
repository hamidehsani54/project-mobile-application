package com.ltu.m7019eblogapp.ui.faq

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.ltu.m7019eblogapp.R

// Adapter for the FAQ list
class FaqListAdapter(private val context: Context) : BaseExpandableListAdapter() {

    // Lists to hold the questions and answers
    private val questionsList: List<String> = FaqData().getData().keys.toList()
    private val answersMap: HashMap<String, String> = FaqData().getData()

    // Return the number of question groups
    override fun getGroupCount(): Int {
        return questionsList.size
    }

    // Return the number of answers for a given question group
    override fun getChildrenCount(groupPosition: Int): Int {
        return 1 // Only one answer per question
    }

    // Return the question at the specified group position
    override fun getGroup(groupPosition: Int): Any {
        return questionsList[groupPosition]
    }

    // Return the answer at the specified group and child position
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return answersMap[questionsList[groupPosition]]!!
    }

    // Return the ID of the question group
    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    // Return the ID of the answer child
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0 // Only one child per group
    }

    // Indicate whether the IDs are stable across changes
    override fun hasStableIds(): Boolean {
        return false // TODO: Is this true?
    }

    // Return the view for the question group
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.fragment_faq_list_item, parent, false)
        }

        // Set the question text for the group
        val questionTextView = view?.findViewById<TextView>(R.id.faq_list_item)
        questionTextView?.text = questionsList[groupPosition]

        return view!!
    }

    // Return the view for the answer child
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent?.context)
            view = inflater.inflate(R.layout.fragment_faq_list_item, parent, false)
        }

        // Set the answer text for the child
        val answerTextView = view?.findViewById<TextView>(R.id.faq_list_item)
        val question = questionsList[groupPosition]
        val answer = answersMap[question]
        answerTextView?.text = answer

        return view!!
    }

    // Indicate whether the answer child is selectable
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false // TODO: Test :)
    }
}
