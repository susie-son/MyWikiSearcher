package com.example.mywikisearcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mywikisearcher.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = ArticleListAdapter()
    private val searchResultList = mutableListOf<QueryResponse.Query.Page>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.searchTextView.requestFocus()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                adapter.articleList.clear()
                if (tab != null && tab.position == 0) {
                    binding.searchTextView.visibility = View.VISIBLE
                    adapter.articleList.addAll(searchResultList)
                } else {
                    binding.searchTextView.visibility = View.GONE
                    adapter.articleList.addAll(BookmarkHelper.bookmarks)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })

        binding.searchTextView.doOnTextChanged { text, start, before, count ->
            if (text!!.length > 0) {
                searchWiki(text.toString())
            }
        }
    }

    private fun searchWiki(query: String, startFromIndex: Int = 0) {
        CoroutineScope(Dispatchers.Main).launch {
            val client = OkHttpClient()
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
            val service = Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/")
                .client(client)
                .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
                .build()
                .create(Service::class.java)

            // Fetch a list of articles from Wikipedia!
            val response = service.prefixSearch(query, 20, startFromIndex)

            // Filter out articles that don't have a thumbnail!
            val finalList = mutableListOf<QueryResponse.Query.Page>()
            response.query?.pages!!.forEach {
                if (it.thumbnail != null) {
                    finalList.add(it)
                }
            }

            if (startFromIndex == 0)
                searchResultList.clear()

            searchResultList.addAll(finalList)
            adapter.articleList.clear()
            adapter.articleList.addAll(searchResultList)
            adapter.notifyDataSetChanged()
        }
    }

    private inner class ArticleListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {
        val articleList = mutableListOf<QueryResponse.Query.Page>()

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
                searchWiki(binding.searchTextView.text.toString(), articleList.size)
            }
        }

        override fun onClick(v: View) {
            val item = articleList[v.tag as Int]
            if (v.id == R.id.bookmark_image_view) {
                if (BookmarkHelper.bookmarks.contains(item)) {
                    BookmarkHelper.removeBookmark(item)
                    Toast.makeText(this@MainActivity, R.string.bookmark_removed, Toast.LENGTH_SHORT).show()
                    articleList.remove(item)
                } else {
                    BookmarkHelper.addBookmark(item)
                    Toast.makeText(this@MainActivity, R.string.bookmark_added, Toast.LENGTH_SHORT).show()
                }
                notifyDataSetChanged()
                return
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/${item.title}")))
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
}
