package com.ltu.m7019eblogapp.database

import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.Tag
import com.ltu.m7019eblogapp.model.User

/*
class MockDatabase {
    val users = mutableListOf<User>()
    val posts = mutableListOf<Post>()
    val categories = mutableListOf<Category>()
    val tags = mutableListOf<Tag>()

    init{
        initUsers()
        initCategories()
        initTags()
        initPosts()
    }

    private fun initUsers(){
        users.add(
            User(
                "0",
                "Hejsan",
                "elliot@elliot.elliot",
            "https://dummyimage.com/600x600/"
            )
        )

        users.add(
            User(
                "1",
                "Svejsan",
                "Hamid@hamid.hamid",
                "https://dummyimage.com/600x600/000/fff"
            )
        )

        users.add(
            User(
                "2",
                "Tjosan",
                "example@example.com",
                "https://dummyimage.com/600x600/0ba/fff"
            )
        )
    }

    private fun initPosts(){
        posts.add(
            Post(
                "0",
                "En sick post",
                "0",
                "Lorem ipsum blablala tralallala sjuk text haha",
                users[0],
                categories[0],
                listOf(tags[0],tags[1],tags[2]),
            "https://dummyimage.com/1200x630/0ba/fff"
            )
        )

        posts.add(
            Post(
                "1",
                "Yep yep",
                "0",
                "Yep yep yep yep yep",
                users[1],
                categories[1],
                listOf(tags[1],tags[2],tags[3]),
                "https://dummyimage.com/1200x630/000/fff"
            )
        )

        posts.add(
            Post(
                "0",
                "Sjukt galet",
                "0",
                "Extremt sjukt galet det är helt sjukt hur galet sjukt det är",
                users[2],
                categories[2],
                listOf(tags[2],tags[3],tags[4]),
                "https://dummyimage.com/1200x630/fff/000"
            )
        )
    }

    private fun initCategories(){
        categories.add(
            Category("0", "Bilar")
        )

        categories.add(
            Category("1", "Båtar")
        )

        categories.add(
            Category("2", "Flygplan")
        )

        categories.add(
            Category("3", "Annat")
        )

        categories.add(
            Category("4", "Övrigt")
        )

        categories.add(
            Category("5", "Mat")
        )
    }

    private fun initTags(){
        tags.add(
            Tag("0", "Brum")
        )

        tags.add(
            Tag("1", "Brrrr")
        )

        tags.add(
            Tag("2", "Wooosh")
        )

        tags.add(
            Tag("3", "Öööh")
        )

        tags.add(
            Tag("4", "Något")
        )

        tags.add(
            Tag("5", "MMmm")
        )
    }
}

 */