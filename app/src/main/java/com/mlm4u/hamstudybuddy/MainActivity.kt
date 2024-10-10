package com.mlm4u.hamstudybuddy

import android.graphics.Color
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mlm4u.hamstudybuddy.R.id.fragmentContainerView
import com.mlm4u.hamstudybuddy.databinding.ActivityMainBinding

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