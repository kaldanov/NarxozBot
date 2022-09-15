package com.example.spgtu.service;

import com.example.spgtu.dao.entities.custom.*;
import com.example.spgtu.dao.enums.Gender;
import com.example.spgtu.dao.repositories.*;
import com.example.spgtu.util.DateUtil;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Date;
import java.util.List;


public class FileGenerator {

    static FilesMessagesRepo filesMessagesRepo;
    static ButtonRepo buttonRepo;



    public static File statementLossDocs(LossDocs reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Заявление "+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(279));
        run.addBreak();
        run.setText(getText(280));
        run.addBreak();
        run.setText(getText(281));
        run.addBreak();
        run.setText(getText(282));
        run.addBreak();
        //************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(String.format(getText(283), reg.getCourse()));
        run.addBreak();
        run.setText(String.format(getText(284), reg.getEduDirection()));
        run.addBreak();
        run.setText(String.format(getText(285), reg.getEducationForm()));
        run.addBreak();
        run.setText(reg.getFio());
        run.addBreak();
        run.setText(getText(286) + " " + reg.getAddress());
        run.addBreak();
        run.setText(getText(287) + " " + reg.getPhone());

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(getText(288));

        //****************************
        //todo get word

        //420 - credit book
        //421 - badge
        //422  - student card
        String texxxxt;
        switch (reg.getTypeDocs()){
            case STUDENT_CARD: texxxxt = getText(422); break;
            case BADGE:texxxxt = getText(421);break;
            case CREDIT_BOOK: texxxxt = getText(420);break;
            default:texxxxt = "Не дали шаблон заявления!";break;
        }
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(texxxxt);
        run.addBreak();
        String dayText =  "Оплата "+reg.getTypeDocs().getPrice()+" тенге, касса № _______ от «"+DateUtil.getDayDate(reg.getDate()) +"» "+DateUtil.getMonthStrAndYear(reg.getDate())+" г.";
        run.setText(dayText);
        run.addBreak();

        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText("«"+DateUtil.getDayDate(reg.getDate())+"» " + DateUtil.getMonthStrAndYear(reg.getDate()));
        run.addBreak();
        run.setText(getText(290));
        run.addBreak();
        run.setText(getText(291));
        run.addBreak();
        run.setText(getText(292));

        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setFontFamily("Times New Roman");

        run.setText(getText(293));
        run.addBreak();
        //******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(295) + " «____»_______202  г.");
        run.addBreak();

        //****************************

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File statementPeriodLearning(RequestDeductions reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Заявление об обучений"+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(279));
        run.addBreak();
        run.setText(getText(280));
        run.addBreak();
        run.setText(getText(281));
        run.addBreak();
        run.setText(getText(282));
        run.addBreak();
        //************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(String.format(getText(283), reg.getCourse()));
        run.addBreak();
        run.setText(String.format(getText(284), reg.getEduDirection()));
        run.addBreak();
        run.setText(String.format(getText(285), reg.getEducationForm()));
        run.addBreak();
        run.setText(reg.getFio());
        run.addBreak();
        run.setText(getText(286) + " " + reg.getAddress());
        run.addBreak();
        run.setText(getText(287) + " " + reg.getPhone());

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(getText(288));

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(getText(298));
        run.addBreak();;
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText("«"+DateUtil.getDayDate(reg.getDate())+"» " + DateUtil.getMonthStrAndYear(reg.getDate()));
        run.addBreak();
        run.setText(getText(290));
        run.addBreak();
        run.setText(getText(291));
        run.addBreak();
        run.setText(getText(292));

        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setFontFamily("Times New Roman");

        run.setText(getText(293));
        run.addBreak();
        //******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(294));
        run.addBreak();
        run.setText(getText(295));
        run.addBreak();
        run.setText(getText(296));
        run.addBreak();
        run.addBreak();
        run.setText(getText(297));

        //****************************

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File statementMyDocs(RequestDeductions reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Заявление на выдачу личных документов "+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(279));
        run.addBreak();
        run.setText(getText(280));
        run.addBreak();
        run.setText(getText(281));
        run.addBreak();
        run.setText(getText(282));
        run.addBreak();
        //************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(String.format(getText(283), reg.getCourse()));
        run.addBreak();
        run.setText(String.format(getText(284), reg.getEduDirection()));
        run.addBreak();
        run.setText(String.format(getText(285), reg.getEducationForm()));
        run.addBreak();
        run.setText(reg.getFio());
        run.addBreak();
        run.setText(getText(286) + " " + reg.getAddress());
        run.addBreak();
        run.setText(getText(287) + " " + reg.getPhone());

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(getText(288));

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(getText(289));
        run.addBreak();;
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText("«"+DateUtil.getDayDate(reg.getDate())+"» " + DateUtil.getMonthStrAndYear(reg.getDate()));
        run.addBreak();
        run.setText(getText(290));
        run.addBreak();
        run.setText(getText(291));
        run.addBreak();
        run.setText(getText(292));

        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setFontFamily("Times New Roman");

        run.setText(getText(293));
        run.addBreak();
        //******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(294));
        run.addBreak();
        run.setText(getText(295));
        run.addBreak();
        run.setText(getText(296));
        run.addBreak();
        run.addBreak();
        run.setText(getText(297));

        //****************************

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File getContractDorm(DormRegistration reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Договор о проживание"+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(13);

        run.setText(getText(187) + reg.getId());
        run.addBreak();
        run.setText(getText(188));
        run.addBreak();
        //************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setText(String.format(getText(189), DateUtil.getDayDate(reg.getDateStart()), DateUtil.getMonthStrAndYear(reg.getDateStart())));
        run.addBreak();

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setText(getText(190));
        run.addBreak();

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);
        run.setText(reg.getFio() + ", ");
        //*************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setText(getText(191));

        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(192));
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontSize(11);
        run.setFontFamily("Times New Roman");
        run.setText(getText(193));
        run.addBreak();
        run.setText(getText(194));
        //******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(195));

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setItalic(true);

        run.setText(getText(196));

        //******************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(197));
        //******************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText("комната №" + reg.getDorm().getId());

        //******************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(", после оплаты проживания и подписания настоящего договора обеими сторонами.");
        run.addBreak();
        run.setText(getText(198));
        run.addBreak();
        run.setText(getText(199));
        run.addBreak();
        run.setText(getText(200));
        run.addBreak();
//********************************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setItalic(true);

        run.setText(getText(201));
        run.addBreak();

        //**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(202));
        run.addBreak();
        run.setText(getText(203));
        run.addBreak();
        run.setText(getText(204));
        run.addBreak();
        run.setText(getText(205));
        run.addBreak();
        run.setText(getText(206));
        run.addBreak();
        run.setText(getText(207));
        run.addBreak();
        run.setText(getText(208));
        run.addBreak();
        run.setText(getText(209));
        run.addBreak();
        run.setText(getText(210));
        run.addBreak();
        run.setText(getText(211));
        run.addBreak();
        run.setText(getText(212));
        run.addBreak();
        run.setText(getText(213));
        run.addBreak();
        run.setText(getText(214));
        run.addBreak();
        run.setText(getText(215));
        run.addBreak();
        run.setText(getText(216));
        run.addBreak();

        //**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(217));

        //**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(String.format(getText(218), reg.getDateStrPlusOneMonth(), reg.getDateStr(), reg.getDateStrPlusOneMonth()));
        run.addBreak();
        run.setText(getText(219));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(reg.getPrice() + "тенге (за "+reg.getCountMonth()+" месяц)");
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(220));
        run.addBreak();
        run.setText(getText(221));
        run.addBreak();
        run.setText(getText(222));
        run.addBreak();
        run.setText(getText(223));
        run.addBreak();
        run.setText(getText(224));
        run.addBreak();
        run.setText(getText(225));
        run.addBreak();
        run.setText(getText(226));
        run.addBreak();
        run.setText(getText(227));
        run.addBreak();
        run.setText(getText(228));
        run.addBreak();
        run.setText(getText(229));
        run.addBreak();
        run.setText(getText(230));
        run.addBreak();
        run.setText(getText(231));
        run.addBreak();
        run.setText(getText(232));
        run.addBreak();
        run.setText(getText(233));
        run.addBreak();
        run.setText(getText(234));
        run.addBreak();
        run.setText(getText(235));
        run.addBreak();
        run.setText(getText(236));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(237));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(false);

