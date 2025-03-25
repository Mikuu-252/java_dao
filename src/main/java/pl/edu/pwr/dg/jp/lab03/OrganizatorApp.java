package pl.edu.pwr.dg.jp.lab03;


import pl.edu.pwr.dg.jp.lab03.data.*;

import java.util.Objects;
import java.util.Scanner;

public class OrganizatorApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dao<Organizer> organizerDao = new OrganizerDao();

        System.out.println("Witaj w oknie Organizatora");
        System.out.println("0. Dodaj nowego");

        for (Organizer organizer : organizerDao.getAll()) {
            System.out.println("Organizer: [Id : "+organizer.getId()+", Name : "+organizer.getName()+"]");
        }

        System.out.println("Wybierz swoje konto z listy (id): ");
        int organizerID = scanner.nextInt();
        Organizer loginOrganizer = null;
        if (organizerID != 0){loginOrganizer = organizerDao.get(organizerID);}
        else { newOrganizer(scanner, organizerDao); }


        while(loginOrganizer != null) {
            System.out.println("Witaj " + loginOrganizer.getName());
            printMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: showOfferst(); break;
                case 2: newOffert(scanner); break;
                case 3: takeVoucher(scanner, loginOrganizer); break;
                case 4: updateVoucher(scanner, loginOrganizer); break;
                case 5: showVouchersToRealized(); break;
                case 6: realizeVoucher(scanner, loginOrganizer); break;
                case 7: loginOrganizer = changeName(scanner, organizerDao, loginOrganizer); break;
                case 8: organizerDao.delete(organizerDao.get(loginOrganizer.getId())); loginOrganizer = null; break;
                case 9: loginOrganizer = null; break;
                default:  System.out.println("Zła opcja wybierz ponownie"); break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("Wybierz opcje z menu: ");
        System.out.println("1. Przeglądaj oferty");
        System.out.println("2. Dodaj oferty");
        System.out.println("3. Przejmij voucher");
        System.out.println("4. Uzupełnij voucher");
        System.out.println("5. Przeglądaj vouchery do zrealizowania");
        System.out.println("6. Zrealizuj voucher");
        System.out.println("7. Zmień imie");
        System.out.println("8. Usun konto");
        System.out.println("9. Wyjdź");
    }

    private static Organizer changeName(Scanner scanner, Dao<Organizer> organizerDao, Organizer loginOrganizer) {
        scanner.nextLine();
        System.out.println("Podaj nową nazwę (0 aby anulować): ");
        String newName = scanner.nextLine();
        if (Objects.equals(newName, "0")) return loginOrganizer;

        organizerDao.update(new Organizer(loginOrganizer.getId(), newName));
        loginOrganizer.setName(newName);
        return loginOrganizer;
    }

    private static void newOrganizer(Scanner scanner, Dao<Organizer> organizerDao) {
        scanner.nextLine();
        System.out.println("Podaj nazwę (0 aby anulować): ");
        String newOrganizerName = scanner.nextLine();
        if (Objects.equals(newOrganizerName, "0")) return;

        organizerDao.create(new Organizer(0, newOrganizerName));
    }

    private static void showOfferst() {
        Dao<Offer> offerDao = new OfferDao();

        for (Offer offer : offerDao.getAll()) {
            System.out.println("Oferta: [Id : "+offer.getId()+", Parametry : "+offer.getParameters()+"]");
        }
    }

    private static void newOffert(Scanner scanner) {
        Dao<Offer> offerDao = new OfferDao();
        scanner.nextLine();
        System.out.println("Podaj nazwę oferty (0 aby anulować):  ");
        String newOfferName = scanner.nextLine();
        if (Objects.equals(newOfferName, "0")) return;

        offerDao.create(new Offer(0, newOfferName));
    }

    private static void showVouchersToRealized() {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getStatus() == Voucher.Status.DECLARED) {
                System.out.println("Voucher: [" +
                        "Id : "+voucher.getId()+
                        ", Nazwa Organizatora : "+voucher.getOrganizerName()+
                        ", Nazwa Klienta : "+voucher.getClientName()+
                        ", Dane oferty : "+voucher.getOfferParameters()+
                        ", Informacje : "+voucher.getInfo()+
                        ", Status : "+voucher.getStatus()+
                        "]");
            }
        }
    }

    private static void takeVoucher(Scanner scanner, Organizer loginOrganizer) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getStatus() == Voucher.Status.WAIT_FOR_ORGANIZER) {
                System.out.println("Voucher: [" +
                        "Id : "+voucher.getId()+
                        ", Nazwa Organizatora : "+voucher.getOrganizerName()+
                        ", Nazwa Klienta : "+voucher.getClientName()+
                        ", Dane oferty : "+voucher.getOfferParameters()+
                        ", Informacje : "+voucher.getInfo()+
                        ", Status : "+voucher.getStatus()+
                        "]");
            }
        }

        System.out.println("Podaj jaki voucher chcesz przejąć (0 aby anulować): ");
        int voucherId = scanner.nextInt();

        if (voucherId == 0) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucher.setOrganizerName(loginOrganizer.getName());
        voucher.setStatus(Voucher.Status.READY_TO_BUY);

        voucherDao.update(voucher);
    }

    private static void updateVoucher(Scanner scanner, Organizer loginOrganizer) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getOrganizerName().equals(loginOrganizer.getName()) && voucher.getStatus() == Voucher.Status.PURCHASED) {
                System.out.println("Voucher: [" +
                        "Id : "+voucher.getId()+
                        ", Nazwa Organizatora : "+voucher.getOrganizerName()+
                        ", Nazwa Klienta : "+voucher.getClientName()+
                        ", Dane oferty : "+voucher.getOfferParameters()+
                        ", Informacje : "+voucher.getInfo()+
                        ", Status : "+voucher.getStatus()+
                        "]");
            }
        }

        System.out.println("Podaj jaki voucher chcesz uzupełnić (0 aby anulować): ");
        int voucherId = scanner.nextInt();
        if (voucherId == 0) return;

        scanner.nextLine();
        System.out.println("Podaj informacje do uzupełnienia (0 aby anulować): ");
        String info = scanner.nextLine();
        if (Objects.equals(info, "0")) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucher.setInfo(info);
        voucher.setStatus(Voucher.Status.TO_REALIZE);

        voucherDao.update(voucher);
    }

    private static void realizeVoucher(Scanner scanner, Organizer loginOrganizer) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getOrganizerName().equals(loginOrganizer.getName()) && voucher.getStatus() == Voucher.Status.DECLARED) {
                System.out.println("Voucher: [" +
                        "Id: "+voucher.getId()+
                        ", Nazwa Organizatora: "+voucher.getOrganizerName()+
                        ", Nazwa Klienta: "+voucher.getClientName()+
                        ", Dane oferty: "+voucher.getOfferParameters()+
                        ", Informacje: "+voucher.getInfo()+
                        ", Status: "+voucher.getStatus()+
                        "]");
            }
        }

        System.out.println("Podaj jaki voucher chcesz zrealizować (0 aby anulować): ");

        int voucherId = scanner.nextInt();
        if (voucherId == 0) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucher.setStatus(Voucher.Status.REALIZED);

        voucherDao.update(voucher);

    }
}
