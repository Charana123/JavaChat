package com.charana.server.message.database_message.database_command_messages.concrete_database_command_messages;

import com.charana.server.message.database_message.database_command_messages.DatabaseCommandMessage;
import com.charana.server.message.database_message.database_command_messages.DatabaseCommandType;
import java.util.UUID;

public class GetPossibleUserMessage extends DatabaseCommandMessage {
    public final String sourceEmail;
    public final String possibleUser;

    public GetPossibleUserMessage(UUID clientID, String sourceEmail, String possibleUser) {
        super(clientID, DatabaseCommandType.GET_POSSIBLE_USER);
        this.sourceEmail = sourceEmail;
        this.possibleUser = possibleUser;

    }
}
