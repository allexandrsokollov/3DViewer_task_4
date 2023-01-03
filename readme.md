# Задания для первого человека:


1. DONE! Загрузка и чтение моделей. Вместо сырого ObjReader должна быть качественная протестированная реализация, 
ваша или другого студента, если вы ее не делали в третьей задаче. А также должна быть возможность сохранения модели 
по кнопке в меню. Использоваться должен либо ваш ObjWriter, либо реализация другого студента, если у вас ее нет.

2. TODO! Сцена. Сейчас в программе только одна модель. Нужно добавить работу с несколькими. 
При этом должна остаться возможность перемещать, вращать и както изменять (см. работу другого студента) 
только одну из них. Продумайте, как можно пользователю выбирать, какая/какие (множественный вариант сложнее в 
реализации, но интереснее) из моделей сейчас активные. 
(Выбраны для применения каких-то трансформаций, сохранения и т.д.)

3. DONE! Интерфейс. Эти пункты, а также то, что выполняют другие члены команды, обязывает развивать интерфейс программы. 
В вашу зону ответственности входит написание его таким, чтобы с ним было удобно работать. 
А еще хорошо бы продумать стиль приложения, добавить цвета, оформление. 
При желании можно сделать динамическое переключение тем: например, "светлая/темная".

4. DONE! Обработка ошибок. Пока это касается только ObjReader, но вы можете придумать что-нибудь еще. 
Модуль выплевывает exception’ы, если приходит некорректный файл. Эти исключения пока никак не используются. 
Надо выводить окошко с ошибкой вроде “у вас тут дрянь какая-то”, 
чтобы пользователь мог обдумать свое поведение и щелкнуть “ок” вместо зависания или вылета программы.

5. DONE! Деплой. Провести финальную работу по деплою приложения. 
При последней сдаче его следует запускать не из среды разработки, а как самостоятельное портативное приложение.


# Задания для второго человека:

1. (Сделано) Модуль для математики. В программе в качестве библиотеки для работы с линейной алгеброй используется javax.vecmath. 
А нужно использовать свой модуль, либо то, что реализовал ваш однокурсник, 
если у вас не было такого варианта в третьей задаче. 
То есть нужно переделать всю работу с математикой на наш собственный пакет Math. 

2. (Сделано) Вектора-столбцы. На данный момент во всех матричных преобразованиях в коде предполагается, 
что мы работаем с векторами-строками. Необходимо переделать методы так, чтобы векторы были столбцами.

3. (Сделано) Аффинные преобразования. В программе реализована только часть графического конвейера. 
Нет перегонки из локальных координат в мировые координаты сцены. Вам нужно реализовать ее, 
то есть добавить аффинные преобразования: масштабирование, вращение, перенос. 
Можете использовать наработки студентов из предыдущей задачи. И не забудьте про тесты, 
без них визуально может быть сложно отследить баги.

4. (Сделано) Трансформация модели. После реализации всего конвейера, нужно добавить в меню настройку модели. 
Необходима возможность масштабировать ее вдоль каждой из осей, вокруг каждой из осей поворачивать и перемещать. 
При сохранении модели (см. работу другого студента) следует выбирать, учитывать трансформации модели или нет. 
То есть нужна возможность сохранить как исходную модель, так и модель после преобразований. 

5. Управление камерой. Сейчас взаимодействие с камерой не очень удобно, используется только клавиатура. 
Но можно переделать его, добавив в систему мышь. 
За основу можете взять управление из компьютерной игры или какого-нибудь приложения для работы с трехмерной графикой. 
Здесь хорошо бы продумать горячие клавиши, а также заодно упростить управление моделями.


# Задания для третьего человека:

1. Подготовка к отрисовке. Добавить возможность триангуляции модели и перевычисления нормалей модели после загрузки. 
Можно использовать код однокурсников из третьей задачи. 

2. Растеризация полигонов. Добавить заполнение полигонов одним цветом, используя растеризацию треугольников, 
написанную студентом во втором задании. Учтите, что вам потребуется алгоритм Z-буффера, 
чтобы при рендере задние полигоны не вылезали на передние.

3. Текстура и освещение. Используя уже написанный метод, 
можно легко дополнить программу наложением текстуры (ее как картинку следует подгружать в меню). 
А после следует реализовать простейшую модель освещения. Источник освещения можно привязывать к текущей камере. 
Используя сглаживание нормалей, вы сможете мягко опоясывать объекты светом. 

4. Режимы отрисовки. Когда все описанные выше пункты будут готовы, 
надо добавить в программу возможность переключения между режимами. Чтобы легко перебирать все возможные случаи, 
предлагается оформление в виде галочек (ваши названия могут отличаться):
   * Рисовать полигональную сетку
   * Использовать текстуру
   * Использовать освещение 
   
    Так, например, если не выбрана ни одна галочка, модель окрашивается статическим цветом, 
    выбор которого, кстати, можно вынести в меню. Если добавляется галочка Использовать освещение, 
    цвет по модели становится то ярче, то темнее. При добавлении Использовать текстуру он подменяется на те значения пикселей, 
    что были на текстуре. А галочка Рисовать полигональную сетку нарисует поверх всего контуры полигонов. 
    Тут необязательно, но тоже было бы хорошо добавить Z-буффер (для этого придется вставить самостоятельную растеризацию прямых)

5. Несколько камер. Нужно добавить поддержку нескольких камер в сцене. Задание похоже на добавление нескольких моделей, 
так что имеет смысл обсудить архитектуру приложения с однокурсниками. Должна быть возможность создавать, 
удалять камеры, а также переключаться между ними. Сами камеры тоже могут быть видны на сцене в виде трехмерных моделей.