## VDA ZAPROS (Verbal Decision Analysis)

Данный репозиторий представляет собою модуль системы ранжирования альтернатив на основе методов семейства ЗАПРОС. Модуль предоставляет 4 метода -- ЗАПРОС II, ЗАПРОС III, ARACE и экспериментальный метод ARACE QV, совмещающий с себе черты методов ARACE и ЗАПРОС III.

Работа проводилась в рамках ВКР в СПбПУ Петра Великого под руководством [В. А. Пархоменко](https://github.com/ParkhomenkoV).

### Использование в проекте

Модуль подключается к проекту так же, как любая зависимость в _java_ -- например, при помощи _maven_. Актуальную версию всегда можно посмотреть [здесь](https://github.com/ADanielGhost/vda-zapros/packages).

### Usage

Для взаимодействия с клиентом используется класс _VdaZaprosFactory_. Он имеет 2 статических метода -- _getService_ и _getConfig_.

Первый применяет параметр типа _MethodType_, который отвечает за метод, который нам нужен и дает на выходе реализацию экземпляра _VdaZaprosService_, который описывает этапы для работы с клиентом.

Второй метод принимает параметр типа _List<Criteria>_, который содержит набор критериев, для которых мы строим конфиг для постоения квазиэкспертов, который в дальнейшем будет использоваться как параметр к методам _VdaZaprosService_.

### Примеры

В примере ниже мы получаем метод ARACE:

    VdaZaprosService serviceQV = VdaZaprosFactory.getService(MethodType.ARACE);
    QuasiExpertConfig config = VdaZaprosFactory.getConfig(criteriaList);

Также, у модуля есть 2 имплементации -- консольная [console-zapros-impl](https://github.com/ADanielGhost/console-zapros-impl) и веб-имплементация [zapros-web](https://github.com/ADanielGhost/zapros-web).