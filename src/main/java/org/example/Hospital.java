package org.example;

import org.example.enums.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Hospital {
    public static final LocalTime[] standardShift = {LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,0)};
    public static final LocalTime[] earlyShift = {LocalTime.of(7,0), LocalTime.of(12,30), LocalTime.of(14,0)};
    public static final LocalTime[] longBreakShift = {LocalTime.of(8,0), LocalTime.of(12,0), LocalTime.of(13,30)};
    public static Map<String, DoctorInfo> MapDoctors = new LinkedHashMap<>();//(Map.of("Мешкова Катерина Сергієвна",new doctorInfo("1111", standardShift, Specialization.TRAUMATOLOGIST),"Шпичка Надія Миколаївна",new doctorInfo("2222",earlyShift, Specialization.SURGEON),"Іванова Світлана Богданівна",new doctorInfo("3333",longBreakShift, Specialization.OCULIST)));
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args){
        Manager m = new Manager();
        m.loadData("databased.txt");

        Hospital h = new Hospital();
        h.displayInfo();

        m.saveData(MapDoctors, "databased.txt");
    }

    public void displayInfo(){
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime timeClosed = LocalTime.of(23,0);

        if(now.isAfter(timeClosed)){
            System.out.println("Лікарння закрита!");
            return;
        }

        while (true){
            System.out.println("Вітаємо в лікарні\nВведіть цифру яка підтверджує вашу особі?\n0)Якщо хочете закінчити роботу\n1)Лікар\n2)Пацієнт\n3)Менеджер");
            try{
                byte choice = Byte.parseByte(scan.nextLine());
                if(choice == UserType.EXIT.ordinal()){
                    return;
                }else if(choice == UserType.DOCTOR.ordinal()){
                    displayInfoDoctor();
                } else if (choice == UserType.PATIENT.ordinal()){
                    displayInfoPatient();
                } else if (choice == UserType.MANAGER.ordinal()){
                    displayInfoManager();
                } else {
                    System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
                }
            }catch (Exception e){
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }
    }

    public void displayInfoDoctor(){
        while (true){
            System.out.print("Увійдіть в систему\nВведіть ПІБ: ");
            String name = scan.nextLine();
            System.out.print("Введіть пароль: ");
            String password = scan.nextLine();

            Manager m = new Manager();
            if(m.verification(name, password)){
                displayTreatmentGuide(name);
                break;
            }
            else {
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }
    }
    public void displayTreatmentGuide(String name){
        Doctor doctor = new Doctor(name);
        System.out.println("Лікар може тільки лікувати");
        while (true){
            if(!doctor.startShift()){
                System.out.println("У мене перерва");
                break;
            }
            System.out.println("Щоб вилікувати пацієнта потрібно нажати Enter");
            scan.nextLine();
            if(MapDoctors.get(name).getPatients().isEmpty()){
                System.out.println("Пацієнти вилікувані");
                break;
            }else{
                System.out.println("Вилікувано: " + doctor.healsPatient());
            }
        }
    }

    public void displayInfoPatient() {
        System.out.print("Пацієнт може тільки реєструватися\nВведіть ваше ім'я: ");
        String patientName = scan.nextLine();
        System.out.println("Ввиберіть цифру яка підтверджує шо у вас турбує?\n0)Якщо бажаєте вийти\n1)Болить рука\n2)Відкрита рана\n3)Проблеми з очима");

        Specialization needSpecialization = null;
        while (true) {
            try {
                byte choice = Byte.parseByte(scan.nextLine());
                if(choice == PatientChoice.EXIT.ordinal()){
                    return;
                }else if (choice == PatientChoice.TREAT_IN_TRAUMATOLOGIST.ordinal()) {
                    needSpecialization = Specialization.TRAUMATOLOGIST;
                    break;
                } else if (choice == PatientChoice.TREAT_IN_SURGEON.ordinal()) {
                    needSpecialization = Specialization.SURGEON;
                    break;
                } else if (choice == PatientChoice.TREAT_IN_OCULIST.ordinal()) {
                    needSpecialization = Specialization.OCULIST;
                    break;
                } else {
                    System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
                }
            } catch (Exception e) {
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }

        Patient patient = new Patient(patientName);
        String doctor = patient.signUpForQueue(needSpecialization);

        if (doctor != null) {
            System.out.println("Ви записалися до: "+ doctor);
        } else {
            System.out.println("Не знайшлося лікаря");
        }
    }

    private LocalTime[] choiceSchedule() {
        System.out.println("Введіть цифру яка показує який графік ви собі хочете?\n1) Звичайний(початок о 8:00, перерва 12:00-13:00)\n2) Ранній(початок о 7:00, перерва о 12:30-14:00)\n3) Довга перерва(початок о 8:00, перерва о 12:00-13:30)");
        while (true) {
            try {
                byte choice = Byte.parseByte(scan.nextLine());
                if (choice == ChoiceSchedule.CHOICE_STANDARD_SHIFT.getValue()){
                    return standardShift;
                }else if (choice == ChoiceSchedule.CHOICE_EARLY_SHIFT.getValue()){
                    return earlyShift;
                }else if (choice == ChoiceSchedule.CHOICE_LONG_BREAK_SHIFT.getValue()){
                    return longBreakShift;
                }else{
                    System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
                }
            } catch (Exception e){
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }
    }

    private Specialization choiceSpecialization() {
        System.out.println("Введіть цифру яка підтверджує ким ви хочете стати?\n1)Травматолог\n2)Хірург\n3)Окуліст");
        while (true) {
            try {
                byte choice = Byte.parseByte(scan.nextLine());
                if (choice == ChoiceSpecialization.CHOICE_TRAUMATOLOGIST.getValue()){
                    return Specialization.TRAUMATOLOGIST;
                }else if (choice == ChoiceSpecialization.CHOICE_SURGEON.getValue()){
                    return Specialization.SURGEON;
                }else if (choice == ChoiceSpecialization.CHOICE_OCULIST.getValue()){
                    return Specialization.OCULIST;
                }else{
                    System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
                }
            } catch (Exception e){
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }
    }

    public void displayInfoManager() {
        Manager mng = new Manager();

        while (true) {
            System.out.println("Кабінет менеджера\nВведіть цифру для вибору функції лікаря?\n0)Якщо бажаєте вийти\n1)Додати лікаря\n2)Вивести список лікарів\n3)Оновити дані про лікаря\n4)Видалити лікаря\nВведіть цифру для вибору функції пацієнта?\n5)Записати до лікаря\n6)Знайти пацієнта\n7)Перевести пацієнта до іншого лікаря\n8)Видалити пацієнта");
            try {
                byte choice = Byte.parseByte(scan.nextLine());
                FunctionsManager action = FunctionsManager.values()[choice];
                switch (action) {
                    case EXIT:
                        return;
                    case CREATE_DOCTOR:
                        System.out.print("ПІБ нового лікаря: ");
                        String newDoc = scan.nextLine();
                        System.out.print("Пароль: ");
                        String newPass = scan.nextLine();
                        if(mng.createDoctor(newDoc, newPass, choiceSchedule(), choiceSpecialization())){
                            System.out.println("Лікаря додано");
                        }else{
                            System.out.println("Лікар вже існує");
                        }
                        continue;
                    case READ_DOCTOR:
                        System.out.println(mng.readAllDoctors());
                        continue;
                    case UPDATE_DOCTOR:
                        System.out.print("ПІБ лікаря для оновлення: ");
                        String upDoc = scan.nextLine();
                        System.out.print("Новий пароль: ");
                        String upPass = scan.nextLine();
                        if(mng.updateDoctor(upDoc, upPass, choiceSchedule(), choiceSpecialization())){
                            System.out.println("Лікаря оновлено");
                        } else{
                            System.out.println("Лікаря не знайдено");
                        }
                        continue;
                    case DELETE_DOCTOR:
                        System.out.print("ПІБ лікаря для видалення: ");
                        if(mng.deleteDoctor(scan.nextLine())) {
                            System.out.println("Лікаря видалено");
                        }
                        else {
                            System.out.println("Лікаря не знайдено");
                        }
                        continue;
                    case CREATE_PATIENT:
                        System.out.print("Ім'я пацієнта: ");
                        String cPat = scan.nextLine();
                        System.out.print("До якого лікаря (ПІБ): ");
                        String cDoc = scan.nextLine();
                        if(mng.createPatient(cPat, cDoc)){
                            System.out.println("Пацієнта створено");
                        }else{
                            System.out.println("Лікаря не знайдено");
                        }
                        continue;
                    case READ_PATIENT:
                        System.out.print("Ім'я пацієнта: ");
                        System.out.println(mng.readPatient(scan.nextLine()));
                        continue;
                    case UPDATE_PATIENT:
                        System.out.print("Ім'я пацієнта: ");
                        String uPat = scan.nextLine();
                        System.out.print("Новий лікар (ПІБ): ");
                        String uDoc = scan.nextLine();
                        if(mng.updatePatient(uPat, uDoc)){
                            System.out.println("Пацієнта переведено");
                        }else{
                            System.out.println("Пацієнта або нового лікаря не знайдено.");
                        }
                        continue;
                    case DELETE_PATIENT:
                        System.out.print("Ім'я пацієнта: ");
                        String dPat = scan.nextLine();
                        System.out.print("Від якого лікаря видаляємо: ");
                        String dDoc = scan.nextLine();
                        if(mng.deletePatient(dPat, dDoc)){
                            System.out.println("Пацієнта видалено");
                        }else{
                            System.out.println("Пацієнта не знайдено");
                        }
                        continue;
                    default:
                        System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
                }
            } catch (Exception e){
                System.out.println("Ви ввели незрозумілий символ, попробуйте ще раз, але уважніше");
            }
        }
    }
}

