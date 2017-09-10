package design.chat.template.model;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by valerius1001 on 25/07/2017.
 */

public class GlobalBus {
    private static EventBus sBus =null;
    public static EventBus getBus(){
        if(sBus==null){
            sBus=EventBus.getDefault();
        }
        return sBus;
    }

    private  GlobalBus(){

    }
}
