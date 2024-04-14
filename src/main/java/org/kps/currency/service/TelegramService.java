package org.kps.currency.service;

import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOGetListImpl;
import org.kps.currency.domain.currency.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TelegramService extends TelegramLongPollingBot {


    @Autowired
    private CurrencyConverterService service;
    private static final String BOT_TOKEN = "7060104792:AAHUmMq2AY8UsPKhvHVP-1H4Bm-bRko4Om0";

    private static final String HELP_MESSAGE =
            """
                            This bot is created to convert currency. 
                            You can ask for a quote from 164 currency pairs and receive a valid result.
                            
                            Type /start to see the welcome message.
                            
                            Type /convert to convert currency pair and sum.
                            
                            Type /quote to see the quote for 1$ from the requested currency.
                            
                            Type /help to see this message.
                            
                    """;

    @Value("${tg.bot_name}")
    public String botName;

    public TelegramService() {
        super(BOT_TOKEN);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.contains("/convert") && update.hasCallbackQuery()){

                CallbackQuery callbackQuery = update.getCallbackQuery();
                String data = callbackQuery.getData();
                handleCallbackQuery(chatId, data);


            }



            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_MESSAGE);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command not found");
            }

        }

    }

    private void handleCallbackQuery(long chatId, String data) {
        String messageToSend = "You clicked on button with data: " + data;

        var getRates = service.getRateForQuote(new CurrencyRequestDTOConvertImpl(data,"USD", 1L));
        sendMessage(chatId, getRates.toString());
        sendMessage(chatId, messageToSend);
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hello " + name + ", welcome!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = getKeyboardRows();

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
            throw new RuntimeException(e);
        }
    }

    private static List<KeyboardRow> getKeyboardRows() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row.add("USD");
        row.add("EUR");
        row.add("RUB");
        row.add("UAH");
        row.add("GEL");
        row.add("TRY");
        row.add("JPY");

        row2.add("GBP");
        row2.add("AUD");
        row2.add("BYN");
        row2.add("AMD");
        row2.add("KZT");
        row2.add("CZK");
        row2.add("AZN");

        keyboardRows.add(row);
        keyboardRows.add(row2);
        return keyboardRows;
    }

}
