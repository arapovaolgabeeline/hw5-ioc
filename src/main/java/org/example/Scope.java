package org.example;

public class Scope {
    // 3. IoC предоставляет работу со скоупами для предотвращения сильной связности
    ThreadLocal scope = new ThreadLocal();
}
