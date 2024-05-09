package org.kps.currency.service;

import lombok.extern.slf4j.Slf4j;
import org.kps.currency.config.TelegramConfig;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.currency.service.CurrencyConverterService;
import org.kps.currency.domain.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.kps.currency.common.constants.TelegramConstants.HELP_MESSAGE;

@Slf4j
@Service
public class TelegramCompositeService extends TelegramLongPollingBot {

    private final CurrencyConverterService service;
    private final UserService userService;
    private final TelegramConfig config;
    private final ImageSenderService imageSenderService;

    @Autowired
    public TelegramCompositeService(
            CurrencyConverterService service,
            UserService userService,
            TelegramConfig config, ImageSenderService imageSenderService
    ) {
        super(config.getBotToken());
        this.service = service;
        this.userService = userService;
        this.config = config;
        this.imageSenderService = imageSenderService;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String messageText = update.getMessage().getText();
        Long userChatId = update.getMessage().getChatId();
        long chatId = userChatId;

        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            var optionalImage = update.getMessage().getPhoto();
            try {
                PhotoSize maxSize = optionalImage.getLast();
                String fileId = maxSize.getFileId();
                GetFile getFile = new GetFile(fileId);
                String filePath = execute(getFile).getFilePath();
                String response;
                try (InputStream inputStream = downloadFileAsStream(filePath)) {
                    response = imageSenderService.sendImage(inputStream);
                }
                if (!response.isEmpty()) {
                    sendMessage(chatId, response);
                }
            } catch (Exception e) {
                log.error(String.format("Error with sending image %s", e.getMessage()), e);
                //todo: при ошибке отправки сообщения нужно выводить смайлики в чат
            }
        }


        if (update.hasMessage() && update.getMessage().hasText()) {

            //todo переделать без /convert
//            if (messageText.contains("/convert")) {
//
//                CallbackQuery callbackQuery = update.getCallbackQuery();
//                String data = callbackQuery.getData();
//                handleCallbackQuery(chatId, data);
//            }


            switch (messageText) {
                case "/start":
                    userService.registerUser(update.getMessage());
                    String firstName = update.getMessage().getChat().getFirstName();
                    if (null == firstName) {
                        firstName = "user";
                    }
                    startCommandReceived(chatId, firstName);
                    log.info("Telegram bot replied to user, {}", userChatId);
                    break;
                case "/clear personnel data":
                    sendMessage(chatId, userService.deleteUser(chatId));
                    break;
                case "/help":
                    sendMessage(chatId, HELP_MESSAGE);
                    log.info("Telegram bot help");
                    break;
                default:
                    sendMessage(chatId, "Sorry, command not found");
                    log.info("Telegram default message reply to chatId {}", userChatId);
            }

        }

    }

    //todo переделать без /convert
    private void handleCallbackQuery(long chatId, String data) {
        String messageToSend = "You clicked on button with data: " + data;

        var getRates = service.getRateForQuote(new CurrencyRequestDTOConvertImpl(data, "USD", 1L));
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
