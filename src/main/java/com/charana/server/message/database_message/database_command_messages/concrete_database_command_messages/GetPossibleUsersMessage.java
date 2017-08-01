package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.DisplayName;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;

import java.util.UUID;

/**
 * Created by Charana on 7/28/17.
 */
public class GetPossibleUsersMessage extends DatabaseCommandMessage {
    public final DisplayName displayName;

    public GetPossibleUsersMessage(UUID clientID, DisplayName displayName) {
        super(clientID, DatabaseCommandType.GET_POSSIBLE_USERS);
        this.displayName = displayName;
    }
}
