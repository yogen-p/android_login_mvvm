package com.yogenp.loginmvvm.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yogenp.loginmvvm.R
import com.yogenp.loginmvvm.data.network.AuthApi
import com.yogenp.loginmvvm.data.network.Resource
import com.yogenp.loginmvvm.data.repository.AuthRepository
import com.yogenp.loginmvvm.databinding.FragmentRegisterBinding
import com.yogenp.loginmvvm.ui.base.BaseFragment
import com.yogenp.loginmvvm.ui.enable
import com.yogenp.loginmvvm.ui.handleApiError
import com.yogenp.loginmvvm.ui.home.HomeActivity
import com.yogenp.loginmvvm.ui.startNewActivity
import com.yogenp.loginmvvm.ui.visible
import kotlinx.coroutines.launch


class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>()  {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressBar.visible(false)
        binding.btnRegister.enable(false)

        viewModel.registerResponse.observe(viewLifecycleOwner, {
            binding.progressBar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(it.value.user.access_token!!)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Failure -> handleApiError(it) { registerUser() }
                else -> Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnGoToLogin.setOnClickListener {
//            parentFragmentManager.beginTransaction().replace(R.id.fragment, LoginFragment())
            findNavController().navigate(R.id.loginFragment)
        }

        binding.edtRegPassword.addTextChangedListener {
            val name = binding.edtRegName.text.toString().trim()
            val email = binding.edtRegEmail.text.toString().trim()
            binding.btnRegister.enable(name.isNotEmpty() && email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val v = inflater.inflate(R.layout.fragment_register, container, false)
//
//        return v
//
//    }

    private fun registerUser(){
        val name = binding.edtRegName.text.toString().trim()
        val email = binding.edtRegEmail.text.toString().trim()
        val password = binding.edtRegPassword.text.toString().trim()
        viewModel.registerUser(name, email, password)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}