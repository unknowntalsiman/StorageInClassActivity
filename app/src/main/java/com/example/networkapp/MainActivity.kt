package com.example.networkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: com.android.volley.RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById(R.id.comicTitleTextView)
        descriptionTextView = findViewById(R.id.comicDescriptionTextView)
        numberEditText = findViewById(R.id.comicNumberEditText)
        showButton = findViewById(R.id.showComicButton)
        comicImageView = findViewById(R.id.comicImageView)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

        val cachedComicFile = File(cacheDir, "cached_comic.json")
        if (cachedComicFile.exists()) {
            val jsonString = cachedComicFile.readText()
            val comicObject = JSONObject(jsonString)
            showComic(comicObject)
        }
    }

    private fun downloadComic(comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add(
            JsonObjectRequest(url,
                { showComic(it) },
                { }
            )
        )
    }

    private fun showComic(comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)
        saveComic(comicObject)
    }

    private fun saveComic(comicObject: JSONObject) {
        val file = File(cacheDir, "cached_comic.json")
        file.writeText(comicObject.toString())
    }
}
