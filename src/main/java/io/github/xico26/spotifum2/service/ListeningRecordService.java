package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.ListeningRecordDAO;
import io.github.xico26.spotifum2.dao.UserDAO;
import io.github.xico26.spotifum2.dao.UserDAOImpl;
import io.github.xico26.spotifum2.model.entity.ListeningRecord;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.plan.ISubscriptionPlan;
import io.github.xico26.spotifum2.model.entity.plan.SubscriptionPlanFactory;

import java.time.LocalDateTime;
import java.util.List;

public class ListeningRecordService {
    private final ListeningRecordDAO lrDAO;

    public ListeningRecordService(ListeningRecordDAO lrDAO) {
        this.lrDAO = lrDAO;
    }

    public void clearHistory(User user) {
        lrDAO.deleteByUser(user);
    }

    public void registerMusicPlay(User u, Music m) {
        ISubscriptionPlan plan = SubscriptionPlanFactory.createPlan(u.getSubscriptionPlan());
        plan.addPoints(m, u);

        ListeningRecord lr = new ListeningRecord(u, m, LocalDateTime.now());
        lrDAO.save(lr);

        UserDAO userDAO = new UserDAOImpl();
        userDAO.save(u);
    }

    public boolean hasListenedMusic (User u, Music m) {
        return lrDAO.hasListened(u, m.getId());
    }

    public int getNumListened (User u) {
        return  lrDAO.getNumListened(u);
    }

}
