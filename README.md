# OneResident

Пользовательский ввод для бд по кнопке добавление нового жильца:

Название дома
Номер квартиры
Имя
Дата
Комментарий

Позже, по кнопке "оплачено", должна создаваться запись с текущей датой и временем(история платежей)
Повторяющиеся напоминания с (желательно) настраиваемым интервалом(например 3 часа, 5 часов, день), 
для каждой из квартир, чья дата платежа наступила, а оплату я ещё не подтвердил
Т.е. начиная с уведомления утром в день оплаты, и далее каждые 5 часов.

На главной странице список квартир, ожидающих подтверждения оплаты с кнопками "оплачено", 
"перенесено"* и т.д., которые с главной страницы будут пропадать.

* должно открываться окно ввода количества дней, на которое одноразово отсрочен платеж
(например задержали зп и оплата будет через 4 дня, соответственно именно с того времени будут уведомления)