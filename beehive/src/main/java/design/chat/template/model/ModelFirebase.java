package design.chat.template.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Asi on 7/2/2017.
 */

public class ModelFirebase {

    private int count=0;

    private HashMap<String, ChildEventListener> mListenerMap = new HashMap<>();



    public interface GetCallbackResult {
        void onComplete(boolean flag);
    }

    public interface GetCoordinatesCallback {
        void onComplete(Coordinates coordinates);
    }

    public interface GetMessagesCallback{
        void onComplete(ArrayList<MessageItem>items);
    }


    public interface GetUserByIDCallback{
        void onComplete(User user);
    }

    public interface GetImageCallback{
        void onComplete(byte[] bytes );
        void onFailed(@NonNull Exception e);
    }


    public interface GetUserCallback {
        void onComplete(User user);
    }

    public interface GetContactsCallback{
        void onComplete(String[]contacts);
    }

    public interface SaveIamgeCallback{
        void onComplete(String url);
        void onFailed();
    }

    public interface GetAllUsersCallback{
        void onComplete(List<User> users);
        void onCancel();
    }

    public interface GetNewMessageEvent{
        void onChildAdded(MessageItem messageItem);
    }

    public interface GetLastMessageEvent{
        void onComplete(ArrayList<MessageItem>items);
    }

    public interface  GetLastMessageTimeEvent{
        void onComplete(String time);
    }

