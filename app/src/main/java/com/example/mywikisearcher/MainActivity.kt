package com.example.mywikisearcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mywikisearcher.databinding.ActivityMainBinding
import com.example.mywikisearcher.model.QueryResponse
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ArticleListAdapter()
    private val tabs: Array<Tab> = Tab.entries.toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.searchTextView.requestFocus()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val selectedTab = tabs.getOrNull(tab.position) ?: return
                viewModel.selectTab(selectedTab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        binding.searchTextView.doOnTextChanged { text, start, before, count ->
            viewModel.searchWiki(text)
        }

        lifecycleScope.launch {
            viewModel.selectedTab.collect {
                binding.searchTextView.visibility = when (it) {
                    Tab.Search -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.list.collectLatest {
                adapter.updateList(it)
            }
        }
    }

    private inner class ArticleListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
        private val articleList = mutableListOf<QueryResponse.Query.Page>()

        override fun getItemCount(): Int {
            return articleList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ListItemHolder(inflater.inflate(R.layout.list_item, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
            (holder as ListItemHolder).bindItem(articleList[pos])
            holder.itemView.setOnClickListener(this)
            holder.itemView.findViewById<View>(R.id.bookmark_image_view).tag = pos
            holder.itemView.findViewById<View>(R.id.bookmark_image_view).setOnClickListener(this)

            if (pos == articleList.size - 1 && binding.tabLayout.selectedTabPosition == 0) {
                // Since we've reached the bottom of the list, fetch the next batch of results!
                viewModel.searchWiki(binding.searchTextView.text, articleList.size)
            }
        }

        override fun onClick(v: View) {
            val item = articleList[v.tag as Int]
            if (v.id == R.id.bookmark_image_view) {
                viewModel.handleBookmark(item)
                return
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/${item.title}")))
        }

        fun updateList(newArticleList: List<QueryResponse.Query.Page>) {
            articleList.clear()
            articleList.addAll(newArticleList)
            notifyDataSetChanged()
        }
    }

    private inner class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(listItem: QueryResponse.Query.Page) {
            itemView.findViewById<TextView>(R.id.title_text_view).text = listItem.title
            itemView.findViewById<TextView>(R.id.description_text_view).text = listItem.description
            Glide.with(this@MainActivity)
                .load(listItem.thumbnail?.source)
                .into(itemView.findViewById(R.id.image_view))

            // TODO: Display the latitude/longitude of the article, if available. This can probably
            // be done by adding `prop=coordinates` to the API query.

            itemView.tag = adapterPosition
        }
    }

    enum class Tab {
        Search,
        Bookmarks
    }
}
