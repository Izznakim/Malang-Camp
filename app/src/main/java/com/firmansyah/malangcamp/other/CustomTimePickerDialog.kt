package com.firmansyah.malangcamp.other

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Build
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.TimePicker
import java.lang.Exception

class CustomTimePickerDialog(
    context: Context?,
    private val mTimeSetListener: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    mIs24HourView: Boolean
) :
    TimePickerDialog(
        context, 2, null, hourOfDay, minute,
        mIs24HourView
    ) {
    private var mTimePicker: TimePicker? = null
    override fun updateTime(hourOfDay: Int, minuteOfHour: Int) {
        if (Build.VERSION.SDK_INT >= 23 ) {
            mTimePicker?.hour = hourOfDay
            mTimePicker?.minute = minuteOfHour
        }else {
            mTimePicker?.currentHour = hourOfDay
            mTimePicker?.currentMinute = minuteOfHour
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        val mHour: Int?
        val mMinute: Int?
        if (Build.VERSION.SDK_INT >= 23 ) {
            mHour=mTimePicker?.hour
            mMinute=mTimePicker?.minute
        }else {
            mHour=mTimePicker?.currentHour
            mMinute=mTimePicker?.currentMinute
        }

        if (mHour!=null && mMinute!=null) {
            when (which) {
                BUTTON_POSITIVE -> mTimeSetListener?.onTimeSet(
                    mTimePicker, mHour,
                    mMinute
                )
                BUTTON_NEGATIVE -> cancel()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            mTimePicker = findViewById(
                Resources.getSystem().getIdentifier(
                    "timePicker",
                    "id",
                    "android"
                )
            )
            val tmPicker=mTimePicker
            val mHourSpinner = tmPicker?.findViewById<NumberPicker>(
                Resources.getSystem().getIdentifier(
                    "hour",
                    "id",
                    "android"
                )
            )

            mHourSpinner?.minValue = 7
            mHourSpinner?.maxValue = 19
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}