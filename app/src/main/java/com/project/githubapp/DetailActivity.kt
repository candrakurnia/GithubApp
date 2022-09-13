package com.project.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.project.githubapp.adapter.SectionsPagerAdapter
import com.project.githubapp.databinding.ActivityDetailBinding
import com.project.githubapp.viewmodel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel


    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = intent.getStringExtra(EXTRA_USER)
        val aid = intent.getIntExtra(EXTRA_ID,0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        viewModel = ViewModelProvider(
            this
        ).get(DetailUserViewModel::class.java)

        viewModel.loading.observe(this) {
            showLoading(it)
        }
        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                showLoading(false)
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

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(aid)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFav.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleFav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener {
            isChecked = ! isChecked
            if (isChecked) {
                viewModel.addFavorite(username.toString(),aid, avatarUrl.toString())

            } else {
                viewModel.removeFavorite(aid)
            }
            binding.toggleFav.isChecked = isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()
    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.visibility = if (b) View.VISIBLE else View.GONE

    }

}