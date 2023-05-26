package com.ltu.m7019eblogapp.ui.faq

class FaqData {
    fun getData() : HashMap<String, String> {
        val outP = HashMap<String,String>()
        outP["Q: Why does this app exist?"] = "A: This app is the result of a mini-project in the course M7019E: Mobile app development"
        outP["Q: How was this app made?"] = "A: This app was written in kotlin using android studio"
        outP["Q: Who made this app?"] = "A: This app was made by Hamid and Elliot :)"
        outP["Q: Who made the backend?"] = "A: The backend is part of a project in the course M7011E, see the git for full details."
        return outP
    }
}