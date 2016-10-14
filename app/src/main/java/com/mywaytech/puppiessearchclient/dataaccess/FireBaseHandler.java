package com.mywaytech.puppiessearchclient.dataaccess;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mywaytech.puppiessearchclient.models.NewUserModel;
import com.mywaytech.puppiessearchclient.models.ReportModel;

import java.util.UUID;

/**
 * Created by m.maigua on 7/15/2016.
 */
public class FireBaseHandler {

    private static FireBaseHandler instance = null;
    private Context mContext;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageRef;

    public static final String OBJECT_USERS_NAME = "USERS";
    public static final String REPORTS = "REPORTS";

    private FireBaseHandler(Context context) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = mFirebaseStorage.getReferenceFromUrl("gs://puppiessearch-7c275.appspot.com");
        this.mContext = context;
    }

    public StorageReference getmStorageRef() {
        return mStorageRef;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public DatabaseReference getFirebaseDatabaseReference() {
        return mFirebaseDatabaseReference;
    }

    public synchronized static FireBaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new FireBaseHandler(context);
        }
        return instance;
    }

    public void fireBaseLogin(String email, String password, final Activity activity, final CallbackLogin callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        task.addOnFailureListener(activity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("LoginOnCompleteError: ", " " + e);
                            }
                        });
                        if (callback != null) {
                            Log.d("Success Login: ", "" + task.isSuccessful());
                            callback.onCompleteLogging(task.isSuccessful());

                        }
                    }
                });
    }

    public void fireBaseSignIn(String email, String password, final Activity activity, final CallbackSign callback) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        task.addOnFailureListener(activity, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SignOnCompleteError: ", " " + e);
                            }
                        });
                        if (callback != null) {
                            Log.d("Success Sign: ", "" + task.isSuccessful());
                            callback.onCompleteSigning(task.isSuccessful());

                        }
                    }
                });
    }

    public interface CallbackSign {
        void onCompleteSigning(boolean isSigned);
    }

    public interface CallbackLogin {
        void onCompleteLogging(boolean isLogged);
    }


    public boolean saveUserObject(NewUserModel newUserObject) {
        String uid = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            newUserObject.setUid(user.getUid());
            mFirebaseDatabaseReference.child(OBJECT_USERS_NAME).child(uid).setValue(newUserObject);
            return true;
        } else {
            Log.d("error in creation", "" + "error");
            return false;
        }

    }

    public boolean savePetObject(ReportModel reportModel) {
        String uid = UUID.randomUUID().toString();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Log.d("report uname: ", "" + reportModel.getuName());
        Log.d("report upath: ", "" + reportModel.getImagePath());

        if (user != null) {
            mFirebaseDatabaseReference.child(REPORTS).child(uid).setValue(reportModel);
            return true;
        } else {
            Log.d("error in creation", "" + "error");
            return false;
        }
    }

    public StorageReference setImageFirebaseStorageReference(String imageId) {
        return mStorageRef.child("images/petImage" + imageId + ".jpg");
    }

    public StorageReference getImageFirebaseStorageReference(String imagePath) {
        return mStorageRef.child(imagePath);
    }

    public StorageReference getUserObjectFirebaseStorageReference(String imagePath) {
        return mStorageRef.child(imagePath);
    }

    public DatabaseReference getUserObjectFirebaseDatabaseReference() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        return mFirebaseDatabaseReference.child(FireBaseHandler.OBJECT_USERS_NAME).child(user.getUid());
    }

    public DatabaseReference getReportsFirebaseDatabaseReference() {
        return mFirebaseDatabaseReference.child(REPORTS);
    }



}
