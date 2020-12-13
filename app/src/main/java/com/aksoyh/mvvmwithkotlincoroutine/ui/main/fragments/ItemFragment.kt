package com.aksoyh.mvvmwithkotlincoroutine.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aksoyh.mvvmwithkotlincoroutine.R
import com.aksoyh.mvvmwithkotlincoroutine.ui.main.MainActivity
import com.aksoyh.mvvmwithkotlincoroutine.ui.main.MainViewModel
import com.aksoyh.mvvmwithkotlincoroutine.util.MainHelper
import com.aksoyh.mvvmwithkotlincoroutine.util.Resource

class ItemFragment : Fragment(R.layout.fragment_item_list) {

    private lateinit var noDataLayout: LinearLayout
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    private lateinit var viewModel: MainViewModel
    private lateinit var usersAdapter: MyItemRecyclerViewAdapter

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        noDataLayout = view.findViewById(R.id.fr_item_no_data_layout)
        userRecyclerView = view.findViewById(R.id.fr_item_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.fr_item_swipe_refresh)

        setupSwipeRefresh()
        setupRecyclerView()

        navController = Navigation.findNavController(requireActivity(), R.id.ac_ma_nav_host_fragment)

        usersAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("userId", it)
            }

            navController.navigate(
                R.id.action_usersFragment_to_userDetailFragment,
                bundle
            )
        }

        viewModel.allUsers.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.data?.let { usersResponse ->
                        if (usersResponse.data.isNullOrEmpty()) {
                            showNoDataLayout()
                        } else {
                            hideNoDataLayout()
                            usersAdapter.differ.submitList(usersResponse.data.toList())
                        }
                    }
                }
                is Resource.Error -> {
                    swipeRefreshLayout.isRefreshing = false
                    (requireActivity() as MainActivity).hideLoading()
                    showNoDataLayout()
                    response.message?.let { message ->
                        MainHelper.showAlertDialog(requireContext(), message)
                    }
                }
                is Resource.Loading -> {
                    if (!swipeRefreshLayout.isRefreshing)
                        (requireActivity() as MainActivity).showLoading()
                }
            }
            response.data = null
            response.message = null
        })

    }

    private fun showNoDataLayout() {
        noDataLayout.visibility = View.VISIBLE
    }

    private fun hideNoDataLayout() {
        noDataLayout.visibility = View.GONE
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.allUsersFun()
        }
        swipeRefreshLayout.setColorSchemeColors(
            resources.getColor(android.R.color.holo_blue_bright),
            resources.getColor(android.R.color.holo_green_light),
            resources.getColor(android.R.color.holo_orange_light),
            resources.getColor(android.R.color.holo_red_light)
        )
    }

    private fun setupRecyclerView() {
        usersAdapter = MyItemRecyclerViewAdapter()
        userRecyclerView.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(activity)
            viewModel.allUsersFun()
        }
    }

}