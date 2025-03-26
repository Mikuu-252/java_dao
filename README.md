## Opis projektu (PL)

Projekt to mały system zarządzania voucherami na atrakcje, stworzony w ramach laboratorium. Główne założenia projektu obejmują:

### Interfejsy użytkownika
- Wersja konsolowa aplikacji  

### Funkcjonalność CRUD
System umożliwia tworzenie, odczytywanie, aktualizację oraz usuwanie danych. Dane są utrwalane w systemie plików lub w bazie danych SQLite.

### Proces realizacji voucherów
System symuluje proces obsługi voucherów na atrakcje, uwzględniając następujących aktorów:
- **Klient:** przegląda ofertę, składa zamówienia, monitoruje status realizacji oraz deklaruje udział w wydarzeniach.
- **Sprzedawca:** redaguje ofertę, zatwierdza zamówienia, przekazuje je do realizacji i aktualizuje ich status.
- **Organizator:** zarządza zgłoszonymi zamówieniami, ustala terminy realizacji i zatwierdza listy uczestników. 
---

## Project Description (EN)

This project is a small voucher management system for attractions, developed as a laboratory exercise. The main objectives include:

### User Interfaces
- A console-based interface  

### CRUD Functionality
The system supports Create, Read, Update, and Delete operations on data, with persistence implemented via file-based storage or a lightweight embedded database (e.g., SQLite or H2).

### Voucher Processing Workflow
The application simulates a voucher processing process for attractions, involving the following actors:
- **Client:** browses offers, places orders, checks order statuses, and declares participation in events.
- **Seller:** manages offers, approves orders, forwards them for processing, and updates order statuses.
- **Organizer:** handles order processing, schedules events, and confirms participant lists.
