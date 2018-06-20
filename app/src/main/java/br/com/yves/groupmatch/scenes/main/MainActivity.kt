package br.com.yves.groupmatch.scenes.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import br.com.yves.groupmatch.R
import br.com.yves.groupmatch.scenes.calendar.CalendarFragment
import br.com.yves.groupmatch.scenes.connectionRole.ConnectionRoleFragment
import br.com.yves.groupmatch.scenes.settings.SettingsFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MainView, BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            displayCalendarScene()
        }

        this.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }


    /*
        MainView Implementation
     */

    override fun displayCalendarScene() = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, CalendarFragment()).commitNow()

    override fun displayConnectionRoleScene() = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, ConnectionRoleFragment()).commitNow()

    override fun displaySettingsScene() = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, SettingsFragment()).commitNow()

    /*
       BottomNavigationView.OnNavigationItemSelectedListener Implementation
    */

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.calendarTabIcon -> this.displayCalendarScene()
            R.id.searchTabIcon -> this.displayConnectionRoleScene()
            R.id.settingsTabIcon -> this.displaySettingsScene()
        }
        return true
    }
}
