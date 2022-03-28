package bot.chat;

import bot.chat.ChatObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatObjectService {

    private static String url = "/Users/ismoilovdavron/IdeaProjects/ApiUse/src/main/resources/users.json";

    static ObjectMapper objectMapper = new ObjectMapper();

    public  static  int add(ChatObject chatId) {
        ArrayList<ChatObject> chatIds = getList();

        if (chatIds == null)
            chatIds = new ArrayList<>();

        chatIds.add(chatId);
        try {
            objectMapper.writeValue(new File(url), chatIds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public  static  ArrayList<ChatObject> getList() {
        try {
            ArrayList<ChatObject> chatIdArrayList = objectMapper.readValue(new File(url), new TypeReference<ArrayList<ChatObject>>() {
            });
            return chatIdArrayList;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public  static  ChatObject getModel(String chatId) {
        ArrayList<ChatObject> chatIds = getList();
        if (chatIds == null)
            chatIds = new ArrayList<>();

        for (int i = 0; i < chatIds.size(); i++) {
            if (chatIds.get(i).getChatId().equals(chatId)) {
                return chatIds.get(i);
            }
        }
        return null;
    }

    public static  boolean check(String chatid){
        for(ChatObject i : getList()){
            if (i.getChatId().equals(chatid))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ChatObject object = new ChatObject();
        object.setChatId("13245");
        add(object);
        System.out.println(getList());

    }
    public  static  Integer update(ChatObject chatId) {
        List<ChatObject> chatIdList = getList();

        if (chatIdList == null) chatIdList = new ArrayList<>();

        for (int i = 0; i < chatIdList.size(); i++) {
            if (chatIdList.get(i).getChatId().equals(chatId.getChatId())) {
                if (chatId.getState() != null)
                    chatIdList.get(i).setState(chatId.getState());
                if (chatId.getFromIn() != null)
                    chatIdList.get(i).setFromIn(chatId.getFromIn());
                if (chatId.getToIn() != null)
                    chatIdList.get(i).setToIn(chatId.getToIn());
                if (chatId.getSomIndex() != null)
                    chatIdList.get(i).setSomIndex(chatId.getSomIndex());
            }
        }

        try {
            objectMapper.writeValue(new File(url), chatIdList);
        } catch (IOException e) {
            System.out.println("ChatService.class => update()  " + e);
        }
        return null;
    }

}
