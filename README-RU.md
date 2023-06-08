# Stable Diffusion Image Manager
Простое консольное приложение для менеджмента картинок сгенерированных Stable Diffusion с AUTOMATIC1111.
## Возможности
* Поиск и подсчет количества дубликатов картинок по prompt, steps, sampler, CFG, size, model, но **разными seeds**.
* Группировка в отдельную папку дубликатов картинок по prompt, steps, sampler, CFG, size, model, но **разными seeds**.
* Удаление дубликатов картинок по prompt, steps, sampler, CFG, size, model, но **разными seeds**.
* Удаление картинок Deforum из папок /outputs/img2img-images/Deforum*.
* Создание репорта по существующим картинкам.

## Сборка из исходников

### Требования
* Java 17
* Maven 3.8+
* Git

### Build
```mvn install```

### Run
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar```

## Использование и примеры
Поддерживаются пути Windows, Linux и MacOS.
* **-path** - путь к папке Stable Diffusion outputs
* **-copy** - копирование дубликатов в папки /outputs/\*/\*/duplicates
* **-rollback** - отмена копирования дубликатов из папок /outputs/\*/\*/duplicates
* **-remove** - удаление дубликатов. Если -deforum установлен, то удаляются картинки Deforum
* **-deforum** - удалять\считать картинки Deforum
* **-info** - отобразить количество дубликатов. Если -deforum установлен, то отобразить в том числе количество картинок Deforum
* **-report** - создание репорта в файл /outputs/report.html
### Поиск и отображение дубликатов
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs```
### Группировка дубликатов
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -copy```
### Отмена группировки дубликатов
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -rollback```
### Удаление дубликатов
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -remove```
### Удаление картинок Deforum
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -deforum -remove```
### Подчет количества дубликатов, в том числе картинок Deforum
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -deforum -info```
### Создание репорта
```java -jar target/sd-image-manager-1.0.0-SNAPSHOT.jar -path path/stable-diffusion/outputs -report```

## Навигация по репорту
* Репорт поддерживает пагинацию. 10 картинов на странице. Переход по страницам осуществляется с помощь стрелок или клавиш 'A' и 'D'.
* Для навигации внутри группы можно использовать стрелки или клавиши 'W' и 'S'.
* Увеличить картинку можно кликнув на ряд или нажав клавишу 'ПРОБЕЛ'.

## Планы
- [ ] Добавить просто UI для взаимодействия с приложением
- [ ] Добавить кастомные фильтры для поиска дубликатов
- [ ] Портативная версия
- [ ] Фильтры и строка поиска для отчета
- [ ] ??? Любые другие идеи

## License
The GNU General Public License v3.0 https://www.gnu.org/copyleft/gpl.html