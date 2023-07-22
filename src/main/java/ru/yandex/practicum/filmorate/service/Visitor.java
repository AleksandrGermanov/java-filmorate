package ru.yandex.practicum.filmorate.service;

@FunctionalInterface
public interface Visitor<Obj> {
    void visit(Obj o);
}
