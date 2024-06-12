package com.example.ashborn


import com.example.ashborn.viewModel.AshbornViewModel
import com.example.ashborn.viewModel.OperationViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
class OperationViewModelTest {
    private  var viewModel: OperationViewModel = OperationViewModel()

    @Test
    fun formatoBeneficiarioValidoTest(){

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

        assertTrue("formato1Nome ", viewModel.formatoBeneficiarioValido(formato1))
        assertTrue("formato2Nome ", viewModel.formatoBeneficiarioValido(formato2))
        assertFalse("formato1Sbabliato ", viewModel.formatoBeneficiarioValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", viewModel.formatoBeneficiarioValido(formato2Sbagliato))
        assertFalse("formato3Sbabliato ", viewModel.formatoBeneficiarioValido(formato3Sbagliato))
        assertFalse("formato4Sbabliato ", viewModel.formatoBeneficiarioValido(formato4Sbagliato))
        assertFalse("formato5Sbabliato ", viewModel.formatoBeneficiarioValido(formato5Sbagliato))
        assertFalse("formato6Sbabliato ", viewModel.formatoBeneficiarioValido(formato6Sbagliato))
        assertFalse("lunghezza minima ", viewModel.formatoBeneficiarioValido(lenMinWrong))
        assertFalse("lunghezza massima", viewModel.formatoBeneficiarioValido(lenMaxWrong))
    }

    @Test
    fun formatoIbanValido(){
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

        assertTrue("formato1 ", viewModel.formatoIbanValido(formato1))
        assertTrue("formato2 ", viewModel.formatoIbanValido(formato1))
        assertFalse("formato1Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("formato2Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("formato3Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("formato4Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("formato5Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("formato6Sbabliato ", viewModel.formatoIbanValido(formato1Sbagliato))
        assertFalse("lunghezza minima", viewModel.formatoIbanValido(lenMinWrong))
        assertFalse("lunghezza massimo", viewModel.formatoIbanValido(lenMaxWrong))
    }


    @Test
    fun  formatoCausaleValidaTest(){

        val formato1="123467890"
        val formato2="ab123cd00"
        val formato1Sbagliato="Ab123CD00"
        val formato2Sbagliato="AA123CD00"
        val formato3Sbagliato="#AA123CD00à"
        val formato4Sbagliato="a"
        val formato5Sbagliato="aaaaaaaaaaaaa"
        assertTrue("formato1 codice cliente ", viewModel.formatoCausaleValida(formato1))
        assertTrue("formato2 codice cliente ", viewModel.formatoCausaleValida(formato2))
        assertFalse("formato1Sbagliato codice cliente ", viewModel.formatoCausaleValida(formato1Sbagliato))
        assertFalse("formato2Sbagliato codice cliente ", viewModel.formatoCausaleValida(formato2Sbagliato))
        assertFalse("formato3Sbagliato codice cliente ", viewModel.formatoCausaleValida(formato3Sbagliato))
        assertFalse("formato4Sbagliato codice cliente ", viewModel.formatoCausaleValida(formato4Sbagliato))
        assertFalse("formato5Sbagliato codice cliente ", viewModel.formatoCausaleValida(formato5Sbagliato))

    }
    @Test
    fun formatoDataAccreditoValidaTest(){
        val oggi = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val domani = oggi.plus(1, DateTimeUnit.DAY)
        val tra5mesi = oggi.plus(5, DateTimeUnit.MONTH)
        val formato1 = domani.format(LocalDate.Format {dayOfMonth();chars("/");monthNumber();chars("/");year() })
        println(formato1)
        val formato2 = tra5mesi.format(LocalDate.Format {dayOfMonth();chars("-");monthNumber();chars("-");year() })
        val formato1Sbagliato="01_01_2000"
        val formato2Sbagliato="01 33 2000"
        val formato3Sbagliato="45 01 2000"
        val formato4Sbagliato="04/21/2024"
        val formato5Sbagliato="45/13/2024"
        val formato6Sbagliato="2000/01/2000"
        val formato7Sbagliato="venerdi 25 aprile 2000"
        val formato8Sbagliato="DROP * FROM * ;"

        assertTrue("formato data di nascita valido", viewModel.formatoDataAccreditoValida(formato1))
        assertTrue("formato data di nascita valido", viewModel.formatoDataAccreditoValida(formato2))
        assertFalse("formato1Sbagliato ", viewModel.formatoDataAccreditoValida(formato1Sbagliato))
        assertFalse("formato2Sbagliato ", viewModel.formatoDataAccreditoValida(formato2Sbagliato))
        assertFalse("formato3Sbagliato ", viewModel.formatoDataAccreditoValida(formato3Sbagliato))
        assertFalse("formato4Sbagliato ", viewModel.formatoDataAccreditoValida(formato4Sbagliato))
        assertFalse("formato5Sbagliato ", viewModel.formatoDataAccreditoValida(formato5Sbagliato))
        assertFalse("formato6Sbagliato ", viewModel.formatoDataAccreditoValida(formato6Sbagliato))
        assertFalse("formato7Sbagliato ", viewModel.formatoDataAccreditoValida(formato7Sbagliato))
        assertFalse("formato8Sbagliato ", viewModel.formatoDataAccreditoValida(formato8Sbagliato))
    }
    @Test
    fun formatoImportoValidoTest(){
        val formato1 = "1.00"
        val formato2 = "67890.76"
        val formato3 = "20.70"
        val formato4 = "420.70"
        val formato5 = "20.70"
        val formato1Sbagliato="pippo; DROP TABLE conti"
        val formato2Sbagliato="34,12"
        val formato3Sbagliato="-1244"
        val formato4Sbagliato="11223344) UNION SELECT 1,NULL,NULL,NULL WHERE 1=2 –-"
        val formato5Sbagliato="\\vlad2!"
        assertTrue("formato1", viewModel.formatoImportoValido(formato1))
        assertFalse("formato2", viewModel.formatoImportoValido(formato2))
        assertTrue("formato3", viewModel.formatoImportoValido(formato3))
        assertTrue("formato4", viewModel.formatoImportoValido(formato4))
        assertTrue("formato5", viewModel.formatoImportoValido(formato5))
        assertFalse("formato1Sbagliato ", viewModel.formatoImportoValido(formato1Sbagliato))
        assertFalse("formato2Sbagliato ", viewModel.formatoImportoValido(formato2Sbagliato))
        assertFalse("formato3Sbagliato ", viewModel.formatoImportoValido(formato3Sbagliato))
        assertFalse("formato4Sbagliato ", viewModel.formatoImportoValido(formato4Sbagliato))
        assertFalse("formato5Sbagliato ", viewModel.formatoImportoValido(formato5Sbagliato))
    }
}