        run.setText(getText(238));
        run.addBreak();
        run.setText(getText(239));
        run.addBreak();
        run.setText(getText(240));
        run.addBreak();
        run.setText(getText(241));
        run.addBreak();
        run.setText(getText(242));
        run.addBreak();
        run.setText(getText(243));
        run.addBreak();
        run.setText(getText(244));
        run.addBreak();
        run.setText(getText(245));
        run.addBreak();
        run.setText(getText(246));
        run.addBreak();
        run.setText(getText(247));
        run.addBreak();
        run.setText(getText(248));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);
        run.setText(getText(249));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(false);

        run.setText(getText(250));
        run.addBreak();
        run.setText(getText(251));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);
        run.setText(getText(252));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(253));
        run.addBreak();
        run.setText(getText(254));
//************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(255));
        run.addBreak();

        //************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(256));
        run.addBreak();
        run.setText(getText(257));
        run.addBreak();
        run.setText(getText(258));
        run.addBreak();
        run.setText(getText(259));
        run.addBreak();
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(260));
//************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);

        run.setText(getText(261));

//************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);
        run.setBold(true);

        run.setText(getText(262));
//************************************
////////////////////////////////////

        // 7. ЮРИДИЧЕСКИЕ АДРЕСА СТОРОН

        XWPFTable table =  document.createTable();
//        table.setTopBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
//        table.setBottomBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
//        table.setLeftBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 0, "ffffff");
//        table.setRightBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
//        table.setInsideHBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
//        table.setInsideVBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");

        XWPFTableRow row1 = table.getRow(0);
        row1.getCell(0).setWidth("6000");
        row1.getCell(0).setParagraph(getUniverPr2());

        XWPFTableCell cell2 = row1.addNewTableCell();
        cell2.setWidth("6000");
        cell2.setParagraph(getStudentInfoPr2(reg));

//////////////////////////////////////////////////////////////////////////////////////////


