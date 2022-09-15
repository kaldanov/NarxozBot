package com.example.spgtu.command.impl;

import com.example.spgtu.command.Command;
import com.example.spgtu.dao.entities.standart.Role;
import com.example.spgtu.dao.entities.standart.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class id028_EditBuhRole extends Command {
    User user;

    private int messId;
    private int notRegisteredMessId;
    private int alreadyAdminMessId;


    @Override
    public boolean execute() throws TelegramApiException {
        deleteUpdateMess();
        deleteNotRegisteredMessId();
        deleteAlreadyAdminMessId();
        user = userRepo.findByChatId(chatId);
        if (!isRegistered() || !isAdmin()) {
            sendMessageWithKeyboard(getText(1), 1);
            return EXIT;
        }

        if (hasContact()){
            String phone = update.getMessage().getContact().getPhoneNumber();

            if (phone.charAt(0) == '8') {
                phone = phone.replaceFirst("8", "+7");
            } else if (phone.charAt(0) == '7') {
                phone = phone.replaceFirst("7", "+7");
            }

            return saveDekanat(phone);
        }
        else if (hasMessageText() && isPhoneNumber(updateMessageText)){
            String phone = updateMessageText;

            if (phone.charAt(0) == '8') {
                phone = phone.replaceFirst("8", "+7");
            } else if (phone.charAt(0) == '7') {
                phone = phone.replaceFirst("7", "+7");
            }

            return saveDekanat(phone);

        }
        else if (hasMessageText() && updateMessageText.contains("/del")){
            if( userRepo.findAllByRolesContains(new Role(6, "ROLE_BUH")).size() <= 1){//todo
                return COMEBACK;
            }
            long delAdminId = getDelAdminId(updateMessageText);
            User notAdmin = userRepo.findById(delAdminId);
            if (notAdmin != null){
                notAdmin.doNotBuh();//todo
                userRepo.save(notAdmin);
                editMessage(getListDekanats(), messId );
                return COMEBACK;
            }
        }
        else {
            if (messId == 0){
                messId = sendMessage( getListDekanats());
            }
            return COMEBACK;
        }
        return COMEBACK;
    }
    private void deleteAlreadyAdminMessId() {
        if (alreadyAdminMessId != 0)
            deleteMessage(alreadyAdminMessId);
    }

    private void deleteNotRegisteredMessId() {
        if (notRegisteredMessId != 0)
            deleteMessage(notRegisteredMessId);
    }

    private long getDelAdminId(String updateMessageText) {
        try {
            return Long.parseLong(updateMessageText.substring(4));
        }catch (Exception e){
            return -1;
        }
    }

    private boolean isPhoneNumber(String phone) {

        if (phone.charAt(0) == '8') {
            phone = phone.replaceFirst("8", "+7");
        } else if (phone.charAt(0) == '7') {
            phone = phone.replaceFirst("7", "+7");
        }
        return phone.charAt(0) == '+' && phone.charAt(1) == '7' && phone.substring(2).length() == 10 && isLong(phone.substring(2)) ;
    }


    private boolean saveDekanat(String phone) throws TelegramApiException {
        User newDekanat = userRepo.findByPhone(phone);
        if (newDekanat == null){
            deleteMessage(notRegisteredMessId);
            notRegisteredMessId = sendMessage(66);
            return COMEBACK;
        }
        if(newDekanat.isBuh()){//todo
            alreadyAdminMessId = sendMessage(162);//todo
            return COMEBACK;
        }
        Role roleAdmin = new Role(6, "ROLE_BUH");//todo
        newDekanat.addRole(roleAdmin);
        userRepo.save(newDekanat);

        editMessage( getListDekanats(), messId );
        return COMEBACK;
    }

    private void deleteUpdateMess() {
        deleteMessage(updateMessageId);
    }
    private String getListDekanats() {
        StringBuilder admins= new StringBuilder();
        admins.append(getText(164)).append(next).append(next);//todo
        Role role = new Role(6, "ROLE_BUH");//todo
        List<User> adminList = userRepo.findAllByRolesContains(role);
        for (User admin :adminList){
            try {
                admins.append(admin.getUserName()).append(" ");
                if (adminList.size() > 1)
                    admins.append("❌ /del").append(admin.getId()).append(next);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        admins.append(next).append(next).append(getText(165)); //todo
        adminList.clear();
        return admins.toString();
    }
}

