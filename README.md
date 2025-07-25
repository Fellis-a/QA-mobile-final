# StudentsApp - Автотесты для приложения управления студентами

## Описание проекта

Проект содержит автоматизированные тесты для Android-приложения управления студентами

## Структура проекта

### PageObjects (`pageobjects/`)
- `BaseScreen.kt` - базовый класс для всех экранов
- `MainPage.kt` - главный экран со списком студентов
- `PersonItemPage.kt` - экран добавления/редактирования студента
- `SortBottomSheetPage.kt` - экран сортировки

### Тесты (`tests/`)
- `BaseTest.kt` - базовый класс с конфигурацией
- `PersonNameValidationTest.kt` - валидация поля "Имя" 
- `PersonSurnameValidationTest.kt` - валидация поля "Фамилия" 
- `ScoreValidationTest.kt` - валидация поля "Итоговый балл" 
- `EmailValidationTest.kt` - валидация поля "Email"
- `BirthdateValidationTest.kt` - валидация поля "Дата рождения"
- `PersonFormValidationTest.kt` - общие тесты валидации формы
- `PersonItemFormTest.kt` - тесты функциональности формы
- `StudentDeletionPersistenceTest.kt` - тесты удаления и сохранения состояния
- `MainScreenTest.kt` - тесты главного экрана
- `MassOperationsAndSortingTest.kt` - тесты массовых операций

### Моки (`testing/mock/`)
- `FavoritePersonsMock.kt` - основной класс мокирования
- `RandomUserResponseFactory.kt` - фабрика ответов API
- `WireMockScenarioFactory.kt` - сценарии для WireMock

## Как запустить тесты

### Запуск через Android Studio

1. Открыть папку `app/src/androidTest/java/ru/tinkoff/favouritepersons/tests/`
2. Выбрать нужный тестовый класс
3. Нажать ПКМ → "Run tests in ..."
4. Или использовать зеленые кнопки "Play" рядом с методами

## Конфигурация

### WireMock
- Порт: 8080
- Автоматический запуск через `@Rule`
- Моки настроены в `favoritePersonsMock {}`