//**********************************

        document.write(out);
        out.close();

        return new File(fileName);

    }

    private static XWPFParagraph getUniverPr2() {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(11);
        run1.setBold(true);

        run1.setText("Университет");
        run1.addBreak();
        run1.setText(getText(263));
        run1.addBreak();

//***************************************
        run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(11);

        run1.setText(getText(264));
        run1.addBreak();
        run1.setText(getText(265));
        run1.addBreak();
        run1.setText(getText(266));
        run1.addBreak();
        run1.setText(getText(267));
        run1.addBreak();
        run1.setText(getText(268));
        run1.addBreak();
        run1.setText(getText(269 ));
        run1.addBreak();
        run1.setText("КОД ОКПО 280433011000");
        run1.addBreak();
        run1.setText(getText(270 ));
        return paragraph1;
    }

    private static XWPFParagraph getStudentInfoPr2(DormRegistration reg) {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(11);
        run1.setBold(false);

        run1.setText(getText(271));
        run1.addBreak();
        run1.setText( reg.getFio() + ", " + getText(272));
        run1.addBreak();
        run1.setText(getText(273) + " " + reg.getEduDirection());
        run1.addBreak();
        run1.setText(getText(274) + reg.getCourse());
        run1.addBreak();
        run1.setText(getText(275) + reg.getEducationForm());
        run1.addBreak();
        run1.setText(getText(276) );
        run1.addBreak();
        run1.setText("№" + reg.getCardId() + " от "+DateUtil.getDayDate1(reg.getDateIssueCard())+" г.");
        run1.addBreak();
        run1.setText(getText(277)  + reg.getAddress());
        run1.addBreak();
        run1.setText(getText(278)  + reg.getSender().getPhone());

        return paragraph1;
    }

    public static File requestDorm (DormRegistration reg) {
        try {
            filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
            buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
            String fileName = "Заявление(Общежитие)" + reg.getSender().getChatId() + ".docx";
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);


//            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\templates\\PDFs\\СправкаАби1.docx");
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();


            paragraph.setAlignment(ParagraphAlignment.RIGHT );
            paragraph.setSpacingBetween(2.0);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);

//**********************************************


            run.setText(getText(170));
            run.addBreak();
            run.setText(getText(171));
            run.addBreak();
            run.setText(getText(172));
            run.addBreak();
            run.setText(getText(173) + reg.getFio());
            run.addBreak();
            run.setText(getText(174) + reg.getIIN());
            run.addBreak();
            run.setText(getText(175) + reg.getAddress());
            run.addBreak();
            run.setText(getText(176) + reg.getSender().getPhone());
            run.addBreak();

//****************************
            paragraph = document.createParagraph();
            paragraph.setSpacingBetween(2.0);
            paragraph.setAlignment(ParagraphAlignment.CENTER );
            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);

            run.setText(getText(177));
//****************************
            paragraph = document.createParagraph();
            paragraph.setSpacingBetween(2.0);
            paragraph.setAlignment(ParagraphAlignment.LEFT );
            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);

            run.setText(String.format(getText(178),reg.getDorm().getCountPlaces(), reg.getDateStr(), reg.getDateStrPlusOneMonth()));
            run.addBreak();
            run.setText(String.format(getText(179), reg.getId(),reg.getDateStr(), reg.getPrice()));
            run.addBreak();
            run.setText(getText(180));
            run.addBreak();
            run.setText(getText(181));
            run.addBreak();
            run.setText(getText(182) + reg.getFio());
            run.addBreak();
            run.setText(getText(183) + reg.getDorm().getId());
            run.addBreak();
            run.setText(getText(184));
            run.addBreak();
            run.setText(getText(185));
            run.addBreak();
            run.setText(getText(186));

            document.write(out);
            out.close();

            return new File(fileName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static File statementToHandle (PreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
        String fileName = "Заявление на обработку "+reg.getSender().getChatId()+".docx";
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);

        FileInputStream fileInputStream = new FileInputStream("C:/Users/Admin/ШЗ2.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setFirstLineIndent(0);
        paragraph.setIndentationFirstLine(0);
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        paragraph.setSpacingBefore(0);


//**********************************************
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(125));
        run.addBreak();
        run.setText(getText(126));
        run.addBreak();
        run.setText(getText(127));
        run.addBreak();
        run.setText(getText(128) + " " + reg.getFio());
        run.addBreak();
        run.setText(getText(129) + " " + reg.getIin());
        run.addBreak();
        run.setText(getText(130));
        run.addBreak();
        run.setText(getText(131) + " " + reg.getNumberCard());
        run.addBreak();
        run.setText(String.format(getText(132), reg.getCardGive()) + " " + DateUtil.getDayDate1(reg.getDateIssueCard()));

//****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(133));
        run.addBreak();
        run.setText(getText(134));
//****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(getText(135 ) + " " + reg.getFio() + ",");
        run.addBreak();
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);



        run.setText(getText(136));
        run.addBreak();

        run.addTab();
        run.setText(getText(137));
        run.addBreak();
        run.addTab();
        run.setText(getText(138));
        run.addBreak();
        run.addTab();
        run.setText(getText(139));
        run.addBreak();
        run.addTab();
        run.setText(getText(140));
        run.addBreak();
        run.addTab();
        run.setText(getText(141));
        run.addBreak();
        run.addTab();
        run.setText(getText(142));
        run.addBreak();
        run.addTab();
        run.setText(getText(143));
        run.addBreak();
        run.addTab();
        run.setText(getText(144));
        run.addBreak();
        run.addTab();
        run.setText(getText(145));
        run.addBreak();
        run.addTab();
        run.setText(getText(146));
        run.addBreak();
        run.addTab();
        run.setText(getText(147));
        run.addBreak();
        run.addTab();
        run.setText(getText(148));
        run.addBreak();
        run.addTab();
        run.setText(getText(149));
        run.addBreak();
        run.addTab();
        run.setText(getText(150));
        run.addBreak();
        run.addTab();
        run.setText(getText(151));
        run.addBreak();
        run.addTab();
        run.setText(getText(152));
        run.addBreak();
        run.addTab();
        run.setText(getText(153));
        run.addBreak();
        run.addTab();
        run.setText(getText(154));
        run.addBreak();
        run.addTab();
        run.setText(getText(155));
        run.addBreak();
        run.addTab();
        run.setText(getText(156));
        run.addBreak();
        run.addTab();
        run.setText(getText(157));
        run.addBreak(); run.addBreak();

        run.setText(String.format(getText(158), DateUtil.getDayDate1(new Date())));
        run.addBreak();

        run.setText(getText(159));
        run.addBreak();
        run.setText(reg.getFio());
        run.addBreak();
        run.setText(String.format(getText(160), DateUtil.getDayDate(new Date()), DateUtil.getMonthStrAndYear(new Date())));
        run.addBreak();

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File statementToHandleWithoutCourses (WithoutPreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
        String fileName = "Заявление на обработку "+reg.getSender().getChatId()+".docx";
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);

        FileInputStream fileInputStream = new FileInputStream("C:/Users/Admin/ШЗ2.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setFirstLineIndent(0);
        paragraph.setIndentationFirstLine(0);
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        XWPFRun run = paragraph.createRun();
        paragraph.setSpacingBefore(0);


//**********************************************
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(125));
        run.addBreak();
        run.setText(getText(126));
        run.addBreak();
        run.setText(getText(127));
        run.addBreak();
        run.setText(getText(128) + " " + reg.getFio());
        run.addBreak();
        run.setText(getText(129) + " " + reg.getIin());
        run.addBreak();
        run.setText(getText(130));
        run.addBreak();
        run.setText(getText(131) + " " + reg.getNumberCard());
        run.addBreak();
        run.setText(String.format(getText(132), reg.getCardGive()) + " " + DateUtil.getDayDate1(reg.getDateIssueCard()));

