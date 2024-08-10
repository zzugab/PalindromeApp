package com.sugab.palindromeapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sugab.palindromeapp.R
import com.sugab.palindromeapp.adapter.UserAdapter
import com.sugab.palindromeapp.model.user.UserResponse
import com.sugab.palindromeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var userAdapter: UserAdapter
    private var currentPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        userAdapter = UserAdapter { user ->
            val intent = Intent()
            intent.putExtra("SELECTED_USER", "${user.firstName} ${user.lastName}")
            setResult(RESULT_OK, intent)
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        swipeRefreshLayout.setOnRefreshListener {
            loadUsers(page = 1, isRefreshing = true)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadUsers(page = currentPage + 1)
                }
            }
        })

        loadUsers(page = 1)
    }

    private fun loadUsers(page: Int, isRefreshing: Boolean = false) {
        isLoading = true
        ApiClient.apiService.getUsers(page = page, perPage = 10).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.data ?: emptyList()
                    if (isRefreshing) {
                        userAdapter.setUsers(users)
                        swipeRefreshLayout.isRefreshing = false
                    } else {
                        userAdapter.addUsers(users)
                    }
                    currentPage = page
                }
                isLoading = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                isLoading = false
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }
}
