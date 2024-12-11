package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.awt.SystemColor.text;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private static final DateTimeFormatter botDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }




    @Override
    public int process(List<Update> updates) {



        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            for (Update uptade : updates) {
                if (update.message() != null && update.message().text() != null) {
                    String messageText = update.message().text();
                    Long chatId = update.message().chat().id();
                    String message = "ну шо ты лысый";
                    switch (messageText) {
                        case "/start" -> message = "Здарова отец! Чем помочь?";
                        case "stop" -> message = "не тормози меня по-братски";
                    }
                    Matcher matcher = pattern.matcher(messageText);
                    if(matcher.matches()) {
                        message = "хорошо!";
                        notificationTaskRepository.save(new NotificationTask(chatId,matcher.group(3), LocalDateTime.parse(matcher.group(1), botDateFormatter)));
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    public void sendMessage(Long chatId, String messageText) {
        telegramBot.execute(new SendMessage(chatId.toString(), messageText));
    }
}
