package bot.admin;

import bot.chat.ChatObject;
import bot.chat.ChatObjectService;
import bot.mainBot.MyBot;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminBot extends MyBot{

    static  String url  = "/Users/ismoilovdavron/IdeaProjects/ApiUse/src/main/resources/users.json";
    static  String urlXls = "/Users/ismoilovdavron/IdeaProjects/ApiUse/src/main/resources/user.xls";

    public    void  sendInfo(Update update) throws Exception {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(update.getMessage().getChatId().toString());
        sendDocument.setCaption("Information about users");

        FileInputStream fileInputStream = new FileInputStream(urlXls);


        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheet("sheet1");

        XSSFRow row = sheet.getRow(0);
        row.getCell(0).setCellValue("Chat id");
        row.getCell(1).setCellValue("username");
        row.getCell(2).setCellValue("State ");
        row.getCell(2).setCellValue("isAdmin  ");

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<ChatObject> users = objectMapper.readValue(new File(url), new TypeReference<ArrayList<ChatObject>>() {});

        int index = 1;
        for(ChatObject user: users) {
            XSSFRow row1 = sheet.getRow(index++);

            row1.getCell(0).setCellValue(user.getChatId().toString());
            row1.getCell(1).setCellValue(user.getUsername());
            row1.getCell(2).setCellValue(user.getState().toString());
            row1.getCell(2).setCellValue(user.isAdmin());

        }

        FileOutputStream fileOutputStream = new FileOutputStream(urlXls);
        workbook.write(fileOutputStream);
        fileInputStream.close();
        fileOutputStream.close();
        workbook.close();

        sendDocument.setDocument(new InputFile(new File(urlXls)));

        execute(sendDocument);


    }

    @SneakyThrows
    public void sendNotification(int index, Message message) {
        ArrayList<ChatObject> userList = ChatObjectService.getList();
        System.out.println(userList);;

        if (index + 5 < userList.size())
            for (int i = index; i < index + 5; i++) {
                if (message.hasText()) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(userList.get(i).getChatId());
                    sendMessage.setText(message.getText());
                    execute(sendMessage);
                } else if (message.hasPhoto()) {
                    SendPhoto sendPhoto = (SendPhoto) message.getPhoto();
                    execute(sendPhoto);
                }
            }
        else if (index < userList.size() && index  + 5 > userList.size()) {
            for (int i = index; i < userList.size(); i++) {
                if (message.hasText()) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(userList.get(i).getChatId());
                    sendMessage.setText(message.getText());
                    execute(sendMessage);
                } else if (message.hasPhoto()) {
                    SendPhoto sendPhoto = (SendPhoto) message.getPhoto();
                    execute(sendPhoto);
                }
            }

        }
    }

    public void sendMessage(Message message) {
        ArrayList<ChatObject> userList = ChatObjectService.getList();
        if (userList != null)
            for (int i = 0; i < userList.size(); i = i + 5) {

                int finalI = i;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendNotification(finalI, message);
                    }
                });
                thread.start();


            }

    }

     public ReplyKeyboardMarkup adminMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow mark = new KeyboardRow();
        mark.add("UserList");
        mark.add("Send message");


        keyboardRowList.add(mark);


        return replyKeyboardMarkup;
    }

}
