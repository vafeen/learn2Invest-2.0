# Learn2Invest
![изображение](https://github.com/vafeen/learn2Invest/assets/67644124/f07c5098-a7da-4cb9-8f0d-de59f0486e6c)

Learn2Invest это биржевой симмулятор для тренировки инвестиционных навыков, с использованием собственного API

- [Learn2Invest](#learn2invest)
  - [Участники проекта](#участники-проекта)
  - [Основной функционал](#основной-функционал)
  - [Технологии](#технологии)
  - [Технические детали реализации](#технические-детали-реализации)
  - [Флоу](#флоу)
  - [Экраны](#экраны)
    - [Splash screen](#splash-screen)
    - [Регистрация](#регистрация)
    - [PIN](#pin)
    - [Портфель](#портфель)
    - [Обзор рынка](#обзор-рынка)
    - [Обзор актива](#обзор-актива)
    - [История](#история)
    - [Настройки](#настройки)

## Участники проекта 
* Воробьев Владимир Васильевич (Arengol (Github)/Vladimir (Имя в локальных настройках git))
* Vafeen
* Черевкова Надежда Александровна (cherevkovanadya (Github)/Nadezhda (Имя в локальных настройках
  git))

## Основной функционал 
* Создание аккаунта
* Авторизация PIN-кодом и биометрией
* Отображение курсов валют
* Фильтры по цене, рыночной капитализации, проценту роста за 24 часа
* Поиск вылют по названию с историей поиска и подсказками
* Виртуальный инвестиционный счет
* Виртуальная покупка/продажа криптовалют
* Обзор инвестиционного портфеля 
* Настройки приложения
* Локализация (ru/en)
* Обновление данных в реальном времени
* Темная тема

## Технологии 
* Retrofit
* Room
* Coroutines
* Biometric
* JetPack Navigation
* Constraintlayout
* Hilt
* Mpandroidchart
* Coil
* Архитектура MVI
* Paging


## Технические детали реализации 
* Backend-заглушка (при каждом обращении выдает валюты с шагом цены <=+-5%)
* Иконки: https://cryptofonts.com/img/icons/{{symbol}}.svg
* Макеты экранов и карта переходов: https://www.figma.com/design/GvomF07D4aJtrFuc3uj4W9/Learn2Invest
* ТЗ: https://necessary-spot-b65.notion.site/Learn2Invest-16e7a0523381411486c2a22513fcae03

Иконки для коинов загружаются с отдельного API, используя данные из поля "symbol" в JSONах основного API. 

## Флоу
https://github.com/vafeen/learn2Invest/assets/67644124/7639e883-1c0b-4ded-ad49-91c798b0c878

## Экраны 
### Splash screen 
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/266d9c22-32ce-429c-8eaf-6d6c84ad46ab" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/d3c5999b-0032-4e28-8f58-77dbf59715d9" width="300"/>

### Регистрация
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/a2231dda-ee04-4f6b-b9c0-6f866735d21b" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/f62095f4-c89a-4d5f-8286-3da9ba101c83" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/feea49cf-4ab8-40a5-b4f5-e8645181c36c" width="300"/>
 
### PIN 
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/7beb25cd-c7e3-44dc-bb41-bef1ee9c1158" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/51e21825-28d5-4c84-b619-e845b39a1c0a" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/37c4e029-e40a-4f0b-964e-7c76d29142af" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/f4a885c4-b4e7-43f2-9d1b-b8f97af9deb1" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/0318aefc-865a-4f24-a73c-0c217af5e050" width="300"/>

### Портфель 
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/13ae0038-727f-4ece-9b41-e0fa1f3b854d" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/5fa76421-f74b-4e6b-819c-a53743ee537d" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/c079e9bb-c60e-4bc2-8fd9-66a647c91048" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/07ec934c-33f8-4752-a5a9-b9a6355713be" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/3412c347-13b8-4ad9-9b9d-3d977ca95c98" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/c3a248fa-f8a7-42a9-b05b-2097cfb29a3e" width="300"/>


### Обзор рынка 
<img src="https://github.com/user-attachments/assets/52ed0880-fa2a-42ad-a179-667033200b1c" width="300"/> <img src="https://github.com/user-attachments/assets/a00d900e-56a9-4f02-864d-90703d3afc04" width="300"/> <img src="https://github.com/user-attachments/assets/786bb777-23b1-4f9c-83c4-a0d47dea485a" width="300"/> <img src="https://github.com/user-attachments/assets/b7c5a54a-64fc-44e2-a0d8-144794d19287" width="300"/> <img src="https://github.com/user-attachments/assets/0647fcde-715a-47bf-9cee-eef2b28e2b54" width="300"/>


### Обзор актива 
<img src="https://github.com/user-attachments/assets/0ab824d0-aa93-45a7-aa49-b5bc94905d36" width="300"/> <img src="https://github.com/user-attachments/assets/51c45527-09a0-4c29-bd24-ec4d72dae904" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/21198a3d-7d33-4aaf-b57f-b773375c152f" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/a35bb0c9-c80e-43ce-998c-b5a50c740161" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/c09205b3-4286-4709-9719-81d40bf274a5" width="300"/>

### История 
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/4b41a24a-3a97-4566-b665-3f425a6c111b" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/a32bb428-4aad-4117-9a23-9bc00da7ecfb" width="300"/>

### Настройки 
<img src="https://github.com/vafeen/learn2Invest/assets/67644124/a7ab6a8a-99cf-459c-9a58-23c38bbc4243" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/9614395e-a854-4dcc-932c-703f733c5f73" width="300"/> <img src="https://github.com/vafeen/learn2Invest/assets/67644124/7d21243f-c22a-4002-9fac-7cb71008fa67" width="300"/>

















