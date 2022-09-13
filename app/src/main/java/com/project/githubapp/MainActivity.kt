package com.project.githubapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubapp.adapter.ListUserAdapter
import com.project.githubapp.databinding.ActivityMainBinding
import com.project.githubapp.model.User
import com.project.githubapp.pref.SettingPreferences
import com.project.githubapp.viewmodel.MainViewModel
import com.project.githubapp.viewmodel.PreferencesViewModel
import com.project.githubapp.viewmodel.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel : MainViewModel


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
               Intent(this@MainActivity, DetailActivity::class.java).also {
                   it.putExtra(DetailActivity.EXTRA_USER, data.login)
                   it.putExtra(DetailActivity.EXTRA_ID, data.id)
                   it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar_url)
                   startActivity(it)
               }
            }

        })
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        binding.apply {
            rvReview.layoutManager = LinearLayoutManager(this@MainActivity)
            rvReview.setHasFixedSize(true)
            rvReview.adapter = adapter
            btnSearch.setOnClickListener {
                searchUser()
            }
            
            searchPage.setOnKeyListener { _, i, keyEvent ->
                if(keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }

                return@setOnKeyListener false
            }

            viewModel.getSearch().observe(this@MainActivity) {
                if (it != null) {
                    adapter.setList(it)
                    showLoading(false)
                }
            }
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    private fun searchUser() {
        binding.apply {
            val query = searchPage.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.searchUser(query)
        }
    }


    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}