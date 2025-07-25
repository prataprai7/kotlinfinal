package com.example.projectk.domain.useCases

import com.example.projectk.common.ResultState
import com.example.projectk.data.models.UserData
import com.example.projectk.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(val repo: Repo){

    fun loginUser(userData: UserData): Flow<ResultState<String>>{
        return repo.loginWithEmailAndPassword(userData)
    }
}