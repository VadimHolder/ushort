# ushort

Встановити docker descktop
Запустити код у docker-compose.yml
Під'єднати MongoDB та Redis на панелі керування в IntellijIdea.

Запустити проект.

Робочі URL:
Перевірка на роботу сервісу:
GET http://localhost:8080

Передача даних на трансформацію:
POST http://localhost:8080/ushort
Body = JSON = {
    "fullUrl":"http://testURL.com"
}

Отримання повної урли після передачі короткої.
http://localhost:8080/u.short/{KEY}
де {KEY} - шифр для короткої урли.


