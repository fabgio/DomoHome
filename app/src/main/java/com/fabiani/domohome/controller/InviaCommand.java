package com.fabiani.domohome.controller;

import com.fabiani.domohome.model.Command;
import com.fabiani.domohome.model.GestioneSocketComandi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

class InviaCommand {
     private Scheduler scheduler = Schedulers.newThread();
     private Scheduler.Worker worker = scheduler.createWorker();
     private Command mCmmand;


     InviaCommand(Command command) {
          mCmmand = command;
     }

     void execute() {
          worker.schedule(this::prepareInvia);
          if(mCmmand.getTimeout()>0)
               worker.schedule(() -> {
                    mCmmand.setWhat(0);
                    prepareInvia();
               }, mCmmand.getTimeout(), TimeUnit.SECONDS);
     }

     private void prepareInvia() {
          String openWebNet="*" + mCmmand.getWho() + "*" + mCmmand.getWhat() + "*" + mCmmand.getWhere() + "##";
          GestioneSocketComandi gestioneSocketComandi=new GestioneSocketComandi();
          gestioneSocketComandi.connect(SettingsFragment.sIp, SettingsFragment.PORT, SettingsFragment.sPasswordOpen);
          gestioneSocketComandi.invia(openWebNet);
          gestioneSocketComandi.close();
     }
}

