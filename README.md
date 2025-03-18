##Как запустить проект
1) Клонировать репозиторий<br>
https://github.com/Woody-rn/JR-hibernate-final-project.git<br>
2) Убедитесь, что Docker установлен и запущен на вашем компьютере<br>
3) выполните в терминале<br>
   <b>```docker-compose up -d```</b><br>
   Эта команда поднимет все необходимые контейнеры в фоновом режиме.<br>
4) результат доступен в корне проекта <br>
   <b>output/benchmark_results.txt</b><br>
<p></p>
Опционально:<br>
  Для ускорения процесса сборки рекомендуется выполнить <br>
  <b>```mvn clean package```</b><br>
  Это позволит собрать .jar файл заранее, если .jar файл не будет найден, инструкция в Dockerfile будет повторно загружать все зависимости из удаленного репозитория
