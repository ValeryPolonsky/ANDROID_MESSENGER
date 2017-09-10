package design.chat.template.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import design.chat.template.ActionProcessButton;
import design.chat.template.ProgressGenerator;
import design.chat.template.R;
import design.chat.template.model.User;
import design.chat.template.model.ModelFirebase;
import design.chat.template.model.Coordinates;
import design.chat.template.util.SettingsPreferences;

/**
 * The LoginActivity class to show  login view contains. This is login screen to app
 *
 * @author ATV Apps
 */
public class LoginActivity extends Activity {

    private Context mContext;
    private ActionProcessButton mButtonSignIn;
    private ActionProcessButton mButtonSwitchUser;
    private EditText mFirstNameEdittext;
    private EditText mLasNameEdittext;
    private EditText mUserNameEdittext;
    private EditText mPasswordEdittext;
    private ModelFirebase modelFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelFirebase = new ModelFirebase();
        setContentView(R.layout.login_layout);
        mContext = LoginActivity.this;


        /*if (SettingsPreferences.isLogin(mContext)) {
            startActivity(new Intent(mContext, HomeActivity.class));
        }*/

        if(modelFirebase.getCurrentUser()!=null){
            startActivity(new Intent(mContext, HomeActivity.class));
        }


        initView();
    }

    /**
     * Inits Components
     */
    private void initView() {
        mButtonSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        mButtonSwitchUser = (ActionProcessButton) findViewById(R.id.btnSwitchUser);
        mFirstNameEdittext = (EditText) findViewById(R.id.first_name_edittext);
        mLasNameEdittext = (EditText) findViewById(R.id.last_name_edittext);
        mPasswordEdittext = (EditText) findViewById(R.id.password_edittext);
        mUserNameEdittext = (EditText) findViewById(R.id.user_name_edittext);
        if(SettingsPreferences.contains(mContext,"firstname")) {
            mFirstNameEdittext.setText(SettingsPreferences.getFirstName(mContext));
            mLasNameEdittext.setText(SettingsPreferences.getLastName(mContext));
            mUserNameEdittext.setText(SettingsPreferences.getUserName(mContext));
            mFirstNameEdittext.setEnabled(false);
            mLasNameEdittext.setEnabled(false);
            mUserNameEdittext.setEnabled(false);
        }


        mButtonSwitchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirstNameEdittext.setEnabled(true);
                mLasNameEdittext.setEnabled(true);
                mUserNameEdittext.setEnabled(true);
                mFirstNameEdittext.setText("");
                mLasNameEdittext.setText("");
                mUserNameEdittext.setText("");

            }
        });

        final ProgressGenerator progressGenerator = new ProgressGenerator();
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserNameEdittext.getText().length() == 0) {
                    mUserNameEdittext.setError(getText(R.string.email_required));
                    return;
                }
                if (mPasswordEdittext.getText().length() == 0) {
                    mPasswordEdittext.setError(getText(R.string.password_required));
                    return;
                }

                if (mFirstNameEdittext.getText().length() == 0) {
                    mFirstNameEdittext.setError(getText(R.string.first_name_required));
                    return;
                }
                if (mLasNameEdittext.getText().length() == 0) {
                    mLasNameEdittext.setError(getText(R.string.last_name_required));
                    return;
                }

                final String firstName = mFirstNameEdittext.getText().toString();
                final String lastName = mLasNameEdittext.getText().toString();
                final String userName = mUserNameEdittext.getText().toString();
                final String password = mPasswordEdittext.getText().toString();
                //progressGenerator.start(mButtonSignIn);
                progressGenerator.setProcessButton(mButtonSignIn);
                progressGenerator.setmProgress(10);



                modelFirebase.checkAccountEmailExistInFirebase(userName, new ModelFirebase.GetCallbackResult() {
                    @Override
                    public void onComplete(boolean flag) {
                        if(flag){
                            modelFirebase.signIn(userName, password, new ModelFirebase.GetCallbackResult() {
                                @Override
                                public void onComplete(boolean flag) {
                                    if (flag) {
                                        progressGenerator.setmProgress(100);
                                        SettingsPreferences.setNewUserInfo(mContext,firstName,lastName, userName);
                                        initHomeActivity();
                                    } else {
                                        Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                        progressGenerator.setmProgress(-100);
                                        progressGenerator.postDelayedProgress(0);

                                    }
                                }
                            });

                        }else{
                            modelFirebase.createUser(userName, password, new ModelFirebase.GetCallbackResult() {
                                @Override
                                public void onComplete(boolean flag) {
                                    if (flag) {
                                        progressGenerator.setmProgress(100);
                                        Coordinates coordinates = new Coordinates("31.969738","34.772787");
                                        User user = new User(firstName,lastName,userName,modelFirebase.getCurrentUser().getUid(),"default",coordinates);
                                        modelFirebase.addUser(user);
                                        SettingsPreferences.setNewUserInfo(mContext,firstName,lastName, userName);
                                        SettingsPreferences.setSound(mContext,true);
                                        SettingsPreferences.setVibrate(mContext,true);
                                        SettingsPreferences.setRingtoneIndex(mContext,R.string.ring1);
                                        SettingsPreferences.setRingtoneName(mContext,getString(R.string.ringtone_1));
                                        initHomeActivity();
                                    } else {
                                        Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
                                        progressGenerator.setmProgress(-100);
                                        progressGenerator.postDelayedProgress(0);
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });
    }


   public void initHomeActivity(){
        Toast.makeText(mContext, R.string.login_succes, Toast.LENGTH_LONG).show();
        SettingsPreferences.setLogin(mContext, true);
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);


    }


    @Override
    protected void onDestroy() {
        mButtonSignIn = null;
        mPasswordEdittext = null;
        mUserNameEdittext = null;
        super.onDestroy();
    }
}
