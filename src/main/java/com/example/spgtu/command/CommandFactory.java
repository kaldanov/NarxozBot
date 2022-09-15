package com.example.spgtu.command;

import com.example.spgtu.command.impl.*;
import com.example.spgtu.exceptions.NotRealizedMethodException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Slf4j
public class CommandFactory {

    private static Map<Integer, Class<? extends Command>> mapCommand;

    public  static Command getCommand(int id) {
        return getFormMap(id).orElseThrow(() -> new NotRealizedMethodException("Not realized for type: " + id));
    }

    private static void addCommand(Class<? extends Command> commandClass) {
        int id = -1;
        try {
            id = getId(commandClass.getName());
        }
        catch (Exception e) { log.warn("Command {} no has id, id set {}", commandClass, id); }
        if (id > 0 && mapCommand.get(id) != null)
            log.warn("ID={} has duplicate command {} - {}", id, commandClass, mapCommand.get(id));
        mapCommand.put(id, commandClass);
    }

    private static int  getId(String commandName) { return Integer.parseInt(commandName.split("_")[0].replaceAll("[^0-9]", "")); }

    private static Optional<Command> getFormMap(int id) {
        if (mapCommand == null) {
            init();
        }
        try {
            return Optional.of(mapCommand.get(id).newInstance());
        }
        catch (Exception e) {
            log.error("Command caput: ", e);
        }
        return Optional.empty();
    }

    private static void  init() {
        mapCommand = new HashMap<>();
        addCommand(id001_Registration.class);
        addCommand(id002_ApplicantPart.class);
        addCommand(id003_RequestPreparationCourses.class);
        addCommand(id004_SummerSemester.class);
        addCommand(id005_RequestWithoutCourses.class);
//        addCommand(id006_Reference.class);
//        addCommand(id007_CertificateOfEdu.class);
//        addCommand(id008_CertificateOfPassage.class);
        addCommand(id009_Dekanat.class);
        addCommand(id010_RequestDeductions.class);
        addCommand(id011_LossDocs.class);
        addCommand(id012_RequestOnay.class);
        addCommand(id013_Booking.class);
        addCommand(id014_Admin.class);
        addCommand(id015_BookingInAdmin.class);
        addCommand(id016_Mailing.class);
        addCommand(id017_EditButtons.class);
        addCommand(id018_EditMessages.class);
        addCommand(id019_EditAdmin.class);
        addCommand(id020_ReferenceApplicant1.class);
        addCommand(id021_ODP.class);
        addCommand(id022_ODPHandleRequest.class);
        addCommand(id023_EditODP.class);
        addCommand(id024_Reports.class);
        addCommand(id025_EditDekanat.class);
        addCommand(id026_EditOnayRole.class);
        addCommand(id027_EditBronRole.class);
        addCommand(id028_EditBuhRole.class);
        addCommand(id029_RolesHandler.class);
        addCommand(id0667_LoginStudent.class);

        printListCommand();
    }

    private static void                 printListCommand() {
        StringBuilder stringBuilder = new StringBuilder();
        new TreeMap<>(mapCommand).forEach((y, x) -> stringBuilder.append(x.getSimpleName()).append("\n"));
        log.info("List command:\n{}", stringBuilder.toString());
    }
}
