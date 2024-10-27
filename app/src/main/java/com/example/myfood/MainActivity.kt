package com.example.myfood

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.myfood.databinding.ActivityMainBinding
import com.example.myfood.databinding.ItemRowFoodBinding
import com.example.myfood.databinding.MenuAddLocationBinding

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<Food>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingFood: ItemRowFoodBinding
    private lateinit var adapter : ListFoodAdapter
    private lateinit var bindingMenu: MenuAddLocationBinding
    private var isGrid = false
    private var white: Int = 0
    private var orangeDark: Int = 0
    private var buttonBgColorList: Int = orangeDark
    private var buttonBgColorGrid: Int = white

    companion object{
        const val SCROLL_POSITION = "scroll_position"
        private const val STATE_SAVE = "state_save"
        private const val STATE_SAVE_LAYOUT = "state_save_layout"
    }

//    private fun showSelectedMode(mode: String){
//        Toast.makeText(this, "Mode "+ mode, Toast.LENGTH_SHORT).show()
//    }

    private fun setLayoutManager(isGrid: Boolean){
        binding.rvFood.animate().alpha(0f).setDuration(300).withEndAction {
            if (isGrid){
                binding.rvFood.layoutManager = GridLayoutManager(this, 2)
            }else{
                binding.rvFood.layoutManager = LinearLayoutManager(this)
            }

            binding.rvFood.animate().alpha(1f).setDuration(300).start()
        }.start()
        //simpan posisi scroll saat ini
        val currenScrollPosition = (binding.rvFood.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        binding.rvFood.scrollToPosition(currenScrollPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isGrid", isGrid)
        val layoutManagerState = binding.rvFood.layoutManager as LinearLayoutManager
        val stateBtnList = binding.btnList
        val stateBtnGrid = binding.btnGrid
        val scrollPosition = binding.rvFood.scrollY

        outState.putInt(SCROLL_POSITION, scrollPosition)
        outState.putInt("STATE_BUTTON_BGLIST",buttonBgColorList)
        outState.putInt("STATE_BUTTON_BGGRID", buttonBgColorGrid)


        outState.putInt("STATE_SAVE_LAYOUT", layoutManagerState.findFirstVisibleItemPosition())
        outState.putInt("STATE_BUTTON_TEXTLIST", stateBtnList.currentTextColor )
        outState.putInt("STATE_BUTTON_TEXTGRID", stateBtnGrid.currentTextColor )

        outState.putString("add_kota", binding.tvLocKota.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isGrid = savedInstanceState.getBoolean("isGrid", false)
        setLayoutManager(isGrid)

        val bgColorList = savedInstanceState.getInt("STATE_BUTTON_BGLIST", R.color.orange_dark)
        val textColorList = savedInstanceState.getInt("STATE_BUTTON_TEXTLIST", R.color.orange_light)
        val bgColorGrid = savedInstanceState.getInt("STATE_BUTTON_BGGRID", R.color.white)
        val textColorGrid = savedInstanceState.getInt("STATE_BUTTON_TEXTGRID", R.color.orange_light)
        val addKota = savedInstanceState.getString("add_kota", "Lamongan")
        val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION, 0)
        binding.rvFood.post {
            binding.rvFood.scrollTo(0, scrollPosition)
        }

        Log.i("restore", "onRestoreInstanceState: ")
        binding.btnList.setBackgroundColor(bgColorList)
        binding.btnList.setTextColor(textColorList)
        binding.btnGrid.setBackgroundColor(bgColorGrid)
        binding.btnGrid.setTextColor(textColorGrid)

        binding.tvLocKota.text = addKota
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inisiallisasi binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingFood = ItemRowFoodBinding.inflate(layoutInflater)
        bindingMenu = MenuAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFood.setHasFixedSize(true)

        white = ContextCompat.getColor(this, R.color.white)
        orangeDark = ContextCompat.getColor(this, R.color.orange_dark)
        buttonBgColorList = white
        buttonBgColorGrid = orangeDark

        //jika tidak ada state yang disimpan , maka set warana
        if (savedInstanceState == null){
            binding.btnList.setBackgroundColor(white)
            binding.btnGrid.setBackgroundColor(orangeDark)
        } else{
            //restore
            buttonBgColorList = savedInstanceState.getInt("STATE_BUTTON_BGLIST", ContextCompat.getColor(this, R.color.orange_dark))
            buttonBgColorGrid = savedInstanceState.getInt("STATE_BUTTON_BGGRID", ContextCompat.getColor(this, R.color.white))
        }

        binding.btnList.setTextColor(resources.getColor(R.color.orange_light))
        binding.btnGrid.setTextColor(resources.getColor(R.color.white))

        binding.rvFood.layoutManager = LinearLayoutManager(this)
        binding.rvFood.itemAnimator = DefaultItemAnimator()

        binding.tvLocKota.text = "Lamongan"
        binding.tvLocKota.setTypeface(null, Typeface.BOLD)

        list.addAll(getListFood())

        //restore scroll position jika ada
        if(savedInstanceState != null){
            val scrollPosition = savedInstanceState.getInt("STATE_SAVE_LAYOUT", 0)
            binding.rvFood.scrollToPosition(scrollPosition)
        }

        showReceyclerList()

        Glide.with(this)
            .load(R.drawable.icon_profile)
            .circleCrop()
            .into(binding.aboutPage)

        binding.aboutPage.setOnClickListener {
            binding.aboutPage.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {

                val moveToProfile = Intent(this@MainActivity, ActivityAccount::class.java)
                startActivity(moveToProfile)

                binding.aboutPage.scaleX = 1.0f
                binding.aboutPage.scaleY = 1.0f
            }.start()
        }

        binding.imgMenuOption.setOnClickListener {
            binding.imgMenuOption.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {

                val popUp = Dialog(this)
                popUp.setContentView(R.layout.menu_add_location)

                //mengatur posisi
                popUp.window?.setGravity(Gravity.TOP or Gravity.END)

                //margin
                val margin = popUp.window?.attributes
                margin?.y = 70
                margin?.x = 50
                popUp.window?.attributes = margin

                //menampilakn dialog
                popUp.show()

                binding.imgMenuOption.scaleX = 1.0f
                binding.imgMenuOption.scaleY = 1.0f

                val jakarta = popUp.findViewById<TextView>(R.id.tv_jakarta)
                val bandung = popUp.findViewById<TextView>(R.id.tv_bandung)
                val magelang = popUp.findViewById<TextView>(R.id.tv_magelang)
                val lamongan = popUp.findViewById<TextView>(R.id.tv_lamongan)
                val semarang = popUp.findViewById<TextView>(R.id.tv_semarang)
                val tegal = popUp.findViewById<TextView>(R.id.tv_tegal)
                val pekalongan = popUp.findViewById<TextView>(R.id.tv_pekalongan)
                val wonosobo = popUp.findViewById<TextView>(R.id.tv_wonosobo)
                val rembang = popUp.findViewById<TextView>(R.id.tv_rembang)
                val yogyakarta = popUp.findViewById<TextView>(R.id.tv_yogyakarta)
                val kudus = popUp.findViewById<TextView>(R.id.tv_kudus)
                val malang = popUp.findViewById<TextView>(R.id.tv_malang)
                val madiun = popUp.findViewById<TextView>(R.id.tv_madiun)
                val tangerang = popUp.findViewById<TextView>(R.id.tv_tangerang)
                val ngajuk = popUp.findViewById<TextView>(R.id.tv_nganjuk)
                val tasikmalaya = popUp.findViewById<TextView>(R.id.tv_tasimalaya)
                val probolinggo = popUp.findViewById<TextView>(R.id.tv_probolinggo)

                jakarta.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvJakarta.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                bandung.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvBandung.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                magelang.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvMagelang.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                lamongan.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvLamongan.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                semarang.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvSemarang.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                tegal.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvTegal.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                pekalongan.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvPekalongan.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                wonosobo.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvWonosobo.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                rembang.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvRembang.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                yogyakarta.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvYogyakarta.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                kudus.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvKudus.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                malang.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvMalang.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                probolinggo.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvProbolinggo.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                madiun.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvMadiun.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                tangerang.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvTangerang.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                ngajuk.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvNganjuk.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
                tasikmalaya.setOnClickListener {
                    binding.tvLocKota.text = bindingMenu.tvTasimalaya.text
                    binding.tvLocKota.setTypeface(null, Typeface.BOLD)
                    popUp.dismiss()//menutup popup
                    Toast.makeText(this,"Add kota ${binding.tvLocKota.text}", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }

        binding.btnList.setOnClickListener {
            //animasi
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                binding.btnList,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f)
            )
            scaleDown.duration = 100
            scaleDown.start()

            //aksi
            isGrid = false
            setLayoutManager(isGrid)
            binding.btnList.setBackgroundColor(white)
            binding.btnGrid.setBackgroundColor(orangeDark)
            binding.btnList.setTextColor(resources.getColor(R.color.orange_light))
            binding.btnGrid.setTextColor(resources.getColor(R.color.white))

            //tetapkan
            buttonBgColorList = white
            buttonBgColorGrid = orangeDark

            //mengembalikan keukuran semula
            scaleDown.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.btnList.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                }
            })
        }
        binding.btnGrid.setOnClickListener{

            //animasi
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                binding.btnGrid,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f)
            )
            scaleDown.duration = 100
            scaleDown.start()

            //aksi
            isGrid = true
            setLayoutManager(isGrid)
            binding.btnGrid.setBackgroundColor(white)
            binding.btnList.setBackgroundColor(orangeDark)
            binding.btnGrid.setTextColor(resources.getColor(R.color.orange_light))
            binding.btnList.setTextColor(resources.getColor(R.color.white))

            //tetapkan
            buttonBgColorList = orangeDark
            buttonBgColorGrid = white

            //mengembalikan keukuran semula
            scaleDown.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.btnGrid.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                }
            })
        }
    }

    @SuppressLint("Recycle")
    private fun getListFood(): ArrayList<Food>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataOverview = resources.getStringArray(R.array.data_overview)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataBahan = resources.getStringArray(R.array.data_bahan)
        val dataIntruksi = resources.getStringArray(R.array.data_intruksi)
        val dataPhotoMemasak = resources.obtainTypedArray(R.array.photo_memasak)


        Log.i("lenght", "panjang: databahan ${dataBahan.size}, data intruksi ${dataIntruksi.size}")

        val listFood = ArrayList<Food>()
        for (i in dataName.indices){
            val food = Food(dataName[i], dataOverview[i], dataPhoto.getResourceId(i, -1), dataBahan[i], dataIntruksi[i], dataPhotoMemasak.getResourceId(i, -1))
            listFood.add(food)
        }
            Log.i("data food", "${listFood.size}")
        return listFood
    }




    private fun showReceyclerList(){
//        binding.rvFood.layoutManager = LinearLayoutManager(this)
        val listFoodAdapter = ListFoodAdapter(list)
        binding.rvFood.adapter = listFoodAdapter

        listFoodAdapter.setOnItemClickListener(object : ListFoodAdapter.OnItemClickListener{ //objekct itu buat anonymous objek, untuk membuat objek tanpa nama
            override fun onItemClicked(data: Food) {

                val intentToDetails = Intent(this@MainActivity, ActivityDetails::class.java)
                intentToDetails.putExtra("EXTRA_DATA_PHOTO", data)
                startActivity(intentToDetails)

//                showSelectedFood(data)
            }
        })
    }
}