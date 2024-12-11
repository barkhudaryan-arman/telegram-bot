package pro.sky.telegrambot.sheduler;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationTaskSheduler {
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    private NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskSheduler(TelegramBotUpdatesListener telegramBotUpdatesListener, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBotUpdatesListener = telegramBotUpdatesListener;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void test(){
        List<NotificationTask> byNotificationDate = notificationTaskRepository.findByNotificationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        for (NotificationTask notificationTask : byNotificationDate) {
            try {
                telegramBotUpdatesListener.sendMessage(notificationTask.getChatId(), notificationTask.getTextMessage());
                notificationTaskRepository.save(new NotificationTask(notificationTask.getChatId(), notificationTask.getTextMessage(), LocalDateTime.now()));
            }catch (Exception e){
                System.err.println("Ошибка при отправке уведомления для задачи: " + notificationTask.getId());
                e.printStackTrace();
            }
        }
    }

}
