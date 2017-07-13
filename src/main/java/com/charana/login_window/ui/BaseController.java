package com.charana.login_window.ui;

import com.charana.login_window.ui.startup.StartUp_Controller;

public abstract class BaseController {
    protected StartUp_Controller startUp_controller;

    public void initStartUpController(StartUp_Controller startUp_controller){
        this.startUp_controller = startUp_controller;
    }
}
