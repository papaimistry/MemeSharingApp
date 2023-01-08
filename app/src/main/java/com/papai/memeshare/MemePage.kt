package com.papai.memeshare

import android.content.Intent

import android.graphics.drawable.BitmapDrawable

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

import android.util.Log

import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_meme_page.*


@Suppress("DEPRECATION")
class MemePage : AppCompatActivity() {


    private val url = "https://meme-api.com/gimme"
    private  lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_page)

        imageView = findViewById(R.id.imageView)

        lodeMeme()


        button_Next.setOnClickListener{
            lodeMeme()
        }
        button_Share.setOnClickListener{
            val bitmapDrawable = imageView.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver,bitmap,"Title",null)

            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="image/jpeg"
            intent.putExtra(Intent.EXTRA_STREAM,bitmapUri)
            startActivity(Intent.createChooser(intent,"Share This Meme To..."))
        }

    }

    private fun lodeMeme() {
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET,this.url,null,{
            response ->
            Log.d("Result",response.toString())

            Picasso.get().load(response.get("url").toString()).placeholder(R.drawable.lod).into(imageView)
        },
            {
                Log.d("error",it.toString())
                Toast.makeText(applicationContext,"Loading Error",Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
        }

    }
