package edu.psu.sweng888.nightout.date_picker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private DatePickerInteractionInterface mdatePickerInteractionInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mdatePickerInteractionInterface = (DatePickerInteractionInterface) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " DatePickerInteractionInterface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = String.format("%02d/%02d/%04d",month+1,day,year);
        mdatePickerInteractionInterface.onDateComplete(date);
    }
}
