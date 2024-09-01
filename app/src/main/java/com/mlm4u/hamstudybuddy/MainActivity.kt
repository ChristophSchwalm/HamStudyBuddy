package com.mlm4u.hamstudybuddy

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.mlm4u.hamstudybuddy.data.database.Questions
import com.mlm4u.hamstudybuddy.data.model.Root
import com.mlm4u.hamstudybuddy.data.remote.QuestionApi
import com.mlm4u.hamstudybuddy.data.viewModel.SharedViewModel
import com.mlm4u.hamstudybuddy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        vb.bottomNavigationView.itemIconTintList = null

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        vb.bottomNavigationView.setupWithNavController(navController)
        vb.bottomNavigationView.setBackgroundColor(Color.TRANSPARENT)


    }

}