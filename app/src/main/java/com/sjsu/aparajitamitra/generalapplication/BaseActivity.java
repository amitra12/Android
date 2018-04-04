package com.sjsu.aparajitamitra.generalapplication;

/**
 * Created by aparajitamitra on 4/26/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    private final int FLAG_SELECTION_DIALOG = 0;
    private AlertDialog flagDialog;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                showDialog(FLAG_SELECTION_DIALOG);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case FLAG_SELECTION_DIALOG:
                AlertDialog.Builder rangeBuilder = new AlertDialog.Builder(this);
                rangeBuilder.setTitle(R.string.select_flag);
                rangeBuilder.setSingleChoiceItems(ApplicationEx.flag_options,
                        ApplicationEx.selectedFlag,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        ApplicationEx.selectedFlag = 0;
                                        flagDialog.dismiss();
                                        break;
                                    case 1:
                                        ApplicationEx.selectedFlag = 1;
                                        flagDialog.dismiss();
                                        break;
                                }
                            }
                        });
                flagDialog = rangeBuilder.create();
                return flagDialog;
            default:
        }
        return super.onCreateDialog(id);
    }

}