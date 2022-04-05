package com.pro.empdep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pro.empdep.model.Message;
import com.pro.empdep.repo.MessageRepository;

import java.util.List;

public class MessagesViewModel extends AndroidViewModel {

    MessageRepository messageRepository;
    private LiveData<List<Message>> messagesData = new MutableLiveData<>();
    private LiveData<Boolean> messageSent = new MutableLiveData<>();
    private LiveData<Boolean> placeSent = new MutableLiveData<>();

    public MessagesViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository();

    }


    public LiveData<List<Message>> getMessages(String groupId) {
        messagesData = messageRepository.getMessages(groupId);
        return messagesData;
    }

    public LiveData<Boolean> sendNormalMessage(String groupId, String message) {
        messageSent = messageRepository.sendMessageNormal(groupId, message);
        return messageSent;
    }

    public LiveData<Boolean> sendPlaceSuggestion(String groupid, String placeid) {
        placeSent = messageRepository.sendPlaceSuggestion(groupid, placeid);
        return placeSent;
    }

}