//****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(133));
        run.addBreak();
        run.setText(getText(134));
//****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setText(getText(135 ) + " " + reg.getFio() + ",");
        run.addBreak();
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(11);



        run.setText(getText(136));
        run.addBreak();

        run.addTab();
        run.setText(getText(137));
        run.addBreak();
        run.addTab();
        run.setText(getText(138));
        run.addBreak();
        run.addTab();
        run.setText(getText(139));
        run.addBreak();
        run.addTab();
        run.setText(getText(140));
        run.addBreak();
        run.addTab();
        run.setText(getText(141));
        run.addBreak();
        run.addTab();
        run.setText(getText(142));
        run.addBreak();
        run.addTab();
        run.setText(getText(143));
        run.addBreak();
        run.addTab();
        run.setText(getText(144));
        run.addBreak();
        run.addTab();
        run.setText(getText(145));
        run.addBreak();
        run.addTab();
        run.setText(getText(146));
        run.addBreak();
        run.addTab();
        run.setText(getText(147));
        run.addBreak();
        run.addTab();
        run.setText(getText(148));
        run.addBreak();
        run.addTab();
        run.setText(getText(149));
        run.addBreak();
        run.addTab();
        run.setText(getText(150));
        run.addBreak();
        run.addTab();
        run.setText(getText(151));
        run.addBreak();
        run.addTab();
        run.setText(getText(152));
        run.addBreak();
        run.addTab();
        run.setText(getText(153));
        run.addBreak();
        run.addTab();
        run.setText(getText(154));
        run.addBreak();
        run.addTab();
        run.setText(getText(155));
        run.addBreak();
        run.addTab();
        run.setText(getText(156));
        run.addBreak();
        run.addTab();
        run.setText(getText(157));
        run.addBreak(); run.addBreak();

        run.setText(String.format(getText(158), DateUtil.getDayDate1(new Date())));
        run.addBreak();

        run.setText(getText(159));
        run.addBreak();
        run.setText(reg.getFio());
        run.addBreak();
        run.setText(String.format(getText(160), DateUtil.getDayDate(new Date()), DateUtil.getMonthStrAndYear(new Date())));
        run.addBreak();

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File statement (PreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();

        FileInputStream inputStreamFile = new FileInputStream("C:/Users/Admin/ШЗ.docx");
        XWPFDocument document = new XWPFDocument(inputStreamFile);
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();


        String fileName = "Заявление "+reg.getSender().getChatId()+".docx";
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);
//**********************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.RIGHT);
        paragraph.setSpacingBetween(2.0);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(108) + " " + reg.getFio());
        run.addBreak();
        run.setText(getText(109) + " " + reg.getAddress());
        run.addBreak();
        run.setText(String.format(getText(110), reg.getEndDateUniver()) + " " + reg.getUniverNameCityDistrict());


        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(14);

        run.setText(getText(111));

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setSpacingBetween(2.0);

        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(112) + " " + getAnswer(Long.parseLong(reg.getEducationForm())) + " " + getText(113)+" " + reg.getChosenDirection().getDirection().getFaculty() );
        run.addBreak();
        run.setText(String.format(getText(114), reg.getChosenDirection().getDirection().getName(1)));
        run.addBreak();
        run.setText(getText(115));
        run.addBreak();

        run.setText(String.format(getText(116), getGender(reg.getGender(), 1))  + " " + reg.getNationality());
        run.addBreak();
        run.setText(getText(117) + " " + reg.getBirthDay());
        run.addBreak();

        run.setText(getText(118));
        run.addBreak();
        run.setText(getText(119) + " "  +reg.getFathersName());
        run.addBreak();
        run.setText(getText(120) + " "  +reg.getFathersContacts());
        run.addBreak();
        run.setText(getText(121) + " "  +reg.getMothersName());
        run.addBreak();

        run.setText(getText(120) + " "  +reg.getMothersContacts());
        run.addBreak();

        run.setText(String.format(getText(122), DateUtil.getYear(new Date())));
        run.addBreak();

        run.setText(String.format(getText(123),DateUtil.getDayDate(new Date()), DateUtil.getMonthStrAndYear(new Date())));
        run.addBreak();

        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File registerCard(PreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Рег. карта"+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setBold(false);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(91));


        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(14);

        run.setText(getText(92));
        run.addBreak();

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
//        paragraph.setSpacingAfterLines(1000);
        paragraph.setSpacingBetween(2.0);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);
        run.setText(getText(93) + " " + reg.getLastName());
        run.addBreak();
        run.setText(getText(94) + " " + reg.getFirstname());
        run.addBreak();
        run.setText(getText(95) + " " + getGender(reg.getGender(), 1) + " " + getText(96) + " " + reg.getCitizenship());
        run.addBreak();
        run.setText(getText(97) + " " + reg.getBirthYear() + " " + getText(98) + " "+ getAnswer(Long.parseLong(reg.getMaritalStatus())));
        run.addBreak();
        run.setText(getText(99) + " ");//  + reg.getSocialStatus());
        run.addBreak();
        run.setText(String.format(getText(100), reg.getEndDateUniver()) + " "+ reg.getUniverNameCityDistrict());
        run.addBreak();
        run.setText(getText(101) + " "  +reg.getChosenDirection().getDirection().getFaculty());
        run.addBreak();
        run.setText(getText(102) + " "  + getAnswer(Long.parseLong(reg.getEducationForm())));
        run.addBreak();
        run.setText(getText(103) + " "  +reg.getChosenDirection().getDirection().getCode());
        run.addBreak();
        run.setText(getText(104) + " "  +reg.getChosenDirection().getDirection().getName(1));
        run.addBreak();
        run.setText(getText(105) + " "  +reg.getEducation());
        run.addBreak();
        run.setText(getText(106));
        run.addBreak();
        run.setText(String.format(getText(107),  DateUtil.getDayDate(reg.getDate()), DateUtil.getMonthStrAndYear(reg.getDate())));




        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File getContractWithout(WithoutPreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Договор"+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(10);

        run.setText(getText(1));
        run.addBreak();
        run.setText(getText(5));
        run.addBreak();
        run.setText(getText(6));
        run.addBreak();

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setBold(false);
        run.setFontSize(8.5);
        run.setFontFamily("Times New Roman");
        run.setText(String.format(getText(2), DateUtil.getDayDate(reg.getDate()), DateUtil.getMonthStrAndYear(reg.getDate())));
        run.addBreak();
        run.addBreak();


//        paragraph = document.createParagraph();
//        paragraph.setAlignment(ParagraphAlignment.BOTH);
//        run.setFontFamily("Times New Roman");
//        run.setFontSize(12);

        //****************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(3));

        //****************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setBold(false);
        run.setText(getText(4));
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(false);

        run.setText(reg.getFio() + ", " + reg.getBirthDay() + ", " + reg.getIin());
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontSize(8.5);
        run.setBold(false);
        run.setFontFamily("Times New Roman");
        run.setText(getText(7));
        //******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setBold(true);
        run.setText(getText(8));

//        run.addBreak();
        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(299));
        run.addBreak();
        run.setText(getText(300));
        run.addBreak();
        run.setText(getText(301));
        run.addBreak();
        run.setText(getText(302));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(303));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(304));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(305));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(306));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(307));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(308));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(309));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(310));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(311));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(312));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(313));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(314));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(315));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(316));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(317));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(318));
        run.addBreak();
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(319));
//****************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(320));
        run.addBreak();

        run.setText(getText(321));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(322));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(323));
        run.addBreak();

