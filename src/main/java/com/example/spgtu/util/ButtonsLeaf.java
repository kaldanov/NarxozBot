package com.example.spgtu.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ButtonsLeaf {

//    private List<String> allDataButtonList;
    protected List<String> allNamesButtonList;
    protected List<String> allIdsButtonList;
    protected int countButtons = 5;
    protected int indexCurrentButton;
    protected int page = 1;
    protected TypeKeyboard typeKeyboard = TypeKeyboard.INLINE;
    protected int countColumn = 1;
    protected String left = "<<";
    protected String right = ">>";
    protected boolean isHorizonSort = false;
    private boolean isAddNextButtons;
    private List<Long> userChatIdList;

    public ButtonsLeaf(List<String> allNamesButtonList, int countButtons, String left, String right) {
        this.allNamesButtonList = allNamesButtonList;
        if (allNamesButtonList.size() > countButtons) {
            this.countButtons   = countButtons;
        } else {
            this.countButtons   = allNamesButtonList.size();
        }
        this.left               = left;
        this.right              = right;
    }

    public ButtonsLeaf(List<String> allNamesButtonList) {
        this.allNamesButtonList = allNamesButtonList;
        this.countButtons = allNamesButtonList.size();
    }

    public ButtonsLeaf(List<String> allNamesButtonList,List<String> allIdsButtonList,
                       int countButtons, String left, String right) {
        this.allNamesButtonList = allNamesButtonList;
        this.allIdsButtonList = allIdsButtonList;
        this.countButtons = countButtons;
        if (allNamesButtonList.size() > countButtons) {
            this.countButtons   = countButtons;
        } else {
            this.countButtons   = allNamesButtonList.size();
        }
        this.left               = left;
        this.right              = right;
    }

    public ButtonsLeaf(List<String> allNamesButtonList, List<String> filesIdListIds) {
        this.allNamesButtonList = allNamesButtonList;
        this.allIdsButtonList   = filesIdListIds;
        this.countButtons       = filesIdListIds.size();
    }

    public ButtonsLeaf(List<String> allNamesButtonList,List<String> allDataButtonList, int countButtons) {
        this.allNamesButtonList = allNamesButtonList;

        this.countButtons       = allNamesButtonList.size();
        this.allIdsButtonList = allDataButtonList;

        if (allNamesButtonList.size() > countButtons) {
            this.countButtons   = countButtons;
        } else {
            this.countButtons   = allNamesButtonList.size();
        }
    }

    public ButtonsLeaf(List<String> allNamesButtonList,List<String> allDataButtonList, int countButtons, List<Long> userChatIdList) {
        this.allNamesButtonList = allNamesButtonList;
        this.userChatIdList     = userChatIdList;
        this.countButtons       = allNamesButtonList.size();
        this.allIdsButtonList  = allDataButtonList;

        if (allNamesButtonList.size() > countButtons) {
            this.countButtons   = countButtons;
        } else {
            this.countButtons   = allNamesButtonList.size();
        }
    }

    public void setAddNextButtons(boolean addNextButtons) { isAddNextButtons = addNextButtons; }

    public boolean isNext(String updateMessageText) {
        if (updateMessageText.equals(left)) {
            page--;
            if (page < 1) page = countPage();
            return true;
        } else if (updateMessageText.equals(right)) {
            page++;
            if (page > countPage()) page = 1;
            return true;
        }else if (updateMessageText.equals(page+"/"+countPage()))
            return true;
        return false;
    }

    public int countPage() {
        int result = allNamesButtonList.size() / countButtons;
        if (allNamesButtonList.size() % countButtons != 0) result++;
        return result;
    }

    public ReplyKeyboard getListButton() {
        indexCurrentButton                  = (page - 1) * countButtons;
        List<String> currentButtonNames     = new ArrayList<>();
        List<String> callbackDataButtons    = new ArrayList<>();
        try {
            for (int i = 0; i < countButtons; i++) {
                currentButtonNames.add(allNamesButtonList.get(indexCurrentButton));
                callbackDataButtons.add(String.valueOf(indexCurrentButton));
                indexCurrentButton++;
            }
        }
        catch (Exception ignored) {}
        if (typeKeyboard == TypeKeyboard.REPLY) {
            if (countButtons >= allNamesButtonList.size())
                return getReplyKeyboard(currentButtonNames);
            return addButtonLeaf(getReplyKeyboard(currentButtonNames));
        }
        if (countButtons >= allNamesButtonList.size() && !isAddNextButtons)
            return getInlineKeyboard(currentButtonNames, callbackDataButtons);
        return addButtonLeaf(getInlineKeyboard(currentButtonNames, callbackDataButtons));
    }


    public ReplyKeyboard getListButtonWithIds() {
        indexCurrentButton = (page - 1) * countButtons;
        List<String> currentButtonNames = new ArrayList<>();
        List<String> callbackDataButtons = new ArrayList<>();
        try {
            for (int i = 0; i < countButtons; i++) {
                currentButtonNames.add(allNamesButtonList.get(indexCurrentButton));
                callbackDataButtons.add(allIdsButtonList.get(indexCurrentButton));
                indexCurrentButton++;
            }
        }catch (Exception e) {}
        if (typeKeyboard == TypeKeyboard.REPLY) {
            if (countButtons >= allNamesButtonList.size()) {
                return getReplyKeyboard(currentButtonNames);
            }
            return addButtonLeaf(getReplyKeyboard(currentButtonNames));
        }
        if (countButtons >= allNamesButtonList.size()) {
            return getInlineKeyboard(currentButtonNames, callbackDataButtons);
        }
        return addButtonLeaf(getInlineKeyboard(currentButtonNames, callbackDataButtons));
    }

    public ReplyKeyboardMarkup addButtonLeaf(ReplyKeyboardMarkup keyboardMarkup) {
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardButton leftButton = new KeyboardButton();
        leftButton.setText(left);

        KeyboardButton rightButton = new KeyboardButton();
        rightButton.setText(right);
        keyboardRow.add(leftButton);
        keyboardRow.add(rightButton);
        keyboardMarkup.getKeyboard().add(keyboardRow);
        return keyboardMarkup;
    }

    private InlineKeyboardMarkup addButtonLeaf(InlineKeyboardMarkup inlineKeyboardMarkup) {
        List<InlineKeyboardButton> rowButton    = new ArrayList<>();

        InlineKeyboardButton leftBtn            = new InlineKeyboardButton();
        leftBtn.setText(left);
        leftBtn.setCallbackData(left);


        InlineKeyboardButton currentPage           = new InlineKeyboardButton();
        currentPage.setText(page+"/"+countPage());
        currentPage.setCallbackData(page+"/"+countPage());


        InlineKeyboardButton rightBtn           = new InlineKeyboardButton();
        rightBtn.setText(right);
        rightBtn.setCallbackData(right);


        rowButton.add(leftBtn);
        rowButton.add(currentPage);
        rowButton.add(rightBtn);
        inlineKeyboardMarkup.getKeyboard().add(rowButton);
        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getInlineKeyboard(List<String> namesButton, List<String> callbackMessage) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsKeyboard;
        if (!isHorizonSort) {
            rowsKeyboard = getVerticalSortedLists(namesButton, callbackMessage);
        } else {
            rowsKeyboard = getHorizonSortedLists(namesButton, callbackMessage);
        }
        keyboard.setKeyboard(rowsKeyboard);
        return keyboard;
    }
    public InlineKeyboardMarkup getInlineKeyboard2(List<String> namesButton, List<String> callbackMessage, List<Long> userChatIdList) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsKeyboard;
        if (!isHorizonSort) {
            rowsKeyboard = getVerticalSortedLists2(namesButton, callbackMessage, userChatIdList);
        } else {
            rowsKeyboard = getHorizonSortedLists2(namesButton, callbackMessage, userChatIdList);
        }
        keyboard.setKeyboard(rowsKeyboard);
        return keyboard;
    }

    public ReplyKeyboardMarkup getReplyKeyboard(List<String> namesButton) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        String buttonIdsString;
        for (int i = 0; i < namesButton.size(); i++) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (int j = 0; j < countColumn; j++) {
                buttonIdsString = namesButton.get(i);
                KeyboardButton button = new KeyboardButton();
                button.setText(buttonIdsString);
                keyboardRow.add(button);
                if (countColumn > 1 && j != countColumn - 1) {
                    i++;
                }
            }
            keyboardRowList.add(keyboardRow);
        }
        keyboard.setKeyboard(keyboardRowList);
        return keyboard;
    }

    protected List<List<InlineKeyboardButton>> getVerticalSortedLists(List<String> namesButton, List<String> callbackMessage) {
        List<List<InlineKeyboardButton>> rowsKeyboard = new ArrayList<>();
        String buttonIdsString;
        int buttons = namesButton.size();
        int countButtonsInColumn = buttons / countColumn;
        if (buttons % countColumn != 0) {
            countButtonsInColumn++;
        }
        int counter = 0;
        for (int j = 0; j < countColumn; j++) {
            for (int i = 0; i < countButtonsInColumn; i++) {
                buttonIdsString = namesButton.get(counter);
                if (j == 0) {
                    List<InlineKeyboardButton> rowButton = new ArrayList<>();
                    InlineKeyboardButton button = getIButton(buttonIdsString, callbackMessage.get(counter));
                    rowButton.add(button);
                    rowsKeyboard.add(rowButton);
                } else {
                    InlineKeyboardButton button = getIButton(buttonIdsString, callbackMessage.get(counter));
                    rowsKeyboard.get(i).add(button);
                }
                counter++;
                if (counter == buttons) {
                    break;
                }
            }
            if (counter == buttons) {
                break;
            }
        }
        return rowsKeyboard;
    }
    protected List<List<InlineKeyboardButton>> getVerticalSortedLists2(List<String> namesButton, List<String> callbackMessage, List<Long> userChatIdList) {
        List<List<InlineKeyboardButton>> rowsKeyboard = new ArrayList<>();
        String buttonIdsString;
        int buttons = namesButton.size();
        int countButtonsInColumn = buttons / countColumn;
        if (buttons % countColumn != 0) {
            countButtonsInColumn++;
        }
        int counter = 0;
        for (int j = 0; j < countColumn; j++) {
            for (int i = 0; i < countButtonsInColumn; i++) {
                buttonIdsString = namesButton.get(counter);
                if (j == 0) {
                    List<InlineKeyboardButton> rowButton = new ArrayList<>();
                    InlineKeyboardButton button = getIButton2(buttonIdsString, callbackMessage.get(counter), String.valueOf(userChatIdList.get(counter)));
                    rowButton.add(button);
                    rowsKeyboard.add(rowButton);
                } else {
                    InlineKeyboardButton button = getIButton2(buttonIdsString, callbackMessage.get(counter), String.valueOf(userChatIdList.get(counter)));
                    rowsKeyboard.get(i).add(button);
                }
                counter++;
                if (counter == buttons) {
                    break;
                }
            }
            if (counter == buttons) {
                break;
            }
        }
        return rowsKeyboard;
    }

    protected List<List<InlineKeyboardButton>> getHorizonSortedLists(List<String> namesButton, List<String> callbackMessage) {
        List<List<InlineKeyboardButton>> rowsKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowButton = new ArrayList<>();
        for (int i = 0; i < namesButton.size(); i++) {
            rowButton.add(getIButton(namesButton.get(i), callbackMessage.get(i)));
            if (rowButton.size() >= countColumn || namesButton.size() == i + 1) {
                rowsKeyboard.add(rowButton);
                rowButton = new ArrayList<>();
            }
        }
        return rowsKeyboard;
    }
    protected List<List<InlineKeyboardButton>> getHorizonSortedLists2(List<String> namesButton,
                                                                      List<String> callbackMessage,
                                                                      List<Long> userChatIdList) {
        List<List<InlineKeyboardButton>> rowsKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowButton = new ArrayList<>();
        for (int i = 0; i < namesButton.size(); i++) {
            rowButton.add(getIButton2(namesButton.get(i), callbackMessage.get(i), String.valueOf(userChatIdList.get(i))));
            if (rowButton.size() >= countColumn || namesButton.size() == i + 1) {
                rowsKeyboard.add(rowButton);
                rowButton = new ArrayList<>();
            }
        }
        return rowsKeyboard;
    }

    protected InlineKeyboardButton getIButton(String text, String callBack) {
        return new InlineKeyboardButton().setText(text).setCallbackData(callBack);
    }
    protected InlineKeyboardButton getIButton2(String text, String callBack, String users) {
        return new InlineKeyboardButton().setText(text).setCallbackData(callBack);
    }
    public enum TypeKeyboard {
        INLINE, REPLY
    }
}


