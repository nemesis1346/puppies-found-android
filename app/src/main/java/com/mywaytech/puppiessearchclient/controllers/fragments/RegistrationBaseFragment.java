package com.mywaytech.puppiessearchclient.controllers.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.mywaytech.puppiessearchclient.models.NewUserModel;

/**
 * Created by Marco on 22/9/2016.
 */
public abstract class RegistrationBaseFragment extends Fragment {

    protected PersonalInfoRegistrationCallback mPersonalInfoRegistrationCallback;

    public abstract boolean isFormReady();


    public interface PersonalInfoRegistrationCallback {
        void updateRegistrationModel(NewUserModel newUserObject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPersonalInfoRegistrationCallback = (PersonalInfoRegistrationCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPersonalInfoRegistrationCallback = null;
    }
}
