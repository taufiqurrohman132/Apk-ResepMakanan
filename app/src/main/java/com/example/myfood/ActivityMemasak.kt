package com.example.myfood

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfood.databinding.ActivityMemasakBinding

class ActivityMemasak : AppCompatActivity() {
    private lateinit var food: ArrayList<FoodOverview>
    private lateinit var photoFoodOverview: FoodOverview
    private var dataPhoto: FoodMemasak? = null
    private var dataMemasak: Food? = null
    private lateinit var binding: ActivityMemasakBinding

    companion object{
        const val SCROLL_POSITION = "scroll_position"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val scrollPosition = binding.svMemasak.scrollY
        outState.putInt(SCROLL_POSITION, scrollPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0)
        binding.svMemasak.post {
            binding.svMemasak.scrollTo(0, scrollPosition)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemasakBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataPhoto = intent.getParcelableExtra("EXTRA_PHOTO_OVERVIEW_MEMASAK")
        dataMemasak = intent.getParcelableExtra("EXTRA_DATA_BAHAN_MEMASAK")

        val text = dataMemasak?.dataBahan ?: "salah"
        Log.i("dataBahan", "onCreate: $text ")
        val dataLangkah = dataMemasak?.dataIntruksi ?: ""
        binding.tvDataBahan.text = text
        binding.tvLangkahMemasak.text = dataLangkah
        binding.imgvBottomMemasak.setImageResource(dataMemasak?.img ?: 0)

        getImageTop(dataMemasak?.photoTopMemasak ?: 0)

        val tvBahan = binding.tvBahan.text.toString()
        val tvLangkah = binding.tvLangkah.text.toString()
        val tvDataBahan = binding.tvDataBahan.text.toString()
        val tvDataLangkah = binding.tvLangkahMemasak.text.toString()
        val bgShareClick = ContextCompat.getColor(this, R.color.abu_abu_white_bg)
        val bgShare = ContextCompat.getColor(this, R.color.white)
        binding.actionShareMemasak.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    //mengecil
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .start()
                    binding.actionShareMemasak.setBackgroundColor(bgShareClick)
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
                    binding.actionShareMemasak.setBackgroundColor(bgShare)
                    shareAllContent(tvBahan, tvLangkah, tvDataBahan, tvDataLangkah)
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

    private fun getImageTop(photoTop: Int){
        binding.imgvTopMemasak.setImageResource(photoTop)
//        binding.imgvTopMemasak.

    }

    private fun shareAllContent(bahan: String, langkah: String, dataBahan: String, dataLangkah: String){
        val shareText = "*$bahan*\n$dataBahan\n\n*$langkah*\n$dataLangkah"

        //intent untuk berbagi
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan Melalui"))
    }
}