//*******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(324) + reg.getChosenDirection().getDirection().getName(1));
        run.addBreak();
        run.setText(String.format(getText(325), getType(reg.getEducationForm())));
        run.addBreak();
        run.setText(getText(326));
        run.addBreak();
        run.setText(getText(327));
        run.addBreak();

//*******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(328));
        run.addBreak();
//*******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(329));
        run.addBreak();
//*******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(330));
        run.addBreak();

//*******************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(331));
        run.addBreak();
        run.setText(getText(332));
        run.addBreak();
        run.setText(getText(333));
        run.addBreak();
        run.setText(getText(334));
        run.addBreak();
        run.setText(getText(335));
        run.addBreak();
        run.setText(getText(336));
        run.addBreak();
        run.setText(getText(337));
        run.addBreak();
        run.setText(getText(338));
        run.addBreak();
        run.setText(getText(339));
        run.addBreak();
        run.setText(getText(340));
        run.addBreak();
        run.setText(getText(341));
        run.addBreak();
        run.setText(getText(342));
        run.addBreak();
        run.setText(getText(343));
        run.addBreak();
        run.setText(getText(344));
        run.addBreak();
        run.setText(getText(345));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(346));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(347));
        run.addBreak();
        run.setText(getText(348));
        run.addBreak();
        run.setText(getText(349));
        run.addBreak();
        run.setText(getText(350));
        run.addBreak();
        run.setText(getText(351));
        run.addBreak();
        run.setText(getText(352));
        run.addBreak();
        run.setText(getText(353));
        run.addBreak();
        run.setText(getText(354));
        run.addBreak();
        run.setText(getText(355));
        run.addBreak();
        run.setText(getText(356));
        run.addBreak();
        run.setText(getText(357));
        run.addBreak();
        run.setText(getText(358));
        run.addBreak();
        run.setText(getText(359));
        run.addBreak();
        run.setText(getText(360));
        run.addBreak();
        run.setText(getText(361));
        run.addBreak();
        run.setText(getText(362));
        run.addBreak();
        run.setText(getText(363));
        run.addBreak();
        run.setText(getText(364));
        run.addBreak();
        run.setText(getText(365));
        run.addBreak();
        run.setText(getText(366));
        run.addBreak();
        run.setText(getText(367));
        run.addBreak();
        run.setText(getText(368));

