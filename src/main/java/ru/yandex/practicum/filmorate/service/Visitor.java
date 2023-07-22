package ru.yandex.practicum.filmorate.service;

@FunctionalInterface
public interface Visitor<T> {
    void visit(T t);
}
