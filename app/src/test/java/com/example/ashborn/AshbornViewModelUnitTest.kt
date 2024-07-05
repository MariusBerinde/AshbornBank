package com.example.ashborn

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class AshbornViewModelUnitTest {
    private  var validatore: Validatore= Validatore()

    @Test
    fun formatoDataNascitaValidaTest() {
        val formato1 = "01-01-2000"
        val formato2 = "01/01/2000"
        val formato1Sbagliato = "01_01_2000"
        val formato2Sbagliato = "01 33 2000"
        val formato3Sbagliato = "45 01 2000"
        val formato4Sbagliato = "04/21/2000"
        val formato5Sbagliato = "45/01/2000"
        val formato6Sbagliato = "2000/01/2000"
        val formato7Sbagliato = "venerdi 25 aprile 2000"
        val formato8Sbagliato = "DROP * FROM * ;"
        val formato9Sbagliato = "01-01-2020"
        val formato10Sbagliato = "01/01/2020"
        val formato11Sbagliato = ""
        assertTrue("formato data di nascita valido", validatore.formatoDataNascitaValida(formato1))
        assertTrue("formato data di nascita valido", validatore.formatoDataNascitaValida(formato2))
        assertFalse("formato1Sbagliato ", validatore.formatoDataNascitaValida(formato1Sbagliato))
        assertFalse("formato2Sbagliato ", validatore.formatoDataNascitaValida(formato2Sbagliato))
        assertFalse("formato3Sbagliato ", validatore.formatoDataNascitaValida(formato3Sbagliato))
        assertFalse("formato4Sbagliato ", validatore.formatoDataNascitaValida(formato4Sbagliato))
        assertFalse("formato5Sbagliato ", validatore.formatoDataNascitaValida(formato5Sbagliato))
        assertFalse("formato6Sbagliato ", validatore.formatoDataNascitaValida(formato6Sbagliato))
        assertFalse("formato7Sbagliato ", validatore.formatoDataNascitaValida(formato7Sbagliato))
        assertFalse("formato8Sbagliato ", validatore.formatoDataNascitaValida(formato8Sbagliato))
        assertFalse("formato9Sbagliato ", validatore.formatoDataNascitaValida(formato9Sbagliato))
        assertFalse("formato10Sbagliato ", validatore.formatoDataNascitaValida(formato10Sbagliato))
        assertFalse("formato11Sbagliato ", validatore.formatoDataNascitaValida(formato11Sbagliato))
    }

    @Test
    fun formatoNomeValidoTest(){
        val formato1="Marius "
        val formato2=" Giorgio"
        val formato3="Giorgio  Magri"
        val formato4="Giorgio Magri Din "
        val formato5="Rollo"
        val formato1Sbagliato="__Giorgio__"
        val formato2Sbagliato="!Giorgio"
        val formato3Sbagliato="#Giorgio"
        val formato4Sbagliato="?Giorgio"
        val formato5Sbagliato=""
        val formato6Sbagliato=" "
        val lenMinWrong = "A"
        val lenMaxWrong = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        assertTrue("formato1Nome ", validatore.formatoNomeValido(formato1))
        assertTrue("formato2Nome ", validatore.formatoNomeValido(formato2))
        assertTrue("formato3Nome ", validatore.formatoNomeValido(formato3))
        assertTrue("formato4Nome ", validatore.formatoNomeValido(formato4))
        assertTrue("formato5Nome ", validatore.formatoNomeValido(formato5))
        assertFalse("formato1Sbabliato ", validatore.formatoNomeValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", validatore.formatoNomeValido(formato2Sbagliato))
        assertFalse("formato3Sbabliato ", validatore.formatoNomeValido(formato3Sbagliato))
        assertFalse("formato4Sbabliato ", validatore.formatoNomeValido(formato4Sbagliato))
        assertFalse("formato5Sbabliato ", validatore.formatoNomeValido(formato5Sbagliato))
        assertFalse("formato6Sbabliato ", validatore.formatoNomeValido(formato6Sbagliato))
        assertFalse("lunghezza minima ", validatore.formatoNomeValido(lenMinWrong))
        assertFalse("lunghezza massima", validatore.formatoNomeValido(lenMaxWrong))
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

        assertTrue("formato1 ", validatore.formatoCognomeValido(formato1))
        assertTrue("formato2 ", validatore.formatoCognomeValido(formato2))
        assertFalse("formato1Sbabliato ", validatore.formatoCognomeValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", validatore.formatoCognomeValido(formato2Sbagliato))
        assertFalse("formato3Sbabliato ", validatore.formatoCognomeValido(formato3Sbagliato))
        assertFalse("formato4Sbabliato ", validatore.formatoCognomeValido(formato4Sbagliato))
        assertFalse("formato5Sbabliato ", validatore.formatoCognomeValido(formato5Sbagliato))
        assertFalse("formato6Sbabliato ", validatore.formatoCognomeValido(formato6Sbagliato))
        assertFalse("lunghezza minima", validatore.formatoCognomeValido(lenMinWrong))
        assertFalse("lunghezza massimo", validatore.formatoCognomeValido(lenMaxWrong))
    }


    @Test
    fun  formatoCodiceClienteTest(){
        val formato1="123467890"
        val formato2="ab123cd00"
        val formato3 = "12 33 44 55 9"
        val formato1Sbagliato="Ab123CD00"
        val formato2Sbagliato="AA123CD00"
        val formato3Sbagliato="#AA123CD00à"
        val formato4Sbagliato="a"
        val formato5Sbagliato="aaaaaaaaaaaaa"
        assertTrue("formato1 codice cliente ", validatore.formatoCodiceCliente(formato1))
        assertTrue("formato2 codice cliente ", validatore.formatoCodiceCliente(formato2))
        assertTrue("formato3 codice cliente ", validatore.formatoCodiceCliente(formato3))
        assertFalse("formato1Sbagliato codice cliente ", validatore.formatoCodiceCliente(formato1Sbagliato))
        assertFalse("formato2Sbagliato codice cliente ", validatore.formatoCodiceCliente(formato2Sbagliato))
        assertFalse("formato3Sbagliato codice cliente ", validatore.formatoCodiceCliente(formato3Sbagliato))
        assertFalse("formato4Sbagliato codice cliente ", validatore.formatoCodiceCliente(formato4Sbagliato))
        assertFalse("formato5Sbagliato codice cliente ", validatore.formatoCodiceCliente(formato5Sbagliato))
    }

/*
    @Test
    fun checkPinTest() {
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
        viewModel.setPinX(formato1)
        assertTrue("formato1 ", viewModel.checkPin())
        viewModel.setPinX(formato2)
        assertTrue("formato2 ", viewModel.checkPin())
        viewModel.setPinX(formato1Sbagliato)
        assertFalse("formato1Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(formato2Sbagliato)
        assertFalse("formato2Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(formato3Sbagliato)
        assertFalse("formato3Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(formato4Sbagliato)
        assertFalse("formato4Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(formato5Sbagliato)
        assertFalse("formato5Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(formato6Sbagliato)
        assertFalse("formato6Sbabliato ", viewModel.checkPin())
        viewModel.setPinX(lenMinWrong)
        assertFalse("lunghezza minima", viewModel.checkPin())
        viewModel.setPinX(lenMaxWrong)
        assertFalse("lunghezza massimo", viewModel.checkPin())

    }
*/



}