//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(369));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(370));
        run.addBreak();
        run.setText(getText(371));
        run.addBreak();
        run.setText(getText(372));
        run.addBreak();
        run.setText(getText(373));
        run.addBreak();
        run.setText(getText(374));
        run.addBreak();
        run.setText(getText(375));
        run.addBreak();
        run.setText(getText(376));
        run.addBreak();
        run.setText(getText(377));
        run.addBreak();
        run.setText(getText(378));
        run.addBreak();
        run.setText(getText(379));
        run.addBreak();
        run.setText(getText(380));
        run.addBreak();
        run.setText(getText(381));
        run.addBreak();
        run.setText(getText(382));
        run.addBreak();
        run.setText(getText(383));
        run.addBreak();
        run.setText(getText(384));
        run.addBreak();
        run.setText(getText(385));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(386));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(387));
        run.addBreak();
        run.setText(getText(388));
        run.addBreak();
        run.setText(getText(389));
        run.addBreak();
        run.setText(getText(390));
        run.addBreak();
        run.setText(getText(391));
        run.addBreak();
        run.setText(getText(392));
        run.addBreak();
        run.setText(getText(393));
        run.addBreak();
        run.setText(getText(394));
        run.addBreak();
        run.setText(getText(395));
        run.addBreak();
        run.setText(getText(396));
        run.addBreak();
        run.setText(getText(397));
        run.addBreak();
        run.setText(getText(398));
        run.addBreak();
        run.setText(getText(399));
        run.addBreak();
        run.setText(getText(400));
        run.addBreak();
        run.setText(getText(401));
        run.addBreak();
        run.setText(getText(402));
        run.addBreak();
        run.setText(getText(403));
        run.addBreak();
        run.setText(getText(404));
        run.addBreak();
        run.setText(getText(405));
        run.addBreak();
        run.setText(getText(406));
        run.addBreak();
        run.setText(getText(407));
        run.addBreak();
        run.setText(getText(408));

//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);

        run.setText(getText(409));
//******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);

        run.setText(getText(410));
        run.addBreak();
        run.setText(getText(419));


////////////////////////////////////

            // 7. ЮРИДИЧЕСКИЕ АДРЕСА СТОРОН

        XWPFTable table =  document.createTable();
        table.setTopBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setBottomBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setLeftBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 0, "ffffff");
        table.setRightBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setInsideHBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");

        XWPFTableRow row1 = table.getRow(0);
        row1.getCell(0).setWidth("6000");
        row1.getCell(0).setParagraph(getUniverPrWithout());

        XWPFTableCell cell2 = row1.addNewTableCell();
        cell2.setWidth("6000");
        cell2.setParagraph(getStudentInfoPrWithout(reg));

//////////////////////////////////////////////////////////////////////////////////////////



//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(5.8);
        run.setBold(true);

        run.setText(getText(80));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(false);

        run.setText(getText(81) + "___________________________________________");
        run.addBreak();
        run.setText(getText(82) + "___________________________________________");
        run.addBreak();
        run.setText(getText(83) + "___________________________________________");
        run.addBreak();
        run.setText(getText(84) + "___________________________________________");
        run.addBreak();
        run.setText(getText(85) + "___________________________________________");
        run.addBreak();
        run.setText(getText(86) + "___________________________________________");

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setBold(true);
        run.setText(getText(416));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(8.5);
        run.setText(getText(417));
        run.addBreak();
        run.setText(getText(418));


        document.write(out);
        out.close();

        return new File(fileName);

    }


    public static File getContract(PreparationCourses reg) throws IOException {
        filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
        XWPFDocument document = new XWPFDocument();
        String fileName = "Договор"+reg.getSender().getChatId()+".docx";
        FileOutputStream out = new FileOutputStream(fileName);

        XWPFParagraph paragraph = document.createParagraph();

        //****************************
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setText(getText(1));
        run.addBreak();
        run.setText(getText(5));
        run.addBreak();
        run.setText(getText(6));
        run.addBreak();

        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setBold(false);
        run.setFontFamily("Times New Roman");
        run.setText(String.format(getText(2), DateUtil.getDayDate(reg.getDate()), DateUtil.getMonthStrAndYear(reg.getDate())));
        run.addBreak();
        run.addBreak();


//        paragraph = document.createParagraph();
//        paragraph.setAlignment(ParagraphAlignment.BOTH);
//        run.setFontFamily("Times New Roman");
//        run.setFontSize(12);

        //****************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");

        run.setBold(true);
        run.setText(getText(3));

        //****************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setBold(false);
        run.setText(getText(4));
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(reg.getFio() + ", " + reg.getBirthDay() + ", " + reg.getIin());
        //***************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(false);
        run.setFontFamily("Times New Roman");
        run.setText(getText(7));
        run.addBreak();
        //******************************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);

        run.setBold(true);
        run.setText(getText(8));
//        run.addBreak();
        //****************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(9));

///////////////////////////////
        List<Subject> subjects = reg.getChosenDirection().getDirection().getRequired();
        subjects.add(reg.getChosenDirection().getChosenSubject());
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        for (int i = 0; i < subjects.size(); i++) {
            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setBold(false);
            run.setText(i+1 + "." + subjects.get(i).getName(getLangId(reg.getSender().getChatId())) + " в объеме ");

            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setBold(true);
            run.setText(getCountHours(reg.getCountHours()));

            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setBold(false);
            run.setText("часов");
            if (i != subjects.size() - 1)
                run.addBreak();
        }
        run .setText(",");
//////////////////////////////////
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        paragraph.removeRun(1);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(String.format(getText(10), getType(reg.getEducationForm())));

        //**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(11));

        //**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(12));

        //**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(13));
        run.addBreak();
        run.setText(getText(14));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText("  «" + reg.getChosenDirection().getChosenSubject().getName(getLangId(reg.getSender().getChatId())) + "»");
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(15));
        run.addBreak();
        run.setText(getText(16));
        run.addBreak();
        run.setText(getText(17));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(18));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(19));
        run.addBreak();
        run.setText(getText(20));
        run.addBreak();
        run.setText(getText(21));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(getText(22));
