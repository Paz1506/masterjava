package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;

public class MailWSClientMain {
    public static void main(String[] args) {
        MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <Paz1506@mail.ru>")),
                ImmutableSet.of(new Addressee("Copy <Paz1506@gmail.com>")), "Subject", "Body");
    }
}