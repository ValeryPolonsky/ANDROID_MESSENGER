package design.chat.template;
import android.os.Handler;


public class ProgressGenerator {

    private ProcessButton processButton;
    private int mProgress=10 ;

    public ProgressGenerator() {

    }

    public void setProcessButton(ProcessButton button){
        processButton=button;
    }


    public void setmProgress(int i){
        mProgress = i;
        processButton.setProgress(mProgress);
    }

    public void postDelayedProgress(final int i){
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        setmProgress(i);
                    }
                }, 3000);
    }

}
