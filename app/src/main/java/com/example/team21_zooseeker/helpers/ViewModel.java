package com.example.team21_zooseeker.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.team21_zooseeker.R;
import com.example.team21_zooseeker.activities.search_select.SearchSelectActivity;
import com.example.team21_zooseeker.helpers.Alerts;
import com.example.team21_zooseeker.helpers.ExhibitDao;
import com.example.team21_zooseeker.helpers.ExhibitDatabase;
import com.example.team21_zooseeker.helpers.ExhibitEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewModel extends AndroidViewModel {
    private LiveData<List<ExhibitEntity>> exhibitEntities;
    private final ExhibitDao exhibitDao;
    private TextView counterDisplay;

    public ViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitDatabase db = ExhibitDatabase.getSingleton(context);
        exhibitDao = db.exhibitDao();
    }

    public void setCounterDisplay(TextView view) {
        this.counterDisplay = view;
    }

    public LiveData<List<ExhibitEntity>> getExhibitEntities() {
        if (exhibitEntities == null) {
            loadUsers();
        }
        return exhibitEntities;
    }

    private void loadUsers() {
        exhibitEntities = exhibitDao.getAllLive();
    }

    public void deleteCompleted(ExhibitEntity exhibit) {
        exhibitDao.delete(exhibit);
        updateCounterDisplay();
    }

    public void deleteAll() {
        exhibitDao.deleteAll();
        updateCounterDisplay();
    }

    public void insertExhibit(Activity activity, ExhibitEntity exhibit) {
        if (exhibitDao.get(exhibit.id) != null)
            Alerts.showAlert(activity, "You have already selected this animal.");
        else {
            exhibitDao.insert(exhibit);
            updateCounterDisplay();
        }
    }

    public String getCount() {
        return String.valueOf(exhibitDao.getAll().size());
    }

    private void updateCounterDisplay() {
        counterDisplay.setText(getCount());
    }
}
