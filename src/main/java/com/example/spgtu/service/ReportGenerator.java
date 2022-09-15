package com.example.spgtu.service;

import com.example.spgtu.dao.entities.custom.*;
import com.example.spgtu.dao.enums.Gender;
import com.example.spgtu.dao.enums.Language;
import com.example.spgtu.dao.enums.TypeReference;
import com.example.spgtu.dao.enums.TypeReferenceStudent;
import com.example.spgtu.dao.repositories.*;
import com.example.spgtu.util.Const;
import com.example.spgtu.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportGenerator {
    private Sheet sheets;
    private Sheet sheet;
    private XSSFWorkbook workbook = new XSSFWorkbook();
    private XSSFCellStyle style = workbook.createCellStyle();

    protected PropertiesRepo propertiesRepository = TelegramBotRepositoryProvider.getPropertiesRepo();
    protected MessageRepo messageRepository = TelegramBotRepositoryProvider.getMessageRepo();
    UsersRegistrCardRepo usersRegistrCardRepo = TelegramBotRepositoryProvider.getUsersRegistrCardRepo();
    WithoutPreparationCoursesRepo withoutPreparationCoursesRepo = TelegramBotRepositoryProvider.getWithoutPreparationCoursesRepo();
    RequestApplicantRepo requestApplicantRepo = TelegramBotRepositoryProvider.getRequestApplicantRepo();
    RequestDeductionsRepo requestDeductionsRepo = TelegramBotRepositoryProvider.getRequestDeductionsRepo();
    LossDocsRepo lossDocsRepo = TelegramBotRepositoryProvider.getLossDocsRepo();
    OnayRepository onayRepository = TelegramBotRepositoryProvider.getOnayRepository();
    DormRegistrationRepo dormRegistrationRepo = TelegramBotRepositoryProvider.getDormRegistrationRepo();
    private XSSFCreationHelper  creationHelper  = workbook.getCreationHelper();
    private XSSFCellStyle       hLinkStyle      = workbook.createCellStyle();
    int messId;
    private DefaultAbsSender bot;



    public void sendPreparationCourses(long chatId, DefaultAbsSender bot, Date startDate, Date endDate, int reporting) throws TelegramApiException {
        try {
            messId = reporting;
            this.bot = bot;
            sendPreparationCourses(chatId, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }

    public void sendPreparationCoursesWithout(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            sendPreparationCoursesWithout(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }

    public void sendReference(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            sendReference(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }

    public void lossDocs(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            lossDocs(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }



    public void dorm(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            dorm(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }
    public void onay(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            onay(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }
    private void onay(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<Onay> onays = onayRepository.findAllByDateBetweenOrderById(startDate, endDate);

        if (onays.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;ИИН;Номер телефона;Номер удостоверения личности;" +
                    "Дата выдачи;Дата окончания срока;Кем выдано;Ссылка на удостоверения личности(лицевая сторона);" +
                    "Ссылка на удостоверения личности(обратная сторона);Ссылка на фото 3х4").split(Const.SPLIT)));
            List<List<String>> dorm = onays.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getFullName());
                list.add(x.getIin());
                list.add(x.getPhone());
                list.add(x.getCardId());
                list.add(DateUtil.getDayDate1(x.getDateIssue()));
                list.add(DateUtil.getDayDate1(x.getDateEnd()));
                list.add(x.getIssuedBy());
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getCardUrlFront()));
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getCardUrlBack()));
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getPhotoUrl()));

                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет Карта Онай " + com.example.spgtu.util.DateUtil.getDayDate1(startDate) + " - " + DateUtil.getDayDate1(endDate) ;

            sendFile(chatId, fn);
        }
    }

    private void dorm(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<DormRegistration> dorms = dormRegistrationRepo.findAllByDateStartBetweenOrderById(startDate, endDate);

        if (dorms.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;ИИН;Пол;Адрес;" +
                    "Направление подготовки;Курс;Форма обучения;Номер комнаты;Номер удостоверения личности;" +
                    "Дата удостоверения личности;Кем выдано;Стоимость комнаты;Статус оплаты;Ссылка на документы").split(Const.SPLIT)));
            List<List<String>> dorm = dorms.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDateStart()));
                list.add(x.getFio());
                list.add(x.getIIN());
                list.add(x.getGender() != null? x.getGender().getVal(): null);
                list.add(x.getAddress());
                list.add(x.getEduDirection());
                list.add(x.getCourse());
                list.add(x.getEducationForm());
                list.add(String.valueOf(x.getDorm().getCountPlaces()));
                list.add(x.getCardId());
                list.add(DateUtil.getDayDate1(x.getDateIssueCard()));
                list.add(x.getWhoGiveCard());
                list.add(String.valueOf(x.getPrice()));
                list.add("Ждем ответа от банков насчет интеграции...");
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getStatementFileId()) + "\n" +
                        "https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getContractFileId()) );

                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет Бронирования общежития" + com.example.spgtu.util.DateUtil.getDayDate1(startDate) + " - " + DateUtil.getDayDate1(endDate) ;

            sendFile(chatId, fn);
        }
    }

    public void sendReferenceStudent(long chatId, DefaultAbsSender bot, Date start, Date end, int del) throws TelegramApiException {
        try {
            messId = del;
            this.bot = bot;
            sendReferenceStudent(chatId, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
        }
    }

    private void sendReferenceStudent(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<RequestDeductions> requestDeductions = requestDeductionsRepo.findAllByDateBetween(startDate, endDate);

        if (requestDeductions.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;Курс;Направление подготовки" +
                    ";Форма обучения;Адрес;Телефон;Тип сравки;Ссылка на справку").split(Const.SPLIT)));
            List<List<String>> dorm = requestDeductions.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getFio());
                list.add(x.getCourse());
                list.add(x.getEduDirection());
                list.add(x.getEducationForm());
                list.add(x.getAddress());
                list.add(x.getPhone());
                list.add(getTypeReferenceStudent(x.getTypeReferenceStudent()));
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getFileId()));

                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет по справкам " + com.example.spgtu.util.DateUtil.getDayDate(startDate) + " - " + DateUtil.getDayDate(endDate) ;

            sendFile(chatId, fn);
        }
    }

    private String getTypeReferenceStudent(TypeReferenceStudent typeReferenceStudent) {
        if (typeReferenceStudent.equals(TypeReferenceStudent.TO_GIVE_MY_DOCS))
            return "Заявление на выдачу личных документов";
        return "Заявление на справку о периоде обучения";
    }

    private void lossDocs(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<LossDocs> lossDocs = lossDocsRepo.findAllByDateBetween(startDate, endDate);

        if (lossDocs.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;Курс;Направление подготовки" +
                    ";Форма обучения;Адрес;Телефон;Тип Документа;Стоимость документа; Статус оплаты;Ссылка на документ").split(Const.SPLIT)));
            List<List<String>> dorm = lossDocs.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getFio());
                list.add(x.getCourse());
                list.add(x.getEduDirection());
                list.add(x.getEducationForm());
                list.add(x.getAddress());
                list.add(x.getPhone());
                list.add(x.getTypeDocs().getVal());
                list.add(String.valueOf(x.getTypeDocs().getPrice()));
                list.add("");
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getFileId()));

                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет Утеря документов " + com.example.spgtu.util.DateUtil.getDayDate(startDate) + " - " + DateUtil.getDayDate(endDate) ;

            sendFile(chatId, fn);
        }
    }


    private void sendReference(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<RequestApplicant> requestApplicants = requestApplicantRepo.findAllByDateBetween(startDate, endDate);

        if (requestApplicants.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;Направление подготовки" +
                    ";Форма обучения;Тип сравки;Стаус;Ссылка на справку").split(Const.SPLIT)));
            List<List<String>> dorm = requestApplicants.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getPreparationCourses().getFio());
                list.add(x.getPreparationCourses().getChosenDirection().getDirection().getName(1));
                list.add(getValue(x.getPreparationCourses().getEducationForm()));
                list.add(getTypeReference(x.getTypeReference()));
                list.add(getAccepted(x.getAccepted()));
                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getFileId()));


                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет по справкам" + com.example.spgtu.util.DateUtil.getDayDate(startDate) + "-" + DateUtil.getDayDate(endDate) ;

            sendFile(chatId, fn);
        }
    }

    private void sendPreparationCoursesWithout(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<WithoutPreparationCourses> withoutPreparationCourses = withoutPreparationCoursesRepo.findAllByDateBetween(startDate, endDate);

        if (withoutPreparationCourses.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;Дата рождения;ИИН;Адрес;"+
                    "Номер удост-е лич-и ;Дата удост-е лич-и;Кем выдано;Кол-во часов;Форма обуч-я;Ссылки на документы").split(Const.SPLIT)));
            List<List<String>> dorm = withoutPreparationCourses.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getFio());
                list.add(x.getBirthDay());
                list.add(x.getIin());
                list.add(x.getAddress());
                list.add(x.getNumberCard());
                list.add(com.example.spgtu.util.DateUtil.getDayDate1(x.getDateIssueCard()));
                list.add(x.getCardGive());
                list.add(x.getCountHours());
                list.add(getValue(x.getEducationForm()));

                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getContractFileId()) + "\n" +
                        "https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getStatementToHandleFileId() ));


                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет Поступление без подготовительных курсов " + com.example.spgtu.util.DateUtil.getDayDate(startDate) + " - " + DateUtil.getDayDate(endDate) ;

            sendFile(chatId, fn);
        }
    }


    private void sendPreparationCourses(long chatId, Date startDate, Date endDate) throws TelegramApiException, IOException {

        sheets = workbook.createSheet("Отчет");
        sheet = workbook.getSheetAt(0);

        List<PreparationCourses> preparationCourses = usersRegistrCardRepo.findAllByDateBetween(startDate, endDate);

        if (preparationCourses.size() == 0) {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
            bot.execute(new SendMessage(chatId, "Записи отсутствуют"));
        } else {
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;

            createTitle(styleTitle, rowIndex, Arrays.asList(("№;Дата поступления обращения;ФИО;Дата рождения;ИИН;Пол;Сем-ное пол-ние;Адрес;Учебное зав-ние;Дата" +
                    "окон-ние;Нап-ние под-ки;Предмет;Форма обуч-я;Обр-ние;ФИО и кон-ты папы;ФИО и кон-ты мамы;Кол-во часов;" +
                    "Номер удост-е лич-и ;Дата удост-е лич-и;Кем выдано;Ссылки на документы").split(Const.SPLIT)));
            List<List<String>> dorm = preparationCourses.stream().map(x -> {

                List<String> list = new ArrayList<>();
                list.add(String.valueOf(x.getId()));
                list.add(DateUtil.getDayDate1(x.getDate()));
                list.add(x.getFio());
                list.add(x.getBirthDay());
                list.add(x.getIin());
                list.add(x.getGender().getVal());
                list.add(getValue(x.getMaritalStatus()));
                list.add(x.getAddress());
                list.add(x.getUniverNameCityDistrict());
                list.add(x.getEndDateUniver());
                list.add(x.getChosenDirection().getDirection().getName(1));
                list.add(x.getChosenDirection().getChosenSubject().getName(1));
                list.add(getValue(x.getEducationForm()));
                list.add(x.getEducation());
                list.add(x.getFathersName() + "\n" + x.getFathersContacts());
                list.add(x.getMothersName() + "\n" + x.getMothersContacts());
                list.add(x.getCountHours());
                list.add(x.getNumberCard());
                list.add(com.example.spgtu.util.DateUtil.getDayDate1(x.getDateIssueCard()));
                list.add(x.getCardGive());

                list.add("https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getContractFileId()) + "\n" +
                        "https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getRegistrCardFileId()) + "\n" +
                        "https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getStatementFileId()) + "\n" +
                        "https://api.telegram.org/file/bot" + propertiesRepository.findById(1).getValue() + "/" + uploadFile(x.getStatementToHandleFileId() ));


                return list;
            }).collect(Collectors.toList());

            addInfo(dorm, rowIndex);

            String fn = "Отчет Подготовительные курсы " + com.example.spgtu.util.DateUtil.getDayDate(startDate) + " - " + DateUtil.getDayDate(endDate) ;

            sendFile(chatId, fn);
        }
    }





































    private String getAccepted(Boolean accepted) {
        if (accepted == null)
            return "На рассмотрений";
        else if (accepted)
            return "Принято";
        return "Отклонено";
    }

    private String getTypeReference(TypeReference typeReference) {
        if (typeReference.equals(TypeReference.ABOUT_LEARNING))
            return "Справка об обучении на подготовительных курсах";
        return "Справка о поступлении в АФ СПбГУП";
    }

    private String getValue(String educationForm) {
        RegistrCardAnswersRepo registrCardAnswersRepo = TelegramBotRepositoryProvider.getRegistrCardAnswersRepo();
        RegistrCardAnswers answers = registrCardAnswersRepo.findById(getLong(educationForm));
        if (answers != null)
            return answers.getRuAnswer();
        else return "";
    }

    private long getLong(String educationForm) {
        try {
            return Long.parseLong(educationForm);
        }catch (Exception e){
            return -1;
        }
    }


    protected String getText(int messageIdFromDb) {
        String name = null;
        try {
            name = messageRepository.findByIdAndLangId(messageIdFromDb, 1).getName();//messageRepository.getMessageText(messageIdFromDb, getLanguage().getId());//.orElseThrow(() -> new Exception("Message not found " + messageIdFromDb));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return name;
    }

    private void sendFile(long chatId,String filename) throws IOException, TelegramApiException {
        String fileName = filename + ".xlsx";
        String path = "C:\\" + fileName;
        try (FileOutputStream stream = new FileOutputStream(path)) {
            workbook.write(stream);
        } catch (Exception e) {
            log.error("Can't send File error: ", e);
        }
        sendFile(chatId, bot, fileName, path);

    }

    private void sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws IOException, TelegramApiException {
        bot.execute(new DeleteMessage().setMessageId(messId).setChatId(chatId));
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            deleteMessage(messId, chatId);
            bot.execute(new SendDocument().setChatId(chatId).setDocument(fileName, fileInputStream));
        }
        file.delete();
    }

    private void deleteMessage(int messId ,long chatId) {
        try {
            bot.execute(new DeleteMessage().setChatId(chatId).setMessageId(messId));
        }catch (Exception ignored){}
    }

    private void addInfo(List<List<String>> reports, int rowIndex) {
        int cellIndex;
        for (List<String> report : reports) {
            sheets.createRow(++rowIndex);
            insertToRow(rowIndex, report, style);
//            insertToRowURL(rowIndex, report, hLinkStyle);

        }
        for (int i = 1; i <= 20; i++) {
            sheets.autoSizeColumn(i);
        }
    }

    private void            insertToRowURL(int row, List<String> cellValues, CellStyle cellStyle) {
        for (int cellIndex = 18; cellIndex < 20; cellIndex++) {
            addCellValueLink(row, cellIndex, cellValues.get(cellIndex), cellStyle);
        }
    }
    private void            addCellValueLink(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {
        try {
            XSSFHyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
            link.setAddress(cellValue);
            sheets.getRow(rowIndex).getCell(cellIndex).setHyperlink(link);
            sheets.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
        } catch (Exception e) {
            if (e.getMessage().contains("Address of hyperlink must be a valid URI")) sheets.getRow(rowIndex).getCell(cellIndex).setCellValue(" ");
        }
    }

    private void createTitle(XSSFCellStyle styleTitle, int rowIndex, List<String> title) {
        sheets.createRow(rowIndex);
        insertToRow(rowIndex, title, styleTitle);
    }

    private void insertToRow(int row, List<String> cellValues, CellStyle cellStyle) {
        int cellIndex = 0;
        for (String cellValue : cellValues) {
            addCellValue(row, cellIndex++, cellValue, cellStyle);
        }
    }

    private void addCellValue(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {

        sheets.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));

        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
    }

    private String getString(String nullable) {
        if (nullable == null) return "";
        return nullable;
    }

    private XSSFCellStyle setStyle(XSSFWorkbook workbook, BorderStyle thin, short black, XSSFCellStyle style) {
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(thin);
        style.setBorderBottom(thin);
        style.setBorderRight(thin);
        style.setBorderLeft(thin);
        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);
        style.getFont().setBold(true);
        BorderStyle tittle = BorderStyle.MEDIUM;

        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setColor(black);
        titleFont.setFontHeight(10);

        XSSFCellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setWrapText(true);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setBorderTop(tittle);
        styleTitle.setBorderBottom(tittle);
        styleTitle.setBorderRight(tittle);
        styleTitle.setBorderLeft(tittle);
        styleTitle.setTopBorderColor(black);
        styleTitle.setRightBorderColor(black);
        styleTitle.setBottomBorderColor(black);
        styleTitle.setLeftBorderColor(black);
        styleTitle.setFont(titleFont);
        style.setFillForegroundColor(new XSSFColor(new Color(0, 94, 94)));
        return styleTitle;
    }

    protected Language getLanguage(long chatIdOfUser) {
        if (chatIdOfUser == 0) return Language.ru;
        return LanguageService.getLanguage(chatIdOfUser);
    }

    private String uploadFile(String fileId) {
        if (fileId == null)
            return "";

        Objects.requireNonNull(fileId);
        GetFile getFile = new GetFile().setFileId(fileId);
        try {
            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
            return file.getFilePath();
        } catch (TelegramApiException e) {
            throw new IllegalMonitorStateException();
        }
    }

}
