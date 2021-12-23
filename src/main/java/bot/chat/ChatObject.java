package bot.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatObject {
    String chatId;
    State state;
    String username;
    Integer fromIn;
    Integer toIn;
    Integer somIndex;
   boolean isAdmin;



}
