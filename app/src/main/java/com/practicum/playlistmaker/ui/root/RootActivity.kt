package com.practicum.playlistmaker.ui.root

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.
        findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addPlayListFragment, R.id.playListFragment -> {
                    if (binding.bottomNavigationView.visibility == View.VISIBLE) {
                        binding.toolbar.visibility = View.GONE
                        binding.bottomNavigationView.visibility = View.GONE
                        binding.bottomNavigationView.animation =
                            AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
                        binding.bottomNavigationView.animate()
                    }

                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                }
                R.id.playerFragment -> {
                    if (binding.bottomNavigationView.visibility == View.VISIBLE) {
                        binding.bottomNavigationView.visibility = View.GONE
                        binding.bottomNavigationView.animation =
                            AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
                        binding.bottomNavigationView.animate()
                    }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                }
                else -> {
                    if (binding.bottomNavigationView.visibility == View.GONE) {
                        binding.bottomNavigationView.visibility = View.VISIBLE
                        binding.toolbar.navigationIcon = null
                        binding.bottomNavigationView.animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up)
                        binding.bottomNavigationView.animate()
                    }
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                }
            }
        }

       binding.bottomNavigationView.setupWithNavController(navController)

    }
}