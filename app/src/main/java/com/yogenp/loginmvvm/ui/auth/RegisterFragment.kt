package com.yogenp.loginmvvm.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yogenp.loginmvvm.R
import com.yogenp.loginmvvm.data.network.AuthApi
import com.yogenp.loginmvvm.data.network.Resource
import com.yogenp.loginmvvm.data.repository.AuthRepository
import com.yogenp.loginmvvm.databinding.FragmentRegisterBinding
import com.yogenp.loginmvvm.ui.*
import com.yogenp.loginmvvm.ui.base.BaseFragment
import com.yogenp.loginmvvm.ui.home.HomeActivity
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>() {

    var name = ""
    var email = ""
    var password = ""

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
                else -> Log.e("Register User", it.toString())
            }
        })

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.edtRegPasswordConfirmation.addTextChangedListener {
            name = binding.edtRegName.text.toString().trim()
            email = binding.edtRegEmail.text.toString().trim()
            password = binding.edtRegPassword.text.toString().trim()
            binding.btnRegister.enable(
                name.isNotEmpty() &&
                        email.isNotEmpty() &&
                        password.isNotEmpty() &&
                        it.toString().isNotEmpty()
            )
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val passwordConfirmation = binding.edtRegPasswordConfirmation.text.toString().trim()
        if (password == passwordConfirmation){
            viewModel.registerUser(name, email, password, passwordConfirmation)
        } else {
            requireView().snackBar("Passwords don't match!!!")
        }
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(AuthApi::class.java), userPreferences)

}