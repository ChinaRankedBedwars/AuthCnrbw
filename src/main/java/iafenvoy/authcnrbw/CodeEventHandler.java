package iafenvoy.authcnrbw;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class CodeEventHandler {
    public static final HashMap<String, Code> codes = new HashMap<>();

    private static String generateShortUuid() {
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 6; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    @Subscribe(order = PostOrder.EARLY)
    public void onLogin(LoginEvent event, Continuation continuation) {
        String code = generateShortUuid();
        codes.put(event.getPlayer().getUsername(), new Code(code));
        event.getPlayer().disconnect(Component.empty()
                .append(Component.text("你的CNRBW注册验证码为： ", NamedTextColor.WHITE))
                .append(Component.text(code, NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(Component.text("验证码有效期为15分钟", NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.text("请到注册频道使用 =r <ign> <验证码> 注册", NamedTextColor.WHITE))
                .append(Component.newline())
                .append(Component.text("请不要泄露你的验证码！", NamedTextColor.RED))
                .append(Component.newline())
                .append(Component.text("有任何疑问请到Kook开票处理", NamedTextColor.AQUA))
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text("Timestamp: " + new Date().getTime() + " | Username: " + event.getPlayer().getUsername() + " | Server: kook.rbwcn.cn", NamedTextColor.GRAY))
        );
    }

    public static class Code {
        public String code;
        public Date time;

        public Code(String code) {
            this.code = code;
            this.time = new Date();
        }
    }
}
