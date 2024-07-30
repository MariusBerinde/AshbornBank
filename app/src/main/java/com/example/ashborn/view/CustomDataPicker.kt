package com.example.ashborn.view

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.datetime.toKotlinLocalDate
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun CustomDatePicker(useCase: DateUseCase, yearRange: IntRange) {
    val date = remember { mutableStateOf(LocalDate.now())}
    val isOpen = remember { mutableStateOf(false)}

        OutlinedTextField(
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            value = date.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            label = { Text("Date") },
            onValueChange = {},
            trailingIcon = {
                IconButton(
                    onClick = { isOpen.value = true } // show de dialog
                ) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                }
            }
        )




    if (isOpen.value) {
        CustomDatePickerDialog(
            yearRange = yearRange,
            onAccept = {
                isOpen.value = false // close dialog

                if (it != null) { // Set the date
                    date.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                }
            },
            onCancel = {
                isOpen.value = false //close dialog
            },
            useCase = useCase
        )
    }
}

fun convertLongToTimeWithLocale(dateAsMilliSecond: Long?):String{
    if(dateAsMilliSecond == null)
        return "data nulla"
    else{
        val date = Date(dateAsMilliSecond)
        val language = "en"
        val formattedDateAsDigitMonth = SimpleDateFormat("dd/MM/yyyy", Locale(language))
        return formattedDateAsDigitMonth.format(date)
    }


}

enum class DateUseCase {
    NASCITA, BONIFICO, MAV
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    yearRange: IntRange = DatePickerDefaults.YearRange,
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit,
    useCase: DateUseCase
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val tag = object {}.javaClass.enclosingMethod.name
    val state = rememberDatePickerState(
        initialDisplayedMonthMillis = System.currentTimeMillis(),
        yearRange = yearRange,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val eighteenYearsAgo = LocalDate.now().minusYears(18)
                //TODO: aggiustare e fare unit test
                val eighteenYearsAgoToMillis = eighteenYearsAgo.toEpochDay() * 86400000 //milliseconds in a day
                val currentDate = Calendar.getInstance().timeInMillis
                when(useCase){
                    DateUseCase.NASCITA -> {return utcTimeMillis <= eighteenYearsAgoToMillis } // Verifica se la data è nel passato o oggi

                    DateUseCase.MAV -> {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = utcTimeMillis
                        val eighteenMonthsFromNowToMillis = LocalDate.now().plusMonths(18).toEpochDay()*86400000
                        return utcTimeMillis in currentDate..eighteenMonthsFromNowToMillis &&
                            calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY &&
                            calendar[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY &&
                            !isHoliday(utcTimeMillis)
                    }

                    DateUseCase.BONIFICO -> {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = utcTimeMillis
                        val eighteenMonthsFromNowToMillis = LocalDate.now().plusMonths(12).toEpochDay()*86400000
                        return utcTimeMillis in currentDate..eighteenMonthsFromNowToMillis &&
                                calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY &&
                                calendar[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY &&
                                !isHoliday(utcTimeMillis)
                    }

                    else -> return true
                }
            }

            private fun isHoliday(utcTimeMillis: Long): Boolean {
                val date: LocalDate = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC")).toLocalDate()
                val holidays = listOf<LocalDate>(
                    LocalDate.of(LocalDate.now().year,1, 1),
                    LocalDate.of(LocalDate.now().year,1, 6),
                    LocalDate.of(LocalDate.now().year,4, 25),
                    LocalDate.of(LocalDate.now().year,5, 1),
                    LocalDate.of(LocalDate.now().year,6, 2),
                    LocalDate.of(LocalDate.now().year,8, 15),
                    LocalDate.of(LocalDate.now().year,11, 1),
                    LocalDate.of(LocalDate.now().year,12, 8),
                    LocalDate.of(LocalDate.now().year,12, 25),
                    LocalDate.of(LocalDate.now().year,12, 26),
                )
                return date in holidays
            }

            override fun isSelectableYear(year: Int): Boolean {
                return true
            }
        }
    )
    DatePickerDialog(
        onDismissRequest = {
            showDatePicker.value = false
        },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {

        DatePicker(state = state,
            )
        Log.i("CustomDataPicker","data selezionata = ${convertLongToTimeWithLocale(state.selectedDateMillis)}")
    }
}