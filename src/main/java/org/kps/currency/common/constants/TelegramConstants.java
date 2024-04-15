package org.kps.currency.common.constants;


public class TelegramConstants {

    public TelegramConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String HELP_MESSAGE =
            """
                            This bot is created to convert currency. 
                            You can ask for a quote from 164 currency pairs and receive a valid result.
                            
                            Type /start to see the welcome message.
                            
                            Type /convert to convert currency pair and sum.
                            
                            Type /quote to see the quote for 1$ from the requested currency.
                            
                            Type /help to see this message.
                            
                    """;
}
