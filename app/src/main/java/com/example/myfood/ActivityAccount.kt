package com.example.myfood

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myfood.databinding.ActivityAccountBinding

class ActivityAccount : AppCompatActivity() {
    private lateinit var bindingAccount: ActivityAccountBinding

    companion object{
        const val SCROLL_POSITION = "scroll_position"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val scrollPosition = bindingAccount.svAccount.scrollY
        outState.putInt(SCROLL_POSITION, scrollPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0)
        bindingAccount.svAccount.post {
            bindingAccount.svAccount.scrollTo(0, scrollPosition)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingAccount = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(bindingAccount.root)

        Glide.with(this)
            .load(R.drawable.icon_profile)
            .circleCrop()
            .into(bindingAccount.ivProfile)

        val bgClick = ContextCompat.getColor(this, R.color.abu_abu_white_bg)
        val bg = ContextCompat.getColor(this, R.color.white)
        bindingAccount.imgFb.setOnTouchListener { view, motionEvent ->
            //alamat brouser
            val uri = Uri.parse("https://www.facebook.com/riders.ranger")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    //mengecil
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .start()
                    bindingAccount.imgFb.setBackgroundColor(bgClick)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    //kembali ke ukuran
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    //aksi
                    bindingAccount.imgFb.setBackgroundColor(bg)
                    //coba buka dengan aplikasi instagram
                    intent.setPackage("com.facebook.android")

                    try {
                        startActivity(intent)
                    }catch (e: ActivityNotFoundException){
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()
                    true
                }
                else -> false
            }
        }
        bindingAccount.imgIg.setOnTouchListener { view, motionEvent ->
            //alamat brouser
            val uri = Uri.parse("https://www.instagram.com/rohmanz.t")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    //mengecil
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .start()
                    bindingAccount.imgIg.setBackgroundColor(bgClick)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    //kembali ke ukuran
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    //aksi
                    bindingAccount.imgIg.setBackgroundColor(bg)
                    //coba buka dengan aplikasi instagram
                    intent.setPackage("com.instagram.android")

                    try {
                        startActivity(intent)
                    }catch (e: ActivityNotFoundException){
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()
                    true
                }
                else -> false
            }
        }
        bindingAccount.imgWa.setOnTouchListener { view, motionEvent ->
            //alamat brouser
            val phoneNumber = "62085738784885"
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    //mengecil
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .start()
                    bindingAccount.imgWa.setBackgroundColor(bgClick)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    //kembali ke ukuran
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    //aksi
                    bindingAccount.imgWa.setBackgroundColor(bg)

                    try {
                        startActivity(intent)
                    }catch (e: ActivityNotFoundException){
                        e.printStackTrace()
                    }
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()
                    true
                }
                else -> false
            }
        }

    }
}