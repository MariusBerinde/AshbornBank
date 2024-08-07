package com.example.ashborn.view.login

import com.example.ashborn.viewModel.WelcomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WelcomeViewModelTest {
    @Test
    fun testGetUsername(){

        val  mockViewModel = mockk<WelcomeViewModel>()
        coEvery { mockViewModel.getUsername() } returns   "giggino"



        assertEquals("giggino", mockViewModel.getUsername())

    }
}