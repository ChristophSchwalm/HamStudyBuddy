package com.mlm4u.hamstudybuddy

import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.mlm4u.hamstudybuddy.R.id.fragmentContainerView
import com.mlm4u.hamstudybuddy.databinding.ActivityMainBinding
import com.mlm4u.hamstudybuddy.utils.AdStart.AdStart

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        handleOnBackPressed()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        //Setzt den NavGraphen und die BotomView
        val navHost =
            supportFragmentManager.findFragmentById(fragmentContainerView) as NavHostFragment
        val navController: NavController = navHost.navController
        vb.bottomNavigationView.setupWithNavController(navController)
        vb.bottomNavigationView.itemIconTintList = null
        vb.bottomNavigationView.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun handleOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                vb.fragmentContainerView.findNavController().navigateUp()
            }
        }
        onBackPressedDispatcher.addCallback(callback)

    }

}

class AppStartAdApp : Application() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()
        try {
            MobileAds.initialize(this) {}
            AdStart(this)
        } catch (e:Exception){
            e.printStackTrace()
        }
    }
}