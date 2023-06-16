package ua.volcaniccupcake.waistline.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SessionManager {
    private SessionType sessionType = null;

    public boolean sessionExists() {
        return sessionType != null;
    }
}
