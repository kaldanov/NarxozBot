package com.example.spgtu.dao.repositories;

import com.example.spgtu.dao.entities.standart.Message;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotRepositoryProvider {

    @Getter
    @Setter
    private static RegistrCardAnswersRepo registrCardAnswersRepo;

    @Getter
    @Setter
    private static WithoutPreparationCoursesRepo withoutPreparationCoursesRepo;

    @Getter
    @Setter
    private static RegistrCardQuestsRepo registrCardQuestsRepo;

    @Getter
    @Setter
    private static RequestApplicantRepo requestApplicantRepo;

    @Getter
    @Setter
    private static SubjectRepo subjectRepo;

    @Getter
    @Setter
    private static ButtonRepo buttonRepo;

    @Getter
    @Setter
    private static KeyboardMarkUpRepo keyboardMarkUpRepo;

    @Getter
    @Setter
    private static LanguageUserRepo languageUserRepo;

    @Getter
    @Setter
    private static MessageRepo messageRepo;

    @Getter
    @Setter
    private static PropertiesRepo propertiesRepo;

    @Getter
    @Setter
    private static UserRepo userRepo;

    @Getter
    @Setter
    private static UsersRegistrCardRepo usersRegistrCardRepo;

    @Getter
    @Setter
    private static DormRegistrationRepo dormRegistrationRepo;

    @Getter
    @Setter
    private static DormRepo dormRepo;

    @Getter
    @Setter
    private static ContractRepo contractRepo;

    @Getter
    @Setter
    private static RequestDeductionsRepo requestDeductionsRepo;

    @Getter
    @Setter
    private static DirectionRepo directionRepo;

    @Getter
    @Setter
    private static FilesMessagesRepo filesMessagesRepo;

    @Getter
    @Setter
    private static ChosenDirectionRepo chosenDirectionRepo;

    @Getter
    @Setter
    private static OnayRepository onayRepository;

    @Getter
    @Setter
    private static LossDocsRepo lossDocsRepo;

    @Autowired
    public TelegramBotRepositoryProvider(DormRepo dormRepo,
                                         DormRegistrationRepo dormRegistrationRepo,
                                         ContractRepo contractRepo,
                                         UsersRegistrCardRepo usersRegistrCardRepo,
                                         RegistrCardAnswersRepo registrCardAnswersRepo,
                                         RegistrCardQuestsRepo registrCardQuestsRepo,
                                         UserRepo userRepo,
                                         PropertiesRepo propertiesRepo,
                                         MessageRepo messageRepo,
                                         LanguageUserRepo languageUserRepo,
                                         KeyboardMarkUpRepo keyboardMarkUpRepo,
                                         ButtonRepo buttonRepo,
                                         SubjectRepo subjectRepo,DirectionRepo directionRepo,
                                         FilesMessagesRepo filesMessagesRepo,ChosenDirectionRepo chosenDirectionRepo,
                                         RequestApplicantRepo requestApplicantRepo,OnayRepository onayRepository,
                                         WithoutPreparationCoursesRepo withoutPreparationCoursesRepo,
                                         RequestDeductionsRepo requestDeductionsRepo,LossDocsRepo lossDocsRepo
    ){
        setDormRepo(dormRepo);
        setDormRegistrationRepo(dormRegistrationRepo);
        setContractRepo(contractRepo);
        setUsersRegistrCardRepo(usersRegistrCardRepo);
        setRegistrCardAnswersRepo(registrCardAnswersRepo);
        setRegistrCardQuestsRepo(registrCardQuestsRepo);
        setUserRepo(userRepo);
        setPropertiesRepo(propertiesRepo);
        setMessageRepo(messageRepo);
        setLanguageUserRepo(languageUserRepo);
        setKeyboardMarkUpRepo(keyboardMarkUpRepo);
        setButtonRepo(buttonRepo);
        setSubjectRepo(subjectRepo);
        setDirectionRepo(directionRepo);
        setFilesMessagesRepo(filesMessagesRepo);
        setChosenDirectionRepo(chosenDirectionRepo);
        setRequestApplicantRepo(requestApplicantRepo);
        setOnayRepository(onayRepository);
        setWithoutPreparationCoursesRepo(withoutPreparationCoursesRepo);
        setRequestDeductionsRepo(requestDeductionsRepo);
        setLossDocsRepo(lossDocsRepo);
    }
}
