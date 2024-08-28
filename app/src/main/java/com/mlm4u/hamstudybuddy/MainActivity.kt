package com.mlm4u.hamstudybuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.mlm4u.hamstudybuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        vb.bottomNavigationView.setupWithNavController(navController)

        vb.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bootcampFragment -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.bootcampFragment, true)
                        .build()
                    navController.navigate(R.id.bootcampFragment, null, navOptions)
                    true
                }

                else -> {
                    navController.navigate(item.itemId)
                true
                }
            }
        }
    }


}