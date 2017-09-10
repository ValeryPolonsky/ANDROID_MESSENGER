package design.chat.template.activity.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import design.chat.template.R;
import design.chat.template.model.ModelFirebase;
import design.chat.template.util.AppUtils;

public class ProfileImageActivity extends Activity {

    ImageView image;
    ModelFirebase firebaseModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        firebaseModel = new ModelFirebase();
        init();
    }

    private void init() {
        image = (ImageView) findViewById(R.id.personal_profile_imageview);
        firebaseModel.getAvatarFromStorage(new ModelFirebase.GetImageCallback() {
            @Override
            public void onComplete(byte[] bytes) {
                image.setImageBitmap(AppUtils.getBitMapFromByteArray(bytes));
            }

            @Override
            public void onFailed(@NonNull Exception e) {

                image.setImageResource(R.drawable.avatar_default_user);
            }
        });
    }
}
