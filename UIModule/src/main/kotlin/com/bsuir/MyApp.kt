package com.bsuir

import com.bsuir.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(MainView::class, Styles::class){
    override fun start(stage: Stage) {
        super.start(stage)
       //stage.isFullScreen = true
    }
}