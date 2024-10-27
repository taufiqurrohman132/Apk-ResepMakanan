package com.example.myfood

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myfood.ListFoodAdapter.OnItemClickListener
import com.example.myfood.databinding.ActivityDetailsBinding

class ActivityDetails : AppCompatActivity() {
    private val listOverview = ArrayList<FoodOverview>()
    private val listDataPhotoMemasak = ArrayList<FoodMemasak>()
    private lateinit var bindingDetails: ActivityDetailsBinding
    private var data: Food? = null
    private lateinit var imgOverview: ArrayList<FoodOverview>
    private lateinit var imges: TypedArray
    private lateinit var photoMemasak: TypedArray
    private lateinit var foodMemasak: FoodMemasak
    private lateinit var onItemClickListener: OnItemClickListener

    companion object{
        const val EXTRA_DATA_PHOTO = "extra_data_photo"
        const val EXTRA_DATA_PHOTO_OVERVIEW = "extra_data_photo_overview"
        const val SCROLL_POSITION = "scroll_position"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val scrollPosition = bindingDetails.svDetail.scrollY
        outState.putInt(SCROLL_POSITION, scrollPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0)
        bindingDetails.svDetail.post {
            bindingDetails.svDetail.scrollTo(0, scrollPosition)
        }
    }

    @SuppressLint("Recycle", "ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        bindingDetails = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(bindingDetails.root)

        Glide.with(this)
            .load(R.drawable.ic_share)
            .circleCrop()
            .into(bindingDetails.actionShareDetail)

        data = intent.getParcelableExtra("EXTRA_DATA_PHOTO")
        Log.d("detail data", data?.name.toString())
        val img: Int = data?.img ?: 0
        bindingDetails.imgTopOverviw.setImageResource(img)
        bindingDetails.tvJudulOverview.text = data?.name.toString()
        bindingDetails.tvOverview.text = data?.description.toString()

        //berbagi
        val title = bindingDetails.tvJudulOverview.text.toString()
        val textOverview = bindingDetails.tvOverview.text.toString()
        val bgShareClick = ContextCompat.getColor(this, R.color.abu_abu_white_bg)
        val bgShare = ContextCompat.getColor(this, R.color.white)
        bindingDetails.actionShareDetail.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    //mengecil
                    view.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .start()
                    bindingDetails.actionShareDetail.setBackgroundColor(bgShareClick)
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
//                    onItemClickListener.onItemClicked(shareAllContent(title, textOverview))
                    bindingDetails.actionShareDetail.setBackgroundColor(bgShare)
                    shareAllContent(title, textOverview)
                    true
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

//                    bindingDetails.actionShareDetail.setBackgroundColor(R.color.transparant)
                    true
                }
                else -> false
            }

        }
//            shareAllContent(title, textOverview)

        val osengSawi = resources.obtainTypedArray(R.array.data_photo_overview_osengsawi1)
        val takoyaki = resources.obtainTypedArray(R.array.data_photo_overview_takoyaki2)
        val miGoreng = resources.obtainTypedArray(R.array.data_photo_overview_migoreng3)
        val cahTauge = resources.obtainTypedArray(R.array.data_photo_overview_cahtauge4)
        val miKangkung = resources.obtainTypedArray(R.array.data_photo_overview_mikangkung5)
        val telurKecap = resources.obtainTypedArray(R.array.data_photo_overview_telurceplok6)
        val telurKrispi = resources.obtainTypedArray(R.array.data_photo_overview_telurdadar7)
        val supBayam = resources.obtainTypedArray(R.array.data_photo_overview_supbayam8)
        val bakwan = resources.obtainTypedArray(R.array.data_photo_overview_bakwanbayam9)
        val tumisteri = resources.obtainTypedArray(R.array.data_photo_overview_tumisteri10)
        val sambalBjk = resources.obtainTypedArray(R.array.data_photo_overview_sambalbajak11)
        val cumiHijau = resources.obtainTypedArray(R.array.data_photo_overview_cumisambal12)
        val tempeMendo = resources.obtainTypedArray(R.array.data_photo_overview_tempemendoan13)
        val gembus = resources.obtainTypedArray(R.array.data_photo_overview_gembus14)
        val kentang = resources.obtainTypedArray(R.array.data_photo_overview_sambalgoreng15)


        val name = data?.name
        imges = when(name){
            "Oseng Sawi Putih Jagung" -> osengSawi
            "Takoyaki Mi" -> takoyaki
            "Mi Goreng" -> miGoreng
            "Cah Tauge dan Tahu" -> cahTauge
            "Mi Kangkung" -> miKangkung
            "Telur Ceplok Kecap" -> telurKecap
            "Telur Dadar Krispy" -> telurKrispi
            "Sup Bayam Jagung" -> supBayam
            "Bakwan Bayam" -> bakwan
            "Tumis Teri Medan Bawang" -> tumisteri
            "Sambal Bajak" -> sambalBjk
            "Cumi Sambal Hijau" -> cumiHijau
            "Tempe Mendoan Rawit" -> tempeMendo
            "Gembus Lombok Kethok" -> gembus
            else -> kentang
        }

        bindingDetails.rvImgOverview.setHasFixedSize(true)
        listOverview.addAll(getListFoodOverview())

        photoMemasak = resources.obtainTypedArray(R.array.photo_memasak)
        val dataPhotoMemasak = ArrayList<FoodMemasak>()
        for (k in 0..photoMemasak.length()-1) {
            Log.i("photolength", "onCreate: ${photoMemasak.length()} ")
            foodMemasak = FoodMemasak(photoMemasak.getResourceId(k, -1))
            dataPhotoMemasak.add(foodMemasak)
        }
        listDataPhotoMemasak.addAll(dataPhotoMemasak)

        bindingDetails.btnMulaiMemasak.setOnClickListener {
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                bindingDetails.btnMulaiMemasak,
                PropertyValuesHolder.ofFloat("scaleX", 0.95f),
                PropertyValuesHolder.ofFloat("scaleY", 0.95f)
            )
            scaleDown.duration = 100
            scaleDown.start()

            //aksi
            val goToMemasak = Intent(this, ActivityMemasak::class.java)
//            goToMemasak.putExtra("EXTRA_PHOTO_OVERVIEW", foodOverview)
            goToMemasak.putExtra("EXTRA_PHOTO_OVERVIEW_MEMASAK", foodMemasak)
            goToMemasak.putExtra("EXTRA_DATA_BAHAN_MEMASAK", data)
            startActivity(goToMemasak)

            //mengembalikan keukuran semula
            scaleDown.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    bindingDetails.btnMulaiMemasak.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                }
            })
        }

        showRecyclerListOverview()
    }

    private fun shareAllContent(title: String, textOverview: String){
        val shareText = "*$title*\n\n$textOverview"

        //intent untuk berbagi
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Bagikan Melalui"))

    }

    private fun showRecyclerListOverview(){
        bindingDetails.rvImgOverview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listOverviewAdapter = ListOverviewAdapter(listOverview)
        bindingDetails.rvImgOverview.adapter = listOverviewAdapter

        listOverviewAdapter.setOnItemClickListener(object : ListOverviewAdapter.OnItemClickListener{
            override fun onItemClicked(data: FoodOverview) {

            }
        })
    }

    @SuppressLint("Recycle")
    private fun getListFoodOverview() : ArrayList<FoodOverview>{
        val listFoodOver = ArrayList<FoodOverview>()
        for (j in 0..imges.length() -1){
            val foodOverview = FoodOverview(imges.getResourceId(j, -1))
            listFoodOver.add(foodOverview)
        }
        return listFoodOver
    }
}