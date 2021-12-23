package bot.mainBot;


import bot.admin.AdminBot;
import bot.chat.ChatObject;
import bot.chat.ChatObjectService;
import bot.chat.State;
import bot.user.UserBot;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class MyBot extends TelegramLongPollingBot implements TelegramBotUtils {
    static String chatId;
    static ChatObjectService chatObjectService = new ChatObjectService();
    static AdminBot adminBot = new AdminBot();
    static UserBot userBot = new UserBot();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        this.chatId = msg.getChatId().toString();
        ChatObject chatObject = ChatObjectService.getModel(chatId);
        System.out.println(msg.getChat().getUserName());

        if (chatObject == null) {
            chatObjectService.add(new ChatObject(chatId, null, msg.getChat().getUserName(), null, null, null, false));
            chatObject = chatObjectService.getModel(chatId);
        } else {
            chatObject = chatObjectService.getModel(chatId);
        }


        if (update.hasMessage()) {
            String message_text = update.getMessage().getText();
            System.out.println(message_text);
            this.chatId = update.getMessage().getChatId().toString();
            if (!chatObject.isAdmin()) {
                if (TelegramBotUtils.isStart(message_text)) {
                    chatObject.setState(State.start);
                    ChatObjectService.update(chatObject);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Kerakli bo'limni tanlang ");
                    sendMessage.setReplyMarkup(userBot.mainMenu());
                    execute(sendMessage);

                } else if (TelegramBotUtils.isData(message_text)) {
                    System.out.println("data");
                    chatObject.setState(State.data);
                    ChatObjectService.update(chatObject);
                    userBot.sendList(chatId, "Malumot");
                } else if (TelegramBotUtils.isConvertSom(message_text)) {
                    chatObject.setState(State.som);
                    ChatObjectService.update(chatObject);
                    userBot.sendListVal(chatId, "Qaysi  valyutadan o'tkazasiz !!");
                }else if (TelegramBotUtils.isPdf(message_text)){
                    userBot.sendPdf(chatId);

                } else  if (userBot.isNumeric(message_text) && chatObject.getState() == State.som) {
                    chatObject.setSomIndex(Integer.valueOf(message_text));
                    chatObject.setState(State.somIndex);
                    ChatObjectService.update(chatObject);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Mablag'ni kiriting ");
                    execute(sendMessage);

                } else if (userBot.isNumeric(message_text) && chatObject.getState() == State.somIndex) {

                    userBot.calculationSom(chatObject.getSomIndex(), Double.valueOf(message_text), chatId);
                } else if (TelegramBotUtils.isConvert(message_text)) {
                    chatObject.setState(State.calculate);
                    ChatObjectService.update(chatObject);
                    userBot.sendListVal(chatId, "⬇️ Qaysi  valyutani o'tkamoqchisiz ⬇️ \n");

                } else if (userBot.isNumeric(message_text) && chatObject.getState() == State.calculate) {
                    chatObject.setState(State.from);
                    chatObject.setFromIn(Integer.valueOf(message_text));
                    ChatObjectService.update(chatObject);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Qaysi valyutaga : ");
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);

                } else if (userBot.isNumeric(message_text) && chatObject.getState() == State.from) {
                    chatObject.setState(State.to);
                    chatObject.setToIn(Integer.valueOf(message_text));
                    ChatObjectService.update(chatObject);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(" 💰 Mablag'ni kiriting ");
                   execute(sendMessage);
                } else if (userBot.isNumeric(message_text) && chatObject.getState() == State.to) {
                    Double amount = Double.parseDouble(message_text);
                    userBot.calculation(chatObject.getFromIn(), chatObject.getToIn(), amount, chatId);
                    chatObject.setState(State.start);
                    ChatObjectService.update(chatObject);
                }
            } else {
                if (TelegramBotUtils.isStart(message_text)) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(" Choose  ");
                    sendMessage.setReplyMarkup(adminBot.adminMenu());
                   execute(sendMessage);
                } else if (message_text.equals("UserList")) {
                    adminBot.sendInfo(update);
                } else if (message_text.equals("Send message")) {
                    chatObject.setState(State.message);
                    ChatObjectService.update(chatObject);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("Jo'natmoqchi bo'lgan message kiriting ");
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                } else if (chatObject.getState() == State.message) {
                    Message message = update.getMessage();
                    adminBot.sendMessage(message);
                    chatObject.setState(State.start);
                }


            }


        } else if (update.hasCallbackQuery()) {
        }
    }


    @Override
    public String getBotUsername() {

        return TelegramBotUtils.USER_NAME;
    }

    @Override
    public String getBotToken() {

        return TelegramBotUtils.BOT_TOKEN;
    }


}
