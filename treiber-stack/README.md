# Lock-Free Treiber Stack

[![Build Status](https://travis-ci.com/ITMO-MPP-2018/treiber-stack-tina80lvl.svg?token=B2yLGFz6qwxKVjbLm9Ak&branch=master)](https://travis-ci.com/ITMO-MPP-2018/treiber-stack-tina80lvl)

В рамках данного задания вы познакомитесь с написанием простых lock-free алгоритмов.

## Описание
Проект включает в себя следующие исходные файлы:

* `Stack.java` содержит интерфейс стека.
* `StackImpl.java` содержит реализацию стека для однопоточного случая. Данная реализация небезопасна для использования из нескольких потоков одновременно.
* `pom.xml` содержит описание проекта для системы сборки Maven.

## Задание
Необходимо доработать реализую `StackImpl` так, чтобы она стала безопасной для использования из множества потоков одновременно.

1.	Для реализации необходимо использовать Compare-And-Set операцию. Для этого используйте класс `AtomicReference`, чтобы хранить ссылку на голову стека (поле `head`).
2.	Все операции должны быть линеаризуемы. 

## Сборка и тестирование
Для тестирования используйте команду `mvn test`. При этом автоматически будут запущены следующие тесты:

* `FunctionalTest.java` проверяет базовую корректность стека.
* `LinearizabilityTest.java` проверяет реализацию стека на корректность в многопоточной среде.

## Формат сдачи

Выполняйте задание в этом репозитории. По готовности добавьте "+" в таблицу с оценками в столбец "Готово" текущего задания. 

В случае необходимости доработки домашнего задания после проверки, "+" в таблице замененяется на "?" и создается issue на GitHub-е. Как только необходимые исправления произведены, заменяйте "?" обратно на "+" и закрывайте issue. После этого задание будет проверено ещё раз.

Перед сдачей задания замените `<your_GitHub_account>` в начале данного файла на свой логин в GitHub для получения информации о сборке в Travis. Это нужно сделать в двух местах: ссылка на картинку и на билд в Travis-е.