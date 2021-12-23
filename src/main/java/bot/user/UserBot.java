package bot.user;

import bot.mainBot.MyBot;
import currency.Currency;
import currency.CurrencyService;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserBot extends MyBot {


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private String date() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter time1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return time.format(time1);
    }

    public void sendList(String chatId, String mes) {
        System.out.println("Send ");
        ArrayList<Currency> list = CurrencyService.getList();
        System.out.println(list);
        String res = mes + " \n\n";
        int ind = 1;
        for (var i : list) {


            res += (ind + ". " + i.getCcyNm_UZ() + " -- " + i.getCcy() + " -- " + i.getRate() + " so'm \n");
            ind++;

        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(res);
        System.out.println(res);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


    public void sendListVal(String chatId, String mes) {

        ArrayList<Currency> list = CurrencyService.getList();
        System.out.println(list);
        String res = mes + " \n\n";
        int ind = 1;
        for (var i : list) {
            res += (ind + ". " + i.getCcyNm_UZ() + " -- " + i.getCcy()) + "\n";
            ind++;

        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(res);
        System.out.println(res);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


    static public ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow mark = new KeyboardRow();
        mark.add("Valyuta haqida malumot");
        mark.add("Convert valyuta");

        keyboardRowList.add(mark);

        mark = new KeyboardRow();
        mark.add("So'mdan o'tkazish");
        mark.add("📃Pdf yuklab olish");
        keyboardRowList.add(mark);


        return replyKeyboardMarkup;
    }

    public void calculation(Integer fromIndex, Integer toIndex, Double amount, String chatId) {
        System.out.println("funksiya");
        Currency from = CurrencyService.getCurrency(fromIndex);
        Currency to = CurrencyService.getCurrency(toIndex);
        System.out.println(from);
        System.out.println(to);

        double amount1 = amount * Double.valueOf(from.getRate()) / Double.valueOf(to.getRate());
        BigDecimal bigDecimal = BigDecimal.valueOf(amount1);


        String res = amount + " " + from.getCcy() + " = " + bigDecimal + " " + to.getCcy();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(res);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void calculationSom(int i, double amount, String chatId) {

        Currency currency = CurrencyService.getCurrency(i);
        BigDecimal amount1 = BigDecimal.valueOf(amount * Double.valueOf(currency.getRate()));
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(amount + currency.getCcy() + "  = " + amount1 + "so'm");
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }



    @SneakyThrows
    public  void  sendPdf(String chatid) {
        PDDocument document = new PDDocument();

        PDDocumentInformation pd = document.getDocumentInformation();
        pd.setTitle("Valyuta ");
        for (int i = 0; i < 5; i++) {
            PDPage pg = new PDPage();
            document.addPage(pg);
        }

        ArrayList<Currency> list = CurrencyService.getList();
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();

        contentStream.setFont(PDType1Font.TIMES_ROMAN,18);
        contentStream.newLineAtOffset(70, 750);

        contentStream.drawString("Sana " +  list.get(0).Date);
        contentStream.newLineAtOffset(0, -25);

        for (int i = 0; i < 35; i++) {
            String res = (i+1)+ ". " + list.get(i).getCcyNm_UZ() + "   --   " +list.get(i).getCcy() + "  --  " + list.get(i).getRate() + " so'm";
            res = res.replace(" ", "");
            contentStream.drawString( res);
            contentStream.newLineAtOffset(0, -18);

        }
        contentStream.endText();
        contentStream.close();


        page = document.getPage(1);
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN,18);
        contentStream.newLineAtOffset(60, 750);
        for (int i = 35; i < 75; i++) {
            String res = (i+1)+ ". " + list.get(i).getCcyNm_UZ() + "   --   " +list.get(i).getCcy() + "  --  " + list.get(i).getRate() + " so'm";
            res = res.replace(" ", "");
            contentStream.drawString( res);
            contentStream.newLineAtOffset(0, -18);

        }
        contentStream.endText();
        contentStream.close();
        document.save(new FileOutputStream("/Users/ismoilovdavron/IdeaProjects/ApiUse/src/main/resources/valyuta.pdf"));
        document.close();

        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatid);
        sendDocument.setDocument(new InputFile(new File("/Users/ismoilovdavron/IdeaProjects/ApiUse/src/main/resources/valyuta.pdf")));
        sendDocument.setCaption("Valyuta haqida ");
        execute(sendDocument);


    }
}

