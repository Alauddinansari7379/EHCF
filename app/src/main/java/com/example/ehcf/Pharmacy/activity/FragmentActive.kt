package com.example.ehcf.Pharmacy.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehcf.Diagnostic.adapter.AdapterOrderListTest
import com.example.ehcf.Helper.AppProgressBar
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Pharmacy.adapter.AdapterOrderList
import com.example.ehcf.Pharmacy.model.ModelOrderList
import com.example.ehcf.R
import com.example.ehcf.databinding.FragmentActiveBinding
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentActive : Fragment() {
    private lateinit var binding: FragmentActiveBinding
    lateinit var sessionManager: SessionManager
    var countN4 = 0
    var countN = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_active, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentActiveBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        if (BrowseMedicine.slug == "test") {
            apiCallOrderTestList()
        } else {
            apiCallOrderList()

        }
    }

    private fun apiCallOrderList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.orderList(
            sessionManager.id.toString(), BrowseMedicine.slug
        )
            .enqueue(object :
                Callback<ModelOrderList> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelOrderList>,
                    response: Response<ModelOrderList>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            myToast(requireActivity(), "No Product Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter =
                                    AdapterOrderList(requireContext(), response.body()!!.result)
                                AppProgressBar.hideLoaderDialog()

                            }


                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelOrderList>, t: Throwable) {
                    countN++
                    if (countN <= 3) {
                        apiCallOrderList()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

    private fun apiCallOrderTestList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.orderListTest(
            sessionManager.id.toString(), BrowseMedicine.slug
        )
            .enqueue(object :
                Callback<ModelOrderList> {
                @SuppressLint("LogNotTimber", "SetTextI18n")
                override fun onResponse(
                    call: Call<ModelOrderList>,
                    response: Response<ModelOrderList>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 404) {
                            myToast(requireActivity(), "Something went wrong")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            myToast(requireActivity(), "No Product Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.recyclerView.apply {
                                adapter =
                                    AdapterOrderListTest(requireContext(), response.body()!!.result)
                                AppProgressBar.hideLoaderDialog()

                            }


                        }
                    } catch (e: Exception) {
                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelOrderList>, t: Throwable) {
                    countN4++
                    if (countN4 <= 3) {
                        apiCallOrderTestList()
                    } else {
                        myToast(requireActivity(), t.message.toString())
                        AppProgressBar.hideLoaderDialog()

                    }
                }

            })

    }

}