//**********************************
        paragraph = document.createParagraph();
//        paragraph.setAlignment(ParagraphAlignment.);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(23));
        run.addBreak();
        run.setText(getText(24));
        run.addBreak();
        run.setText(getText(25));
        run.addBreak();
        run.setText(getText(26));
        run.addBreak();
        run.setText(getText(27));
        run.addBreak();
        run.setText(getText(28));
        run.addBreak();
        run.setText(getText(29));
        run.addBreak();
        run.setText(getText(30));
        run.addBreak();

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);
        run.setText(getText(31));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.NUM_TAB);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(32));

//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(33));

//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        if (reg.getCountHours().contains("1 месяц")||reg.getCountHours().contains("1 ай")){
            run.setText(getText(500));
        }
        else  if (reg.getCountHours().contains("2 месяц")||reg.getCountHours().contains("2 ай")){
            run.setText(getText(501));
        }
        else if (reg.getCountHours().contains("3 месяц")||reg.getCountHours().contains("3 ай")){
            run.setText(getText(502));
        }

//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(35));
        run.addBreak();
        run.setText(getText(36));
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(" " + getText(37) + " ");
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(38));
        run.addBreak();

//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        if (reg.getCountHours().contains("1 месяц")||reg.getCountHours().contains("1 ай")){
            run.setText(String.format(getText(39), "40000"));
        }
        else  if (reg.getCountHours().contains("2 месяц")||reg.getCountHours().contains("2 ай")){
            run.setText(String.format(getText(39), "60000"));
        }
        else if (reg.getCountHours().contains("3 месяц")||reg.getCountHours().contains("3 ай")){
            run.setText(String.format(getText(39), "80000"));
        }

        run.addBreak();
        run.setText(getText(40));
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(41));
        run.addBreak();
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(42));
//************************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(43));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(44));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(45));

//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(46));
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(47));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(48));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(49));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(50));

//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(51));
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(52));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(53));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(54));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(55));

//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(56));
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(57));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(58));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(59));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(60));
        run.addBreak();
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(61));
//**********************************
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(62));
        run.addBreak();

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(63));
////////////////////////////////////

            // 7. ЮРИДИЧЕСКИЕ АДРЕСА СТОРОН

        XWPFTable table =  document.createTable();
        table.setTopBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setBottomBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setLeftBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 0, "ffffff");
        table.setRightBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setInsideHBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.DASH_DOT_STROKED , 5000, 20, "ffffff");

        XWPFTableRow row1 = table.getRow(0);
        row1.getCell(0).setWidth("6000");
        row1.getCell(0).setParagraph(getUniverPr());

        XWPFTableCell cell2 = row1.addNewTableCell();
        cell2.setWidth("6000");
        cell2.setParagraph(getStudentInfoPr(reg));

//////////////////////////////////////////////////////////////////////////////////////////


//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(89));
        run.addBreak();
        run.setText(getText(90));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(true);

        run.setText(getText(80));
//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);

        run.setText(getText(81));
        run.addBreak();
        run.setText(getText(82));
        run.addBreak();
        run.setText(getText(83));
        run.addBreak();
        run.setText(getText(84));
        run.addBreak();
        run.setText(getText(85));
        run.addBreak();
        run.setText(getText(86));

