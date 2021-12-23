package bot.mainBot;

public interface TelegramBotUtils {

    String USER_NAME = "https://t.me/Valyuta_Davron_bot";
    String BOT_TOKEN = "5062486463:AAGb20p4pp__rspLdPHtv2HGerqBV3EKmZc";

    static boolean isStart(String text){
        return text.equals("/start");
    }

    static boolean isData(String text){
        return text.equals("Valyuta haqida malumot");
    }

    static boolean isConvert(String text){
        return  text.equals("Convert valyuta");
    }

    static boolean isConvertSom(String text){
        return  text.equals("So'mdan o'tkazish");
    }

    static boolean isPdf(String text){
        return  text.equals("📃Pdf yuklab olish");
    }
}
