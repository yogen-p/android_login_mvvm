package com.yogenp.loginmvvm.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.yogenp.loginmvvm.R
import com.yogenp.loginmvvm.data.network.Resource
import com.yogenp.loginmvvm.data.network.UserApi
import com.yogenp.loginmvvm.data.repository.UserRepository
import com.yogenp.loginmvvm.data.responses.User
import com.yogenp.loginmvvm.databinding.FragmentHomeBinding
import com.yogenp.loginmvvm.ui.base.BaseFragment
import com.yogenp.loginmvvm.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, UserRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visible(false)

        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visible(false)
                    updateUI(it.value.user)
                }
                is Resource.Loading -> {
                    binding.progressBar.visible(true)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateUI(user: User){
        with(binding) {
            txtId.text = user.id.toString()
            txtName.text = user.name
            txtEmail.text = user.email
        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(UserApi::class.java, token)
        return UserRepository(api)
    }


}