    public void checkAccountEmailExistInFirebase(String email, final GetCallbackResult callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                callback.onComplete(!task.getResult().getProviders().isEmpty());
            }
        });
    }



    public void createUser(String email, String pass, final GetCallbackResult callback) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        (firebaseAuth.createUserWithEmailAndPassword(email, pass)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                callback.onComplete(task.isSuccessful());

            }
        });
    }


    public void signIn(String email, String pass, final GetCallbackResult callback) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        (firebaseAuth.signInWithEmailAndPassword(email, pass)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                callback.onComplete(task.isSuccessful());
            }
        });
    }

    public void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }

    public void addUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(user.getUserID()).setValue(user);
    }

    public void getLastMessageTime(String userID,final GetLastMessageTimeEvent callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        ref.child(getCurrentUserID()).child("messages").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("messageCount")) {
                    callback.onComplete(dataSnapshot.child("messageCount").getValue().toString());
                }else{
                    callback.onComplete("-1");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateFirstNameAndLastName(String firstName,String lastName){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).child("firstName").setValue(firstName);
        ref.child(getCurrentUserID()).child("lastName").setValue(lastName);
    }

    public void addMessage(final String sendToUserID,final String sentFrom,final String sentTo,final MessageItem message){


        getLastMessageTime(sendToUserID, new GetLastMessageTimeEvent() {
            @Override
            public void onComplete(String time) {

                String currentTime = "";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("users");
                int counter=Integer.parseInt(time)+1;
                currentTime = ""+counter;
                ref.child(getCurrentUserID()).child("messages").child(sendToUserID).child("messageCount").setValue(currentTime);
                ref.child(sendToUserID).child("messages").child(getCurrentUserID()).child("messageCount").setValue(currentTime);

                message.setSentByMe(true);
                ref.child(getCurrentUserID()).child("messages").child(sendToUserID).child("messages").child(currentTime).setValue(message);
                ref.child(getCurrentUserID()).child("messages").child(sendToUserID).child("timesKeys").child(currentTime).setValue(currentTime);

                MessageItem lastMessage = new MessageItem(message);
                lastMessage.setUserID(sendToUserID);
                lastMessage.setName(sentTo);
                Log.d("lastMessageMine",lastMessage.toString());
                ref.child(getCurrentUserID()).child("messages").child(sendToUserID).child("lastMessage").setValue(lastMessage);


                message.setSentByMe(false);
                ref.child(sendToUserID).child("messages").child(getCurrentUserID()).child("messages").child(currentTime).setValue(message);
                ref.child(sendToUserID).child("messages").child(getCurrentUserID()).child("timesKeys").child(currentTime).setValue(currentTime);

                lastMessage=message;
                lastMessage.setUserID(getCurrentUserID());
                lastMessage.setName(sentFrom);
                Log.d("lastMessageSentTo",lastMessage.toString());
                ref.child(sendToUserID).child("messages").child(getCurrentUserID()).child("lastMessage").setValue(message);
            }
        });




    }


    public void updateLocation(Coordinates coordinates){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).child("coordinates").setValue(coordinates);

    }



    public void getMessages(final String userID,final GetMessagesCallback callback){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("messages").child(userID).child("messages").getChildrenCount()<1) {
                    callback.onComplete(null);
                    return;
                }


                String timesKeys = dataSnapshot.child("messages").child(userID).child("timesKeys").getValue().toString();
                timesKeys = timesKeys.replace ("[", "").replace ("]", "");
                timesKeys = timesKeys.replace (" ", "");

                Log.d("timesKeys",timesKeys);

                String[] finalTimesKeys= timesKeys.split(",");

                Arrays.sort(finalTimesKeys);
                ArrayList<MessageItem>items=new ArrayList<MessageItem>();

                for(int i=0;i<finalTimesKeys.length;i++)
                {

                    MessageItem  messageItem = dataSnapshot.child("messages").child(userID).child("messages").child(finalTimesKeys[i]).getValue(MessageItem.class);
                    if(messageItem.isSentByMe())
                        messageItem.setUserMessage(false);//Show message from right side
                    else
                        messageItem.setUserMessage(true);//Show message from left side

                    items.add(messageItem);
                }

               callback.onComplete(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getLastMessageItem(final GetLastMessageEvent callback){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MessageItem>items=new ArrayList<MessageItem>();
                for(DataSnapshot child : dataSnapshot.getChildren() ){
                    //Log.d("getLastMessageItem",""+child.getKey());
                    //Log.d("getLastMessageItem",""+child.child("lastMessage").toString());
                    MessageItem item=child.child("lastMessage").getValue(MessageItem.class);
                    //item.setUserID(child.getKey());
                    items.add(item);
                }
                callback.onComplete(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void removeNewMessageListener(String userID){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).child("messages").child(userID).child("messages").removeEventListener(mListenerMap.get(userID));
    }

    public void setNewMessageListener(int messagesCount, String userID, final GetNewMessageEvent callback){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        count= messagesCount;
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("onChildAddedAA",""+dataSnapshot.getValue().toString());
                if(count==0) {
                    MessageItem newMessage = dataSnapshot.getValue(MessageItem.class);
                    if(newMessage.getMessage()!=null) {
                        if (newMessage.isSentByMe())
                            newMessage.setUserMessage(false);
                        else
                            newMessage.setUserMessage(true);

                        callback.onChildAdded(newMessage);
                    }
                }
                else
                    count--;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.child(getCurrentUserID()).child("messages").child(userID).child("messages").addChildEventListener(listener);
        mListenerMap.put(userID,listener);
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserID(){
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void saveImageToFirebase(Bitmap bitmap,String name ,final SaveIamgeCallback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("Avatars").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailed();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.onComplete(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }


    public void getUserByID(String userID,final GetUserByIDCallback callback){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user  = dataSnapshot.getValue(User.class);
                callback.onComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getCoordinatesByID(String userID, final GetCoordinatesCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(userID).child("coordinates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coordinates coordinates = dataSnapshot.getValue(Coordinates.class);
                callback.onComplete(coordinates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getContactsID(final GetContactsCallback callback){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("contacts").getChildrenCount()<1)
                    return;
                Log.d("contactsAAA", dataSnapshot.child("contacts").getValue().toString());
                String parse ="[ ,={]"; // Because: {7KO16YBuTENwGq9bRI5Kk8zeUBv2=7KO16YBuTENwGq9bRI5Kk8zeUBv2, 26HAAhAUk7gPhRy7LrPJpgGR1W63=26HAAhAUk7gPhRy7LrPJpgGR1W63}
                String contact = dataSnapshot.child("contacts").getValue().toString();
                String[] contactID= contact.split(parse);
                String[] finalList=new String[contactID.length/3];
                int j=0;
                for(int i=1;i<contactID.length;i+=3) // The reason we increment i by 3 each loop, is because the string holding he contacts is being parsed by ",={"
                    // if this isn't clear, check the comment, 7 lines above. (after the 'String parse' declaration)
                {
                    finalList[j]=contactID[i];
                    Log.d("contactsAAB", "Contact ID in index "+j+ " is: "+finalList[j]);
                    j++;
                }
                callback.onComplete(finalList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void setDataField(String field,String data){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUser().getUid()).child(field).setValue(data);
    }



    public void getAvatarFromStorage(final GetImageCallback callback){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        ref.child("Avatars/"+getCurrentUserID()+".jpeg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                callback.onComplete(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailed(e);
            }
        });
    }


    public void getAvatarFromStorageAccordingToUserID(String userID,final GetImageCallback callback){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();
        ref.child("Avatars/"+userID+".jpeg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                callback.onComplete(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailed(null);
            }
        });
    }



    public void addContactToContactsList(String userID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUserID()).child("contacts").child(userID).setValue(userID);
        ref.child(userID).child("contacts").child(getCurrentUserID()).setValue(getCurrentUserID());
    }
    public void isContactExists(final String userID, final GetCallbackResult callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(getCurrentUserID()).child("contacts");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag=false;
                for (DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Log.d("flagB",""+snap.getValue().toString());
                    if (snap.getValue().toString().equals(userID)){
                        Log.d("testLL",snap.getValue().toString());
                        flag=true;
                        break;
                    }
                }
                callback.onComplete(flag);
                Log.d("testLL",""+flag);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeContact(String userID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(getCurrentUser().getUid()).child("contacts").child(userID).removeValue();
    }


    public void searchUsersByName(final String searchText, final GetAllUsersCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new LinkedList<User>();
                if(dataSnapshot.getChildrenCount()<1) {
                    callback.onComplete(users);
                    return;
                }
                for (DataSnapshot snap : dataSnapshot.getChildren()){

                    String firstName = ""+snap.child("firstName").getValue().toString().toLowerCase();
                    String lastName = "" + snap.child("lastName").getValue().toString().toLowerCase();
                    String name=firstName+" "+lastName;
                    String search=searchText.toLowerCase();
                    if(name.contains(search)) {
                        firstName = ""+snap.child("firstName").getValue().toString();
                        lastName = "" + snap.child("lastName").getValue().toString();
                        String userName = "" + snap.child("userName").getValue();
                        String avatar = "" + snap.child("avatar").getValue();
                        String userID = "" + snap.child("userID").getValue();
                        String lat = "" + snap.child("coordinates").child("altitude").getValue();
                        String longt = "" + snap.child("coordinates").child("longtitude").getValue();
                        Coordinates coordinates = new Coordinates(lat, longt);
                        users.add(new User(firstName, lastName, userName, userID, avatar, coordinates));
                    }
                }
                callback.onComplete(users);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}






