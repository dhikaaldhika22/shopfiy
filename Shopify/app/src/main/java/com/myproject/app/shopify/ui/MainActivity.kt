package com.myproject.app.shopify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.myproject.app.shopify.R
import com.myproject.app.shopify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setItemSelected(R.id.navigation_home, true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, HomeFragment())
            .commit()

        navigationBottom()
    }

    private fun navigationBottom() {
        binding.navView.setOnItemSelectedListener {
            var fragment: Fragment? = null
            fragment = when (it) {
                R.id.navigation_checkout -> CheckoutFragment()
                else -> HomeFragment()
            }
            if (fragment != null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
        }
    }

}