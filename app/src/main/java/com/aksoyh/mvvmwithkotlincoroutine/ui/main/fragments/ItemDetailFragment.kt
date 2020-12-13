package com.aksoyh.mvvmwithkotlincoroutine.ui.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.aksoyh.mvvmwithkotlincoroutine.R
import com.aksoyh.mvvmwithkotlincoroutine.data.UserDetail
import com.aksoyh.mvvmwithkotlincoroutine.ui.main.*
import com.aksoyh.mvvmwithkotlincoroutine.util.*
import com.bumptech.glide.Glide

class ItemDetailFragment : Fragment(R.layout.fragment_item_detail) {

    private lateinit var userPhoto: ImageView
    private lateinit var userGender: TextView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userAddress: TextView

    private val args: ItemDetailFragmentArgs by navArgs()

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        userPhoto = view.findViewById(R.id.detail_user_photo)
        userGender = view.findViewById(R.id.detail_user_gender)
        userName = view.findViewById(R.id.detail_user_name)
        userEmail = view.findViewById(R.id.detail_user_email)
        userAddress = view.findViewById(R.id.detail_user_address)

        val userId: String = args.userId
        viewModel.userDetailFun(userId)

        viewModel.userDetail.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.data?.let { userDetailResponse ->
                        if (userDetailResponse.id.isNotEmpty()) {
                            setData(userDetailResponse)
                        } else
                            MainHelper.showAlertDialog(requireContext(), "Kullanıcı detayı bulunamamıştır.")
                    }
                }
                is Resource.Error -> {
                    (requireActivity() as MainActivity).hideLoading()
                    response.message?.let { message ->
                        MainHelper.showAlertDialog(requireContext(), message)
                    }
                }
                is Resource.Loading -> {
                    (requireActivity() as MainActivity).showLoading()
                }
            }
            response.data = null
            response.message = null
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(userDetail: UserDetail) {
        val imageUrl = userDetail.picture
        Glide.with(requireContext()).load(imageUrl).into(userPhoto)

        userGender.text = userDetail.gender
        userName.text = "${userDetail.title} ${userDetail.firstName} ${userDetail.lastName}"
        userEmail.text = userDetail.email
        userAddress.text = "${userDetail.location.street}, ${userDetail.location.state}, ${userDetail.location.city}, ${userDetail.location.country}"
    }
}