package edu.psu.sweng888.nightout.time_picker;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private TimePickerInteractionInterface mtimePickerInteractionInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mtimePickerInteractionInterface = (TimePickerInteractionInterface) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " TimePickerInteractionInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final java.util.Calendar c = java.util.Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String getAMPMValue = "AM";
        if(hourOfDay>11) {
            getAMPMValue = "PM";
            if (hourOfDay != 12) {
                hourOfDay = hourOfDay - 12;
            }
        }
        String time = String.format("%02d:%02d %s",hourOfDay,minute,getAMPMValue);
        mtimePickerInteractionInterface.onTimeComplete(time);

    }
}
