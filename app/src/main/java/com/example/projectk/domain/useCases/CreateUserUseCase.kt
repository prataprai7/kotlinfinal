package com.example.projectk.domain.useCases

import com.example.projectk.common.ResultState
import com.example.projectk.data.models.UserData
import com.example.projectk.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(val repo: Repo){

    fun createUser(userData: UserData): Flow<ResultState<String>>{
        return repo.registerWithEmailAndPassword(userData)
    }
}