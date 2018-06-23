package br.com.yves.groupmatch.scenes.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.scenes.calendar.CalendarFragment
import br.com.yves.groupmatch.scenes.connectionRole.ConnectionRoleFragment
import br.com.yves.groupmatch.scenes.settings.SettingsFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = NavHostFragment.findNavController(my_nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            onNavDestinationSelected(it, navController)
        }
    }
}