//**********************************
        paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(12);
        run.setBold(false);
        run.setText(getText(87));
        run.addBreak();
        run.setText(getText(88));



        document.write(out);
        out.close();

        return new File(fileName);

    }

    public static File referenceApplicant1 (PreparationCourses reg, String days) {
        try {
            filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
            buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
            String fileName = "Справка об обучений" + reg.getSender().getChatId() + ".docx";
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);

            FileInputStream fileInputStream = new FileInputStream("C:/Users/Admin/СправкаАби1.docx");
            XWPFDocument document = new XWPFDocument(fileInputStream);
            XWPFParagraph paragraph = document.createParagraph();


            paragraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun run = paragraph.createRun();


//**********************************************
            run.setFontFamily("Times New Roman");
            run.setFontSize(14);

            run.setText(String.format(getText(161), reg.getFio()));
            run.setText(String.format(getText(162), reg.getChosenDirection().getDirection().getName(1)));
            run.addBreak();
            run.setText(getText(163) + " " + days);
            run.addBreak();
            run.setText(getText(164));
            run.addBreak();
            run.setText(getText(165));
            run.addBreak();
            run.addBreak();
            run.setText(getText(166));
            run.addBreak();
            run.setText(getText(167));
//****************************


            document.write(out);
            out.close();

            return new File(fileName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static File referenceApplicant2 (PreparationCourses reg)  {
        try {


            filesMessagesRepo = TelegramBotRepositoryProvider.getFilesMessagesRepo();
            buttonRepo = TelegramBotRepositoryProvider.getButtonRepo();
            String fileName = "Справка о поступлений " + reg.getSender().getChatId() + ".docx";
            File file = new File(fileName);
            FileOutputStream out = new FileOutputStream(file);

            FileInputStream fileInputStream = new FileInputStream("C:/Users/Admin/СправкаАби1.docx");
            XWPFDocument document = new XWPFDocument(fileInputStream);
            XWPFParagraph paragraph = document.createParagraph();


            paragraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun run = paragraph.createRun();


//**********************************************
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);

            run.setText(String.format(getText(168), reg.getFio()));

            run.setText(String.format(getText(169), reg.getChosenDirection().getDirection().getName(1), getType2(reg.getEducationForm())));
            run.addBreak();
            run.setText(getText(165));
            run.addBreak();
            run.addBreak();
            run.setText(getText(166));
            run.addBreak();
            run.setText(getText(167));
//****************************


            document.write(out);
            out.close();

            return new File(fileName);
        }catch (Exception e){
            return null;
        }

    }

    private static XWPFParagraph getUniverPrWithout() {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(8.5);
        run1.setBold(false);
        run1.setText(getText(64));
        run1.addBreak();
        run1.setText(getText(66));
        run1.addBreak();
        run1.setText(getText(67));
        run1.addBreak();
        run1.setText(getText(68));
        run1.addBreak();
        run1.setText(getText(69));
        run1.addBreak();
        run1.setText(getText(70));
        run1.addBreak();
        run1.setText(getText(71));
        run1.addBreak();
        run1.setText(getText(72));
        run1.addBreak();
        run1.addBreak();

        paragraph1.setAlignment(ParagraphAlignment.CENTER);
        run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(8.5);
        run1.setBold(true);
        run1.setText(getText(411));
        run1.addBreak();
        run1.addBreak();
        run1.addBreak();
        run1.setText(getText(414));
        run1.addBreak();
        run1.setText(getText(415));

        return paragraph1;
    }

    private static XWPFParagraph getUniverPr() {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(12);
        run1.setBold(false);
        run1.setText(getText(64));
        run1.addBreak();
        run1.setText(getText(66));
        run1.addBreak();
        run1.setText(getText(67));
        run1.addBreak();
        run1.setText(getText(68));
        run1.addBreak();
        run1.setText(getText(69));
        run1.addBreak();
        run1.setText(getText(70));
        run1.addBreak();
        run1.setText(getText(71));
        run1.addBreak();
        run1.setText(getText(72));
        return paragraph1;
    }

    private static XWPFParagraph getStudentInfoPr(PreparationCourses reg) {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(12);
        run1.setBold(false);

        run1.setText(getText(65));
        run1.addBreak();
        run1.setText(getText(73) + " " + reg.getFio());
        run1.addBreak();
        run1.setText(getText(74) + " " + reg.getChosenDirection().getDirection().getName(getLangId(reg.getSender().getChatId())));
        run1.addBreak();
        run1.setText(getText(75) + " " + reg.getChosenDirection().getDirection().getFaculty());
        run1.addBreak();
        run1.setText(getText(76) + " " + reg.getAddress());
        run1.addBreak();
        run1.setText(getText(77));
        run1.addBreak();
        run1.setText(getText(78)  + reg.getNumberCard());
        run1.addBreak();
        run1.setText(getText(79) + " " +reg.getCardGive() + " " + DateUtil.getDayDate1(reg.getDateIssueCard()));

        return paragraph1;
    }

    private static XWPFParagraph getStudentInfoPrWithout(WithoutPreparationCourses reg) {
        XWPFDocument document1 = new XWPFDocument();
        XWPFParagraph paragraph1 = document1.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(8.5);
        run1.setBold(false);

        run1.setText(getText(65));
        run1.addBreak();
        run1.setText(getText(73) + " " + reg.getFio());
        run1.addBreak();
        run1.setText(getText(74) + " " + reg.getChosenDirection().getDirection().getName(getLangId(reg.getSender().getChatId())));
        run1.addBreak();
        run1.setText(getText(75) + " " + reg.getChosenDirection().getDirection().getFaculty());
        run1.addBreak();
        run1.setText(getText(76) + " " + reg.getAddress());
        run1.addBreak();
        run1.setText(getText(77));
        run1.addBreak();
        run1.setText(getText(78)  + reg.getNumberCard());
        run1.addBreak();
        run1.setText(getText(79) + " " +reg.getCardGive() + " " + DateUtil.getDayDate1(reg.getDateIssueCard()));
        run1.addBreak();
        run1.addBreak();

        paragraph1.setAlignment(ParagraphAlignment.CENTER);
        run1 = paragraph1.createRun();
        run1.setFontFamily("Times New Roman");
        run1.setFontSize(9);
        run1.setBold(true);
        run1.setText(getText(412) );
        run1.addBreak();
        run1.setText(getText(413) );
        run1.addBreak();
        run1.setText("(Подпись Студента)");

        return paragraph1;
    }

    private static String getType(String educationForm) {
        if (educationForm.equals("7"))
            return "очной";
        return "заочной";
    }

    private static String getType2(String educationForm) {
        if (educationForm.equals("7"))
            return "очную";
        return "заочную";
    }

    private static String getCountHours(String countHours) {
        try {
            String[] arr = countHours.split("часов");
            if (arr.length > 1)
                return arr[0];
            else return "-1";
        }catch (Exception e){
            return "-1";
        }
    }

    private static int getLangId(Long chatID){
        return LanguageService.getLanguage(chatID).getId();
    }

    private static String getText(long i ) {
        try {

            return filesMessagesRepo.findById(i).get().getText();
        }catch (Exception e){}
        return "";
    }

    private static String getMessage(long i , int langId) {
        try {
            MessageRepo messageRepo = TelegramBotRepositoryProvider.getMessageRepo();
            return messageRepo.findByIdAndLangId(i, langId).getName();
//            return filesMessagesRepo.findById(i).get().getText();
        }catch (Exception e){}
        return "";
    }

    private static String getAnswer(long i ) {
        try {
            RegistrCardAnswersRepo answersRepo = TelegramBotRepositoryProvider.getRegistrCardAnswersRepo();
            return answersRepo.findById(i).getRuAnswer();
        }catch (Exception e){}
        return "";
    }

    private static String getGender(Gender gender, int langId) {
        if (gender.equals(Gender.MALE))
            return getMessage(87, langId);
        return getMessage(88, langId);
    }

}
