package com.charana.chat_window.ui.main_view;


import com.charana.server.message.database_message.Account;

public interface ViewSwapperInterface {
    void viewAddContact();
    void loadRecentContact(Account friend);
}
