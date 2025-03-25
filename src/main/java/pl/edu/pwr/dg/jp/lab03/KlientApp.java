package pl.edu.pwr.dg.jp.lab03;


import pl.edu.pwr.dg.jp.lab03.data.*;

import java.util.Objects;
import java.util.Scanner;

public class KlientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dao<Client> clientDao = new ClientDao();

        System.out.println("Witaj w oknie Klienta");
        System.out.println("0. Dodaj nowego");

        for (Client client : clientDao.getAll()) {
            System.out.println("Client: [Id : "+client.getId()+", Name : "+client.getName()+"]");
        }

        System.out.println("Wybierz swoje konto z listy (id): ");
        int clientID = scanner.nextInt();
        Client loginClient = null;
        if (clientID != 0){loginClient = clientDao.get(clientID);}
        else { newClient(scanner, clientDao); }


        while(loginClient != null) {
            System.out.println("Witaj " + loginClient.getName());
            printMenu();

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: showVouchers(); break;
                case 2: showMyVouchers(loginClient); break;
                case 3: buyVouchers(scanner, loginClient); break;
                case 4: declareVoucher(scanner, loginClient); break;
                case 5: loginClient = changeName(scanner, clientDao, loginClient); break;
                case 6: clientDao.delete(clientDao.get(loginClient.getId())); loginClient = null; break;
                case 7: loginClient = null; break;
                default:  System.out.println("Zła opcja wybierz ponownie"); break;
            }
        }
    }


    private static void printMenu() {
        System.out.println("Wybierz opcje z menu: ");
        System.out.println("1. Przeglądaj vouchery");
        System.out.println("2. Moje vouchery");
        System.out.println("3. Kup voucher");
        System.out.println("4. Zadeklaruj swój udział w voucherze");
        System.out.println("5. Zmień imie");
        System.out.println("6. Usun konto");
        System.out.println("7. Wyjdź");
    }

    private static Client changeName(Scanner scanner, Dao<Client> clientDao, Client loginClient) {
        scanner.nextLine();
        System.out.println("Podaj nowe imię (0 aby anulować): ");
        String newName = scanner.nextLine();

        if (Objects.equals(newName, "0")) return loginClient;

        clientDao.update(new Client(loginClient.getId(), newName));
        loginClient.setName(newName);
        return loginClient;
    }

    private static void newClient(Scanner scanner, Dao<Client> clientDao) {
        scanner.nextLine();
        System.out.println("Podaj imię (0 aby anulować): ");
        String newClientName = scanner.nextLine();
        if (Objects.equals(newClientName, "0")) return;

        clientDao.create(new Client(0, newClientName));
    }

    private static void showVouchers() {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getStatus() == Voucher.Status.READY_TO_BUY) {
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
    }

    private static void showMyVouchers(Client loginClient) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getClientName().equals(loginClient.getName())) {
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
    }

    private static void buyVouchers(Scanner scanner, Client loginClient) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getStatus() == Voucher.Status.READY_TO_BUY) {
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

        System.out.println("Podaj jaki voucher chcesz kupić (0 aby anulować): ");
        int voucherId = scanner.nextInt();

        if (voucherId == 0) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucher.setClientName(loginClient.getName());
        voucher.setStatus(Voucher.Status.PURCHASED);

        voucherDao.update(voucher);
    }

    private static void declareVoucher(Scanner scanner, Client loginClient) {
        Dao<Voucher> voucherDao = new VoucherDao();

        for (Voucher voucher : voucherDao.getAll()) {
            if (voucher.getClientName().equals(loginClient.getName()) && voucher.getStatus() == Voucher.Status.TO_REALIZE) {
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

        System.out.println("Podaj na jaki voucher chcesz sie zadeklarować (0 aby anulować): ");
        int voucherId = scanner.nextInt();
        if (voucherId == 0) return;

        Voucher voucher = voucherDao.get(voucherId);
        voucher.setStatus(Voucher.Status.DECLARED);

        voucherDao.update(voucher);

    }

}
