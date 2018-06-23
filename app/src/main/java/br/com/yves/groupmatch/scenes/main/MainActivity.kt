package br.com.yves.groupmatch.scenes.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.yves.groupmatch.R
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.navHost)

        bottomNavigationView.setupWithNavController(navController)
        //setupActionBarWithNavController(navController)

        //FIXME: WHAT IS THIS LINE DOING?
//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            onNavDestinationSelected(it, navController)
//        }
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.navHost).navigateUp()
}
