package pl.edu.pwr.dg.jp.lab03;


import pl.edu.pwr.dg.jp.lab03.data.*;

import java.util.Objects;
import java.util.Scanner;

public class SprzedawcaApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dao<Seller> sellerDao = new SellerDao();

        System.out.println("Witaj w oknie Sprzedawcy");
        System.out.println("0. Dodaj nowego");

        for (Seller seller : sellerDao.getAll()) {
            System.out.println("Seller: [Id : "+seller.getId()+", Name : "+seller.getName()+"]");
        }

        System.out.println("Wybierz swoje konto z listy (id): ");
        int sellerID = scanner.nextInt();
        Seller loginSeller = null;
        if (sellerID != 0){loginSeller = sellerDao.get(sellerID);}
        else { newSeller(scanner, sellerDao); }


        while(loginSeller != null) {
            System.out.println("Witaj " + loginSeller.getName());
            printMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: showVouchers(); break;
                case 2: newVoucher(scanner); break;
                case 3: deleteVoucher(scanner); break;
                case 4: loginSeller = changeName(scanner, sellerDao, loginSeller); break;
                case 5: sellerDao.delete(sellerDao.get(loginSeller.getId())); loginSeller = null; break;
                case 6: loginSeller = null; break;
                default:  System.out.println("Zła opcja wybierz ponownie"); break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("Wybierz opcje z menu: ");
        System.out.println("1. Przeglądaj vouchery");
        System.out.println("2. Wystawić voucher");
        System.out.println("3. Usunąć voucher");
        System.out.println("4. Zmień imie");
        System.out.println("5. Usun konto");
        System.out.println("6. Wyjdź");
    }

    private static Seller changeName(Scanner scanner, Dao<Seller> sellerDao, Seller loginSeller) {
        scanner.nextLine();
        System.out.println("Podaj nowe imię (0 aby anulować): ");
        String newName = scanner.nextLine();
        if(Objects.equals(newName, "0")) return loginSeller;

        sellerDao.update(new Seller(loginSeller.getId(), newName));
        loginSeller.setName(newName);
        return loginSeller;
    }

    private static void newSeller(Scanner scanner, Dao<Seller> sellerDao) {
        scanner.nextLine();
        System.out.println("Podaj imię (0 aby anulować): ");
        String newSellerName = scanner.nextLine();
        if(Objects.equals(newSellerName, "0")) return;

        sellerDao.create(new Seller(0, newSellerName));
    }

    private static void showVouchers() {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
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

    private static void newVoucher(Scanner scanner) {
        Dao<Offer> offerDao = new OfferDao();
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Offer offer : offerDao.getAll()) {
            System.out.println("Voucher: [" +
                    "Id : "+offer.getId()+
                    ", Nazwa Oferty : "+offer.getParameters()+
                    "]");
        }
        System.out.println("Podaj jakiej oferty zrobić voucher (0 aby anulować): ");
        int offerId = scanner.nextInt();
        if(offerId == 0) return;
        Offer offer = offerDao.get(offerId);

        voucherDao.create(new Voucher(0,"Brak", "Brak", offer.getParameters(), "", Voucher.Status.WAIT_FOR_ORGANIZER));
    }

    private static void deleteVoucher(Scanner scanner) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            System.out.println("Voucher: [" +
                    "Id : "+voucher.getId()+
                    ", Nazwa Organizatora : "+voucher.getOrganizerName()+
                    ", Nazwa Klienta : "+voucher.getClientName()+
                    ", Dane oferty : "+voucher.getOfferParameters()+
                    ", Informacje : "+voucher.getInfo()+
                    ", Status : "+voucher.getStatus()+
                    "]");
        }

        System.out.println("Podaj który voucher chcesz usunąć (0 aby anulować): ");
        int voucherId = scanner.nextInt();

        if(voucherId == 0) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucherDao.delete(voucher);
    }
}
