## 1. Некорректное исполнение
[![Build Status](https://travis-ci.com/ITMO-MPP-2017/lamport-lock-fail-tina80lvl.svg?token=B2yLGFz6qwxKVjbLm9Ak&branch=master)](https://travis-ci.com/ITMO-MPP-2017/lamport-lock-fail-tina80lvl)


## 2. Исправление алгоритма
```java
threadlocal int id  // 0..N-1 -- идентификатор потока
shared int label[N], flag = 0 // заполненно нулями по умолчанию

def lock:
  1: flag++
  2: my = 1 // номер билета текущего потока
  3: for k in range(N): if k != id:
  4:     my = max(my, label[k] + 1) // должен быть больше, чем у других
  5: label[id] = my // публикуем свой номер билета для других потоков
  6: flag--
  7: for k in range(N): if k != id:
  8:     while true: // пропускаем поток k до тех пока, пока номер его билета меньше
  9:         if (flag == 0 and (label[k], k) > (my, id)): break@6 

def unlock:
  9: label[id] = 0
```