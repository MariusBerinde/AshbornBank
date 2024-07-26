package com.example.ashborn.view

import android.os.Build
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
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun CustomDatePicker() {
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
            yearRange = 1990..2000,
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
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    yearRange: IntRange = DatePickerDefaults.YearRange,
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    var startDay = System.currentTimeMillis()
    val showDatePicker = remember { mutableStateOf(false) }
    val state =// rememberDatePickerState(yearRange = yearRange)
    rememberDatePickerState(
        initialDisplayedMonthMillis = System.currentTimeMillis(),
        yearRange = 2000..2024,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
                        .toLocalDate().dayOfWeek
                    dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY
                } else {
                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.timeInMillis = utcTimeMillis
                    calendar[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY &&
                            calendar[Calendar.DAY_OF_WEEK] != Calendar.SATURDAY
                }
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