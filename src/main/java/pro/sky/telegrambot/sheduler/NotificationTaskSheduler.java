package pro.sky.telegrambot.sheduler;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationTaskSheduler {
    @Autowired
    private TelegramBot telegramBot;
    private NotificationTaskRepository notificationTaskRepository;
    private

     NotificationTaskSheduler(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void test(){
        List<NotificationTask> byNotificationDate = notificationTaskRepository.findByNotificationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

    }

}
