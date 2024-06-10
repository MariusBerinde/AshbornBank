package com.example.ashborn

import com.example.ashborn.viewModel.AshbornViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class AshbornViewModelUnitTest {
    private  var viewModel: AshbornViewModel = AshbornViewModel()

    @Test
    fun formatoDataNascitaValidaTest() {
        val formato1="01-01-2000"
        val formato2="01/01/2000"
        val formato1Sbagliato="01_01_2000"
        val formato2Sbagliato="01 33 2000"
        val formato3Sbagliato="45 01 2000"
        val formato4Sbagliato="04/21/2000"
        val formato5Sbagliato="45/01/2000"
        val formato6Sbagliato="2000/01/2000"
        val formato7Sbagliato="venerdi 25 aprile 2000"
        val formato8Sbagliato="DROP * FROM * ;"

        assertTrue("formato data di nascita valido", viewModel.formatoDataNascitaValida(formato1))
        assertTrue("formato data di nascita valido", viewModel.formatoDataNascitaValida(formato2))
        assertFalse("formato1Sbagliato ", viewModel.formatoDataNascitaValida(formato1Sbagliato))
  /*      assertFalse("formato2Sbagliato ", viewModel.formatoDataNascitaValida(formato2Sbagliato))
        assertFalse("formato3Sbagliato ", viewModel.formatoDataNascitaValida(formato3Sbagliato))
        assertFalse("formato4Sbagliato ", viewModel.formatoDataNascitaValida(formato4Sbagliato))
        assertFalse("formato5Sbagliato ", viewModel.formatoDataNascitaValida(formato5Sbagliato))
        assertFalse("formato6Sbagliato ", viewModel.formatoDataNascitaValida(formato6Sbagliato))
        assertFalse("formato7Sbagliato ", viewModel.formatoDataNascitaValida(formato7Sbagliato))
        assertFalse("formato8Sbagliato ", viewModel.formatoDataNascitaValida(formato8Sbagliato))*/
    }

    @Test
    fun formatoNomeValidoTest(){

        val formato1="Marius"
        val formato2="Giorgio"
        val formato1Sbagliato="__Giorgio__"
        val formato2Sbagliato="!Giorgio"
        val formato3Sbagliato="#Giorgio"
        val formato4Sbagliato="?Giorgio"
        val formato5Sbagliato=""
        val formato6Sbagliato=" "
        val lenMinWrong = "A"
        val lenMaxWrong = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        assertTrue("formato1Nome ", viewModel.formatoNomeValido(formato1))
        assertTrue("formato2Nome ", viewModel.formatoNomeValido(formato1))
      /*  assertFalse("formato1Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato3Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato4Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato5Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato6Sbabliato ", viewModel.formatoNomeValido(formato1Sbagliato)) */
    }

    @Test
    fun formatoCognomeValidoTest(){
        val formato1="Marius"
        val formato2="Giorgio"
        val formato1Sbagliato="__Giorgio__"
        val formato2Sbagliato="!Giorgio"
        val formato3Sbagliato="#Giorgio"
        val formato4Sbagliato="?Giorgio"
        val formato5Sbagliato=""
        val formato6Sbagliato=" "
        val lenMinWrong = "A"
        val lenMaxWrong = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        assertTrue("formato1 ", viewModel.formatoCognomeValido(formato1))
        assertTrue("formato2 ", viewModel.formatoCognomeValido(formato1))
       /* assertFalse("formato1Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato3Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato4Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato5Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato6Sbabliato ", viewModel.formatoCognomeValido(formato1Sbagliato))*/
    }


    @Test
    fun  formatoCodiceClienteTest(){

        val formato1="123467890"
        val formato2="ab123cd00"
        val formato1Sbagliato="Ab123CD00"
        val formato2Sbagliato="AA123CD00"
        val formato3Sbagliato="#AA123CD00à"
        val formato4Sbagliato="a"
        val formato5Sbagliato="aaaaaaaaaaaaa"
        assertTrue("formato1 codice cliente ", viewModel.formatoCodiceCliente(formato1))
        assertTrue("formato2 codice cliente ", viewModel.formatoCodiceCliente(formato1))
        /*assertTrue("formato1Sbagliato codice cliente ", viewModel.formatoCodiceCliente(formato1Sbagliato))
        assertTrue("formato2Sbagliato codice cliente ", viewModel.formatoCodiceCliente(formato2Sbagliato))
        assertTrue("formato3Sbagliato codice cliente ", viewModel.formatoCodiceCliente(formato3Sbagliato))
        assertTrue("formato4Sbagliato codice cliente ", viewModel.formatoCodiceCliente(formato4Sbagliato))
        assertTrue("formato5Sbagliato codice cliente ", viewModel.formatoCodiceCliente(formato5Sbagliato))*/

    }

}