package com.project.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.project.githubapp.adapter.SectionsPagerAdapter
import com.project.githubapp.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(
            this
        ).get(DetailUserViewModel::class.java)

        val bundle = Bundle()
        val username = intent.getStringExtra(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        bundle.putString(EXTRA_USER, username)
        if (username != null) {
            mainViewModel.setUserDetail(username)
        }
        mainViewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvUsername.text = it.login
                    tvFullname.text = it.name
                    tvCompany.text = it.company
                    tvLocation.text = it.location
                    tvRepo.text = it.repos_url
                    Glide.with(this@DetailActivity)
                        .load(it.avatar_url)
                        .apply(RequestOptions().override(220, 230))
                        .into(circleImageView)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = mainViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFav.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFav.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener {
            _isChecked = ! _isChecked
            if (_isChecked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        mainViewModel.addFavorite(username,id,avatarUrl)
                    }
                }

            } else {
                mainViewModel.removeFavorite(id)
            }
            binding.toggleFav.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()
    }

}